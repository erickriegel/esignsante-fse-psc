package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;
import com.mongodb.MongoSocketOpenException;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Result;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.PreuveDAO;
import fr.ans.api.sign.esignsante.psc.utils.FileResource;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.api.sign.esignsante.psc.utils.PSCTokenResult;
import fr.ans.api.sign.esignsante.psc.utils.PSCTokenStatus;
import fr.ans.api.sign.esignsante.psc.utils.ParametresSign;
import fr.ans.api.sign.esignsante.psc.utils.TYPE_SIGNATURE;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.OpenidToken;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /Asksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate {

	@Autowired
	EsignsanteCall esignWS;

	@Autowired
	PreuveDAO dao;

	@Autowired
	ProsanteConnectCalls pscApi;

	@Override
	public ResponseEntity<org.springframework.core.io.Resource> postAskSignaturePades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {

		log.debug("Réception d'une demande de signature Pades");
		// ResponseEntity<Resource> re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		log.error("!!bypass du controle Accpet HEader sur une demande de signature XADES");
//		if ((isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)
//				&& isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_PDF)) == false) {
//			msgError = "Le header doit contenir s application/json et application/pdf";
//			log.error("Demande de signature Xades: rejet pour accept Header non conforme. \n getAcceptHeaders(): {}", getAcceptHeaders());
//			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//		}

		ParametresSign params = prepareAppelEsignWS(accessToken, file, userinfo);
//		 if (! params.getStatus().equals(HttpStatus.OK)) {
//			 return new ResponseEntity<>(params.getStatus());
//			}

		// verification du type de fichier
		if (!(checkTypeFile(params.getFileToSign()).equals(Helper.APPLICATION_PDF))) {
			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}

		log.debug(
				"Demande de signature valid: verif AccessToken OK: {} \n, File OK {} \n, userInfo OK Organisation: {}",
				params.getPscReponse(), params.getFileToSign().getName(),
				params.getUserinfo().getSubjectOrganization());
		// construction du OpenIDToken
		UserInfo user = params.getUserinfo();
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		openidToken.setAccessToken(accessToken);
		openidToken.setIntrospectionResponse(params.getPscReponse());
		openidToken.setUserInfo(userinfo);
		openidTokens.add(openidToken);

		List<String> signers = new ArrayList<String>();

		log.debug("openidtoken[0] transmis à esignWS:");
		log.debug("\t userinfo (base64): {} ", openidTokens.get(0).getUserInfo());
		log.debug("\t accessToken: {} ", openidTokens.get(0).getAccessToken());
		log.debug("\t PSCResponse: {} ", openidTokens.get(0).getIntrospectionResponse());

//		////////////////////////////////////////////////////////////////////////////////////////////////
		log.debug("Appel esignWS pour une signature PADES");
		ESignSanteSignatureReportWithProof report = null;
		try {
			report = esignWS.signaturePades(params.getFileToSign(), signers, params.getRequestID(), openidTokens);
		} catch (RestClientException e) {
			log.error("RestClientException  lors d'un appel à esignsante ");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
//		///////////////////////////////////////////////////////////////////////////////////////////////////////

		// archivage BDD
		try {
			archivagePreuve(report, params.getRequestID(), user, params.getDate());
		} catch (Exception e) {
			log.error("Exception lors de l'archivage en BDD ");
//			log.error("e.msg {} \t classe: {}", e.getMessage(), e.getClass());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}

//		// Formatage retour
		String signedStringBase64 = report.getDocSigne();
		// log.debug("signedStringBase64 {}", signedStringBase64);
		String signedString = null;
		try {
			signedString = Helper.decodeBase64(signedStringBase64);
		} catch (UnsupportedEncodingException e) {
			log.error("PADES: pb decodage base 64 du fichier signeré");
			e.printStackTrace();
		}
		log.debug("signedString {}", signedString);

		FileResource docRetourned = new FileResource(signedString.getBytes(), "SIGNED" + file.getOriginalFilename());
//		Resource resource = new ByteArrayResource(signedString.getBytes());
		// return new ResponseEntity<>(resource, HttpStatus.OK);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_PDF);
		return new ResponseEntity<Resource>(docRetourned, httpHeaders, HttpStatus.OK);
	}

	@Override
//	public ResponseEntity<String> /* ResponseEntity<Document> */ postAsksignatureXades(
//			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
//			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
//			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {
//
//		log.debug("Réception d'une demande de signature Xades");
//		// SI pas interception de Spring -> TODO créer nouvelle methode au lieu de 2
//		// appels ...
//		log.error("!!bypass du controle Accpet HEader sur une demande de signature XADES");
////		if ((isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)
////				&& isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_XML)) == false) {
////			msgError = "Le header doit contenir s application/json et application/xml";
////			log.error("Demande de signature Xades: rejet pour accept Header non conforme. \n getAcceptHeaders(): {}", getAcceptHeaders());
////			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
////		}
//
//		// Vérification des paramètres, appel PSC, formatge des paramètres pour esignWS
//		 ParametresSign params = prepareAppelEsignWS(accessToken, file, userinfo);
////		 if (! params.getStatus().equals(HttpStatus.OK)) {
////			 return new ResponseEntity<>(params.getStatus());
////			}
//
//		 //jusrte pour trace du type de fichier en debug
//		 checkTypeFile(params.getFileToSign());
//		 
//	 log.debug("Demande de signature valid: verif AccessToken OK: {} \n, File OK {} \n, userInfo OK Organisation: {}",
//			 params.getPscReponse(), params.getFileToSign().getName(), params.getUserinfo().getSubjectOrganization());
//		// construction du OpenIDToken
//		 UserInfo user = params.getUserinfo();
//		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
//		OpenidToken openidToken = new OpenidToken();
//		openidToken.setAccessToken(accessToken);
//		openidToken.setIntrospectionResponse(params.getPscReponse());
//		openidToken.setUserInfo(userinfo);
//		openidTokens.add(openidToken);
//
//		List<String> signers = new ArrayList<String>();
//	
//			////////////////////////////////////////////////////////////////////////////////////////////////
//		    log.debug("openidtoken[0] transmis à esignsanteWS:");
//		    log.debug("\t userinfo (base64): {} ",openidTokens.get(0).getUserInfo());
//		    log.debug("\t accessToken: {} ",openidTokens.get(0).getAccessToken());
//		    log.debug("\t PSCResponse: {} ",openidTokens.get(0).getIntrospectionResponse());
//		
//			ESignSanteSignatureReportWithProof report = null;
//			try {
//				report = esignWS.signatureXades(params.getFileToSign(), signers, params.getRequestID(), openidTokens);
//			} catch (RestClientException e) {
//				throwExceptionRequestError(e,
//						"Exception sur appel esignWS. Service inaccessible",
//						HttpStatus.SERVICE_UNAVAILABLE);			}
//			///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//			log.debug("retour appel esignsante xades - formatage de la réponse");
//			log.debug("report ... \n \tValide {} \n \tErrors {} ", report.isValide(),
//					report.getErreurs());
//
//
//			// archivage BDD
//			try {
//			archivagePreuve(report, params.getRequestID(),user, params.getDate());
//			} catch (Exception e) {
//				throwExceptionRequestError(e,
//						"Requête abandonnée pour problème d'archivage de la preuve en base de données. Base inaccessible",
//						HttpStatus.SERVICE_UNAVAILABLE);
//			}
//
//
//			String signedStringBase64 = report.getDocSigne();
//
//			String signedString = null;
//			try {
//				signedString = Helper.decodeBase64(signedStringBase64);
//			} catch (UnsupportedEncodingException e) {			
//				e.printStackTrace();
//				throwExceptionRequestError(e,
//						"Exception Serveur",
//						HttpStatus.INTERNAL_SERVER_ERROR);
//	
//			}
//			log.debug("signedString {}", signedString);
//
////			File fileOut  = new File();
////			Path pathOut = new Pat
////					
////			pathOut = Files.writeString(Path.of("my", "path"), signedString, StandardCharset.UTF_8);
////			
////			signedDoc.setFile(resource);
////			Resource resource = fileOut;
////			FileWriter writer = new FileWriter(fileOut);
////			writer.write(signedString);
////			writer.close();
//
//			///////////////////////////////////////////////
//			// test avec FIle
////			 File tmpFile = File.createTempFile("response", ".xml");
////			    FileWriter writer = new FileWriter(tmpFile);
////			    writer.write(signedString);
////			    writer.close();
////			 log.debug("écriture du fichier tmp");
////			 log.debug("File name {} exist {} path {} length {} " ,tmpFile.getName(), tmpFile.exists(), tmpFile.getAbsolutePath(),
////					 tmpFile.length());
////			 
////			 Resource resourceOut = new FileSystemResource(tmpFile);
////			    signedDoc.setFile(resourceOut);
////			    log.debug("ICI");
//			// re = new ResponseEntity<>(signedDoc, HttpStatus.OK);
//			// FIN test avec FIle
//			///////////////////////////////////////////////
//
//			//////////////////////////////////////////////
//			// test avec Input Stream
//			/*
//			 * InputStream targetStream = new ByteArrayInputStream(signedString.getBytes());
//			 * Document returned = new Document(); returned.setFile(new
//			 * InputStreamResource(targetStream));
//			 */
//
//			////////////////////////////////////////////////
//			// test ....
////			Resource resource = new ByteArrayResource(signedString.getBytes());
////			Document returned = new Document();
////			returned.setFile(resource);
//			
//			//reponse 'simple'
//			//return new ResponseEntity<>(signedString, HttpStatus.OK);
//			//reponse avec type en retour
//			 HttpHeaders httpHeaders = new HttpHeaders();
//			 httpHeaders.setContentType(MediaType.APPLICATION_XML);
//			return  new ResponseEntity<>(signedString,httpHeaders, HttpStatus.OK);	
//
//	}
//	

	public ResponseEntity<String> /* ResponseEntity<Document> */ postAsksignatureXades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {

		log.debug("Réception d'une demande de signature Xades");
		// SI pas interception de Spring -> TODO créer nouvelle methode au lieu de 2
		// appels ...
		log.error("!!bypass du controle Accpet HEader sur une demande de signature XADES");
//		if ((isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)
//				&& isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_XML)) == false) {
//			msgError = "Le header doit contenir s application/json et application/xml";
//			log.error("Demande de signature Xades: rejet pour accept Header non conforme. \n getAcceptHeaders(): {}", getAcceptHeaders());
//			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//		}

		ESignSanteSignatureReportWithProof report = executeAskSignature(TYPE_SIGNATURE.XADES, accessToken, file,
				userinfo);

		String signedStringBase64 = report.getDocSigne();

		String signedString = null;
		try {
			signedString = Helper.decodeBase64(signedStringBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throwExceptionRequestError(e, "Exception Serveur", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		log.debug("signedString {}", signedString);

		// reponse avec type en retour
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<>(signedString, httpHeaders, HttpStatus.OK);

//			File fileOut  = new File();
//			Path pathOut = new Pat
//					
//			pathOut = Files.writeString(Path.of("my", "path"), signedString, StandardCharset.UTF_8);
//			
//			signedDoc.setFile(resource);
//			Resource resource = fileOut;
//			FileWriter writer = new FileWriter(fileOut);
//			writer.write(signedString);
//			writer.close();

		///////////////////////////////////////////////
		// test avec FIle
//			 File tmpFile = File.createTempFile("response", ".xml");
//			    FileWriter writer = new FileWriter(tmpFile);
//			    writer.write(signedString);
//			    writer.close();
//			 log.debug("écriture du fichier tmp");
//			 log.debug("File name {} exist {} path {} length {} " ,tmpFile.getName(), tmpFile.exists(), tmpFile.getAbsolutePath(),
//					 tmpFile.length());
//			 
//			 Resource resourceOut = new FileSystemResource(tmpFile);
//			    signedDoc.setFile(resourceOut);
//			    log.debug("ICI");
		// re = new ResponseEntity<>(signedDoc, HttpStatus.OK);
		// FIN test avec FIle
		///////////////////////////////////////////////

		//////////////////////////////////////////////
		// test avec Input Stream
		/*
		 * InputStream targetStream = new ByteArrayInputStream(signedString.getBytes());
		 * Document returned = new Document(); returned.setFile(new
		 * InputStreamResource(targetStream));
		 */

		////////////////////////////////////////////////
		// test ....
//			Resource resource = new ByteArrayResource(signedString.getBytes());
//			Document returned = new Document();
//			returned.setFile(resource);

		// reponse 'simple'
		// return new ResponseEntity<>(signedString, HttpStatus.OK);

	}

//	private PSCTokenResult checkPSCToken(String token) {
//		PSCTokenResult retour = new PSCTokenResult(PSCTokenStatus.TECH_ERROR, "Reponse non connue");
//		try {
//			String result = pscApi.isTokenActive(token);
//			retour.setStatus(Helper.parsePSCresponse(result));
//			retour.setPscResponse(result);
//			log.debug("Appel PSC intropesction: token= {}  reponse = {}  PSCTokenStatus = {}", token, retour.getPscResponse(),
//					retour.getStatus());
//			log.error("!!!!!!!!!!  ICI BYPASS => ON force le status du Token à valid ...");
//			/////////////////////////////////////////////////////////////////////////
//			       retour.setStatus(PSCTokenStatus.VALID);
//			/////////////////////////////////////////////////////////////////////////
//		} catch (JsonProcessingException e1) {
//			log.error("Exception sur introspection PSC token= {}  erreur: JsonProcessingException \n {}", token, e1.getMessage());
//			e1.printStackTrace();
//			retour.setStatus(PSCTokenStatus.TECH_ERROR);
//		} catch (UnsupportedEncodingException e1) {
//			log.error("Exception sur introspection PSC token= {}  erreur: UnsupportedEncodingException \n {}",token,  e1.getMessage());
//			e1.printStackTrace();
//			retour.setStatus(PSCTokenStatus.TECH_ERROR);
//		} catch (URISyntaxException e1) {
//			log.error("Exception sur introspection PSC token= {} erreur: URISyntaxException \n {}", token, e1.getMessage());
//			e1.printStackTrace();
//			retour.setStatus(PSCTokenStatus.TECH_ERROR);
//		}
//		return retour;
//	}
//	

	private String checkPSCToken(String token) {
		// PSCTokenResult retour = new PSCTokenResult(PSCTokenStatus.TECH_ERROR,
		// "Reponse non connue");
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String msgError = "Exception sur vérfication de la validté du token PSC: " + token;
		String result = "Reponse ProSanteConnect inconnue";
		Boolean bypass = pscApi.bypassResultatPSC();
		try {
			result = pscApi.isTokenActive(token);
			httpStatus = Helper.parsePSCresponse(result);
			log.debug("Appel PSC intropesction: token= {}  reponse PSC = {} ", token, result);
			log.debug("TODELETE" + httpStatus);
			if (httpStatus == HttpStatus.BAD_REQUEST) {
				msgError = "L'accessToken fourni dans la requête n'est pas reconnu comme un token actif par ProSanteConnect token: "
						+ token + " response PSC: " + result;
				
			}
		} catch (JsonProcessingException | UnsupportedEncodingException | URISyntaxException e1) {
			msgError.concat(" , JsonProcessingException");
			e1.printStackTrace();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		if (!bypass) {
			if (!httpStatus.equals(HttpStatus.OK)) {
				throwExceptionRequestError(msgError, httpStatus);
			}
		} else {
			log.error("!!!!!!!!!!!!!!!!!!!");
			log.error("!!!!!!!!!!!!!!! ATTENTION BYPASS du resultat de l'introspection ProsanteConnect");
			log.error("!!!!!!!!!!!!!!!!!!!");
		}

		return result;
	}

	private void testRetourXML(String docSigne) {
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("file", new ByteArrayResource(docSigne.getBytes()) {

			@Override
			public String getFilename() {
				return "docSigned.xml";
			}

		}, MediaType.APPLICATION_XML);
		bodyBuilder.build();
		// bodyBuilder.
	}

//	private File checkInputFile(MultipartFile file)  {
//		File fileToSign = null;
//		try {
//			fileToSign = multipartFileToFile(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//			
//		}
//		log.debug("fileToCheck length" + fileToSign.length());
//		log.debug("fileToCheck isFile" + fileToSign.isFile());
//		//test verification du type de fichier
//		return fileToSign;
//	}

//	private void archivagePreuve(ESignSanteSignatureReportWithProof report, String requestID,
//			Map<String, String> userToPersit, Date now) {
//
//		// vrai archivage de la pruve
//		log.debug(" START archivage de la preuve en BDD");
//		ProofStorage proof = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
//				userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
//				userToPersit.get(Helper.FAMILY_NAME), now, report.getPreuve());
//
//		dao.archivePreuve(proof);
//		log.debug("FIN archivage de la preuve en BDD");
//
//	}

	private void archivagePreuve(ESignSanteSignatureReportWithProof report, String requestID, UserInfo userToPersit,
			Date now) {

		// vrai archivage de la preuve
		log.debug(" START archivage de la preuve en BDD pour request id: {}", requestID);
		ProofStorage proof = new ProofStorage(requestID, userToPersit.getSubjectOrganization(),
				userToPersit.getPreferredUsername(), userToPersit.getGivenName(), userToPersit.getFamilyName(), now,
				report.getPreuve());
		// archivage BDD
		try {
			dao.archivePreuve(proof);
		} catch (Exception e ) { 
			e.printStackTrace();
				throwExceptionRequestError(e,
						"Requête abandonnée pour problème d'archivage de la preuve en base de données. ",
						HttpStatus.SERVICE_UNAVAILABLE);
		}

		log.debug("Fin archivage de la preuve en BDD pour request id: {}\", requestID");
	}

	private Map<String, String> extracMaptUserToPersit(String jsonUserInfoBase64Encoded) {// extraction des champs du
																							// userInfo pour sauvegarde
																							// dans la preuve MongoDB
		Map<String, String> userToPersit = null;
		try {
			userToPersit = Helper.jsonStringBase64ToPartialMap(jsonUserInfoBase64Encoded);

		} catch (JsonMappingException e) {
			e.printStackTrace();
			log.error("UserInfo:  JsonMappingException ");

		} catch (JsonProcessingException e) {
			log.error("UserInfo:  JsonProcessingException ");
			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("UserInfo:  UnsupportedEncodingException ");
		}
		return userToPersit; // si null -> return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	private fr.ans.api.sign.esignsante.psc.model.Error buildError(String code, String msg) {
		// if (!acceptHeader.isPresent()
		fr.ans.api.sign.esignsante.psc.model.Error erreur = new fr.ans.api.sign.esignsante.psc.model.Error();
		erreur.setCode(code);
		erreur.setMessage(msg);
		return erreur;
	}

	private UserInfo extractUserInfoFromRequest(String jsonUserInfoBase64) {
		UserInfo userInfo = null;
		try {
			userInfo = Helper.jsonBase64StringToUserInfo(jsonUserInfoBase64);
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throwExceptionRequestError(e, "Exception en parsant le userInfo reçu: " + jsonUserInfoBase64,
					HttpStatus.BAD_REQUEST);
		}
		return userInfo;
	}

	/*
	 * Methode qui vérifie les paramètres d'entrée de la requête (hors HEader
	 * Accept) et les formate pour esignWS et MongoDB
	 */
	private ParametresSign prepareAppelEsignWS(String accessToken, MultipartFile file, String userinfo) {
		ParametresSign resultat = new ParametresSign();

		//
		resultat.setDate(new Date());

		// verification de l'accessToken
		String pscResult = checkPSCToken(accessToken);
		resultat.setPscReponse(pscResult);

		// fichier reçu
		File fileToSign = getMultiPartFile(file);
		resultat.setFileToSign(fileToSign);

		// extraction des champs du userInfo pour sauvegarde dans la preuve MongoDB
		UserInfo userToPersit = extractUserInfoFromRequest(userinfo);
		resultat.setUserinfo(userToPersit);

		// génération d'un UUDI
		resultat.setRequestID(Helper.generateRequestId());

		return resultat;

	}

	private ESignSanteSignatureReportWithProof executeAskSignature(TYPE_SIGNATURE typeSignature, String accessToken,
			MultipartFile file, String userinfo) {

		ParametresSign params = prepareAppelEsignWS(accessToken, file, userinfo);

		// verification du type de fichier
		String typeFile = checkTypeFile(params.getFileToSign());
		// contrôle uniquement des PDFs
		if (typeSignature.equals(TYPE_SIGNATURE.PADES)) {
			if (!typeFile.equals(Helper.APPLICATION_PDF)) {
				throwExceptionRequestError(
						"Requête rejetée car le fichier transmis semble ne pas être un PDF, type détecté  " + typeFile,
						HttpStatus.BAD_REQUEST);
			}
		}

		log.debug(
				"Demande de signature valid: verif AccessToken OK: {} \n, File OK {} \n, userInfo OK Organisation: {}",

				// Construction des l'OpneIdToken
				params.getPscReponse(), params.getFileToSign().getName(),
				params.getUserinfo().getSubjectOrganization());
		// construction du OpenIDToken
		UserInfo user = params.getUserinfo();
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		openidToken.setAccessToken(accessToken);
		openidToken.setIntrospectionResponse(params.getPscReponse());
		openidToken.setUserInfo(userinfo);
		openidTokens.add(openidToken);
		log.debug("openidtoken[0] transmis à esignWS:");
		log.debug("\t userinfo (base64): {} ", openidTokens.get(0).getUserInfo());
		log.debug("\t accessToken: {} ", openidTokens.get(0).getAccessToken());
		log.debug("\t PSCResponse: {} ", openidTokens.get(0).getIntrospectionResponse());

		// liste des signataires
		List<String> signers = new ArrayList<String>();

		// Appel esignsante
		log.debug("Appel esignWS pour une signature de type {}", typeSignature.getTypeSignature());
		ESignSanteSignatureReportWithProof report = null;
		try {
			if (typeSignature.getTypeSignature().equals(TYPE_SIGNATURE.PADES.toString())) {
				report = esignWS.signaturePades(params.getFileToSign(), signers, params.getRequestID(), openidTokens);
			} else {
				report = esignWS.signatureXades(params.getFileToSign(), signers, params.getRequestID(), openidTokens);
			}
		} catch (RestClientException e) {
			throwExceptionRequestError(e, "Exception sur appel esignWS. Service inaccessible",
					HttpStatus.SERVICE_UNAVAILABLE);
		}

		archivagePreuve(report, params.getRequestID(), user, params.getDate());
//		// archivage BDD
//		try {
//			archivagePreuve(report, params.getRequestID(), user, params.getDate());
////		} catch (MongoSocketOpenException | RestClientException e) { 
////			throwExceptionRequestError(e,
////					"Requête abandonnée pour problème d'archivage de la preuve en base de données. Base inaccessible",
////					HttpStatus.SERVICE_UNAVAILABLE);
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (e instanceof MongoSocketOpenException) {
//				throwExceptionRequestError(e,
//						"Requête abandonnée pour problème d'archivage de la preuve en base de données. Base inaccessible",
//						HttpStatus.SERVICE_UNAVAILABLE);
//			} else {
//				throwExceptionRequestError(e,
//						"Requête abandonnée pour problème d'archivage de la preuve en base de données. ",
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
		return report;
	}

}
