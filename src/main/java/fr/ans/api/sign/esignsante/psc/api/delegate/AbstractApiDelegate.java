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
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
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

	final protected static String HEADER_TYPE = "application/json";
	protected String msgError = "";

	final private static Path TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir")); 
	/**
	 * Gets the request
	 *
	 * @return the request
	 */
	public Optional<NativeWebRequest> getRequest() {
		Optional<NativeWebRequest> request = Optional.empty();
		final ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();

		if (attrs != null) {
			request = Optional.of(new ServletWebRequest(attrs.getRequest()));
		}
		return request;
	}

	/**
	 * Gets the accept header.
	 *
	 * @return the accept header
	 */
	public Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	public List<String> getAcceptHeaders() {

		Optional<String[]> accpetHeaders = getRequest().map(r -> r.getHeaderValues("accept"));
		if (accpetHeaders.isPresent() && !accpetHeaders.isEmpty()) {
			return Arrays.asList(accpetHeaders.get());
		}
		return new ArrayList<String>();
	}

	protected File multipartFileToFile(MultipartFile multipart) throws IOException {

		log.debug(" multipart.getOriginalFilename: {}", multipart.getOriginalFilename());

		
		
		Path tempFile = Files.createTempFile(TMP_PATH, multipart.getOriginalFilename(), null);
		log.debug("tempFile {}", tempFile.getFileName().toString());
		multipart.transferTo(tempFile);
		return tempFile.toFile();
	}

	protected File getMultiPartFile(MultipartFile file) {
		File fileToCheck = null;
		try {
			fileToCheck = multipartFileToFile(file);
			log.debug("fileToCheck isFile: {} \t name: {} \t length {}", fileToCheck.isFile(), fileToCheck.getName(),
					fileToCheck.length());
		} catch (IOException e) {
			e.printStackTrace();
			throwExceptionRequestError(e, "Erreur dans la lecture du fichier reçu", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return fileToCheck;
		
	}

	protected Boolean isExpectedAcceptHeader(Optional<String> header, String expectedAcceptHeader) {
		Boolean retour = false;
		if (header.isPresent() && header.get().contains(expectedAcceptHeader)) {
			retour = true;
		}
		return retour;
	}

	protected Boolean isAcceptHeaderPresent(List<String> acceptheaders, String expectedAcceptHeader) {
		return acceptheaders.contains(expectedAcceptHeader);
	}

	protected String checkTypeFile(File fichierAtester) {
		Tika tika = new Tika();
		String detectedType = "";
		try {
			detectedType = tika.detect(fichierAtester);
			log.debug("Verification du fichier {} type detecté: {}", fichierAtester.getName(), detectedType);
		} catch (IOException e) {
			e.printStackTrace();
			throwExceptionRequestError(e, "Execption sur vérification du type de fichier transmis",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return detectedType;
	}

	protected void throwExceptionRequestError(Exception e, String msg, HttpStatus status) {
		log.error("Requête en echec. Message retourné à l'utilisateur: {}", msg);
		log.error("classe Exception: {}", e.getClass().getName());
		log.error("cause de l'eException: {}", e.getCause());
		log.error("message de l'exception {}", e.getMessage());
		throwExceptionRequestError(msg, status);
	}

	protected void throwExceptionRequestError(String msg, HttpStatus status) {
		fr.ans.api.sign.esignsante.psc.model.Error erreur = new fr.ans.api.sign.esignsante.psc.model.Error();
		erreur.setCode(status.toString());
		erreur.setMessage(msg);
		throw new EsignPSCRequestException(erreur, status);

	}

}
