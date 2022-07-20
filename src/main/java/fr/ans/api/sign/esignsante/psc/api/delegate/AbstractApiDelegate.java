/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ApiDelegate. Classe mère de tous les delegates. Traitement
 * générique des requêtes
 */


@Slf4j
public abstract class AbstractApiDelegate {

	// Nom des headers attendus
    public static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    public static final String HEADER_NAME_USERINFO = "X-Userinfo";
    public static final String HEADER_NAME_INTROSPECTION_RESPONSE = "X-Introspection-Response";
    public static final String HEADER_NAME_ACCEPT = "Accept";
    
    //token
    public static final String TOKEN_HEADER_PREFIX_BEARER = "Bearer";
	
	//Valeurs reconnues du Header 'accept'
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String HEADER_TYPE_APP_WILDCARD = "application/*";
	public static final String HEADER_TYPE_FULL_WILDCARD = "*/*";
	
	
	protected String msgError = "";

	private static final Path TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"));

	protected HttpServletRequest httpRequest = null;

	/**
	 * Gets the request
	 *
	 * @return the request
	 */
	public Optional<NativeWebRequest> getRequest() {
		final ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();

		Optional<NativeWebRequest> request = Optional.of(new ServletWebRequest(attrs.getRequest()));
		return request;
	}

	/**
	 * Gets the HttpServletRequest
	 *
	 * @return the HttpServletRequest
	 */

	public HttpServletRequest getHttpRequest() {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			this.httpRequest = attrs.getRequest();
			System.out.println(attrs.getAttributeNames(0));
			
		return this.httpRequest;
	}

	/*
	 * 
	 */
	// String> => Map<String, LIst<String> ..
	public Map<String, String> getUsedHeaders() {
		List<String> usedHeaders = new ArrayList<String>();
		usedHeaders.add(HEADER_NAME_AUTHORIZATION);
		usedHeaders.add(HEADER_NAME_USERINFO);
		usedHeaders.add(HEADER_NAME_INTROSPECTION_RESPONSE);
		Map<String, String> headers = Collections.list(getHttpRequest().getHeaderNames()).stream()
				.filter(h -> usedHeaders.contains(h)).collect(Collectors.toMap(h -> h, httpRequest::getHeader));
		return headers;
	}

	/**
	 * Gets the accept header.
	 *
	 * @return the accept header
	 */
	public List<String> getAcceptHeaders() {

		List<String> acceptes = new ArrayList<>();
		Enumeration<String> acceptheaders = getHttpRequest().getHeaders(HEADER_NAME_ACCEPT);
		while (acceptheaders.hasMoreElements()) {
			List<String> tmp = Arrays.asList(acceptheaders.nextElement().trim().split(","));
			tmp.replaceAll(x -> x.trim());
			acceptes.addAll(tmp);
		}
		return acceptes;
	}

	/**
	 * Gets the accessToken
	 *
	 * @return the PSC accessToken
	 */

	public String getAccessToken() {
		List<String> tmp = new ArrayList<String>();
		Enumeration<String> tokens = getHttpRequest().getHeaders(HEADER_NAME_AUTHORIZATION);
		while (tokens.hasMoreElements()) {
			log.debug("Au moins un header 'Authorization' trouvé ");
			String token = tokens.nextElement();
			if (token.startsWith(TOKEN_HEADER_PREFIX_BEARER)) {
				tmp.add(StringUtils.deleteWhitespace(token).substring(TOKEN_HEADER_PREFIX_BEARER.length()));
				log.debug("token 'Bearer' trouvé dans un header 'Authorization': {} ", token);
			}
		}
		if ((tmp.size() == 0) || (tmp.size() > 1)) {
			throwExceptionRequestError("Token non trouvé dans les headers de la requête (ou plusieurs token)", HttpStatus.BAD_REQUEST);
		}
		log.debug("accessToken received (without prefix): {}", tmp.get(0));

		return tmp.get(0);
	}

	public List<String> getHeaderNames() {
		Enumeration<String> headerNames = getHttpRequest().getHeaderNames();
		return Collections.list(headerNames);
	}

	public File multipartFileToFile(MultipartFile multipart) throws IOException {

		String unsafeFileName = multipart.getOriginalFilename();
		String pureFilename = (new File(unsafeFileName)).getName();

		Path tempFile = Files.createTempFile(TMP_PATH, pureFilename, null);
		log.debug(" multipart.getOriginalFilename: {}. Use {} as name", unsafeFileName, pureFilename);
		log.debug("tempFile {}", tempFile.getFileName().toString());
		multipart.transferTo(tempFile);
		return tempFile.toFile();
	}

	public File getMultiPartFile(MultipartFile file) {
		File fileToCheck = null;
		try {
			fileToCheck = multipartFileToFile(file);
			log.debug("fileToCheck isFile: {} \t name: {} \t length {}", fileToCheck.isFile(), fileToCheck.getName(),
					fileToCheck.length());
		} catch (IOException e) {
			throwExceptionRequestError(e, "Erreur dans la lecture du fichier reçu", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return fileToCheck;

	}

	protected Boolean isAcceptHeaderPresent(List<String> acceptheaders, String expectedAcceptHeader) {
		return (acceptheaders.contains(expectedAcceptHeader) || acceptheaders.contains(HEADER_TYPE_APP_WILDCARD)
				|| acceptheaders.contains(HEADER_TYPE_FULL_WILDCARD));
	}

	protected String checkTypeFile(File fichierAtester) {
		var tika = new Tika();
		var detectedType = "";
		try {
			detectedType = tika.detect(fichierAtester);
			log.debug("Verification du fichier {} type detecté: {}", fichierAtester.getName(), detectedType);
		} catch (IOException e) {
			log.debug(e.toString());
			throwExceptionRequestError(e, "Execption sur vérification du type de fichier transmis",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return detectedType;
	}

	protected void throwExceptionRequestError(Exception e, String msg, HttpStatus status) {
		log.debug("Requête en echec. Message retourné à l'utilisateur: {}", msg);
		log.debug("classe Exception: {}", e.getClass().getName());
		log.debug("cause de l'eException: {}", e.getCause());
		log.debug("message de l'exception {}", e.getMessage());
		throwExceptionRequestError(msg, status);
	}

	protected void throwExceptionRequestError(String msg, HttpStatus status) {
		var erreur = new fr.ans.api.sign.esignsante.psc.model.Error();
		erreur.setCode(status.toString());
		erreur.setMessage(msg);
		throw new EsignPSCRequestException(erreur, status);

	}

}
