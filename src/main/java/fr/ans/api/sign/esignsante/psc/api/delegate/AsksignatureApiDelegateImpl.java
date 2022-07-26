/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.prosantedatas.PSCData;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.PreuveDAO;
import fr.ans.api.sign.esignsante.psc.utils.FileResource;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.api.sign.esignsante.psc.utils.ParametresSign;
import fr.ans.api.sign.esignsante.psc.utils.TYPE_SIGNATURE;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.Erreur;
import fr.ans.esignsante.model.OpenidToken;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /Asksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate {

	/*
	 * Appel esignSanteWebservices liste Cas, contrôles et demandes de signatures
	 */
	@Autowired
	EsignsanteCall esignWS;

	/*
	 * Acces à MOngoDB Archivage de la preuve sur une demande de signature
	 */
	@Autowired
	PreuveDAO dao;
	
	@Value("${with.gravitee:false}")
    private String withGravitee;

	/*
	 * Appel ProSanteConnect pour vérification de la validité de l'accessToken
	 * (Introspection) + optention du userInfo. Utilisé/Instancié ssi il exite la propriété gravitee=true
	 */
	
	@Autowired
	PSCData pscApi;

	public static final String SIGNER_XADES = "Délégataire de signature pour ";

	public static final String SIGNER_PADES = "Signé pour le compte de ";


	@Override
	    public ResponseEntity<org.springframework.core.io.Resource> postAskSignaturePades(MultipartFile file,
	    		String xUserinfo,
		        String xIntrospectionResponse) {	
		log.debug("Réception d'une demande de signature Pades");
		

		//Vérification des types acceptés en retour
		List<String> acceptedHeaders = getAcceptHeaders();
		if (!isAcceptHeaderPresent(acceptedHeaders,APPLICATION_JSON)) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		if (!isAcceptHeaderPresent(acceptedHeaders, APPLICATION_PDF)) {
			throwExceptionRequestError("Le header 'accept' doit contenir  application/json et application/pdf",
					HttpStatus.NOT_ACCEPTABLE);
		}

		//Mise à jour des données PSC: accessToken, reponse introspection, userInfo
		ParametresSign params = prepareAppelEsignWSPSCDatas (xUserinfo, xIntrospectionResponse );
					
		ESignSanteSignatureReportWithProof report = executeAskSignature(TYPE_SIGNATURE.PADES,  file, params);

		String signedStringBase64 = report.getDocSigne();
		byte[] signedDoc = null;

		try {
			signedDoc = Helper.decodeBase64toByteArray(signedStringBase64);
		} catch (UnsupportedEncodingException | IllegalArgumentException e) {
			log.debug(e.toString());
			throwExceptionRequestError(e, "Exception Serveur (décodage base64 du fichier signé PADES)",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Resource resource = new FileResource(signedDoc, "SIGNED_" + file.getOriginalFilename());

		var httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_PDF);
		return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);

	}

/**
  	* postAsksignatureXades
    * @param file  (required)
     * @param userInfo userInfo Prosante Connect (json encodé base 64) (optional)
     * @param tokenValidationResponse response PSC sur introspection (json encodé base 64) (optional)
  
 */
	@Override
	 public ResponseEntity<String> postAsksignatureXades(MultipartFile file,
		        String xUserinfo,
		        String xIntrospectionResponse) {
		log.debug("Réception d'une demande de signature Xades");
		log.debug("withGravitee: " + withGravitee);
        
		List<String> acceptedHeaders = getAcceptHeaders();
		if (!isAcceptHeaderPresent(acceptedHeaders, APPLICATION_JSON)) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		if (!isAcceptHeaderPresent(acceptedHeaders, APPLICATION_XML)) {
			throwExceptionRequestError("Le header 'accept' doit contenir  application/json et application/xml",
					HttpStatus.NOT_ACCEPTABLE);
		}
		
		//lecture des données PSC: accessToken, reponse introspection, userInfo
		ParametresSign params = prepareAppelEsignWSPSCDatas(xUserinfo, xIntrospectionResponse);

		
		ESignSanteSignatureReportWithProof report = executeAskSignature(TYPE_SIGNATURE.XADES, file, params);

		var signedStringBase64 = report.getDocSigne();

		String signedString = null;
		try {
			signedString = Helper.decodeBase64toString(signedStringBase64);
		} catch (UnsupportedEncodingException | IllegalArgumentException e) {
			log.debug(e.toString());
			throwExceptionRequestError(e, "Exception Serveur (décodage base64 du fichier signé XADES)",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

		var httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<>(signedString, httpHeaders, HttpStatus.OK);

	}

	        
	@Override
	public ResponseEntity<org.springframework.core.io.Resource> postAskSignatureFse(byte[] hashFSE,
			String idFacturationPS,
	        String xUserinfo,
	        String xIntrospectionResponse,
	        String typeFlux) {

				log.debug("postAskSignatureFse");
				List<String> acceptedHeaders = getAcceptHeaders();
				if (!isAcceptHeaderPresent(acceptedHeaders, APPLICATION_JSON)) {
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				}
				if (!isAcceptHeaderPresent(acceptedHeaders, APPLICATION_OCTET_STREAM)) {
					throwExceptionRequestError("Le header 'accept' doit contenir  application/json et application/octet_stream",
							HttpStatus.NOT_ACCEPTABLE);
				}
				
				getHttpRequest();
				
				//parametre optionnel type de flux
				if ((typeFlux==null) || (typeFlux.isEmpty()) ) {
					typeFlux = Helper.TYPE_FLUX_DEFAULT;
					log.debug("Paramètre 'typeDeFLux' non fourni. On prend la valeur par défaut 'T'");
				}
				
				//Mise à jour des données PSC: accessToken, reponse introspection, userInfo
				ParametresSign params = prepareAppelEsignWSPSCDatas (xUserinfo, xIntrospectionResponse );				
				
				// construction du OpenIDToken
				var user = params.getUserinfo();
				
				//Preuves données PSC
				List<OpenidToken> openidTokens = setOpenIdTokens(params);
				
				TYPE_SIGNATURE typeSignature = TYPE_SIGNATURE.FSE;
				
				// liste des signataires => non utilisé dans le cas d'une FSE (signature non 'ADES)
				List<String> signers = new ArrayList<String>();
				signers.add("Not used");
						
				// Appel esignsante
				log.debug("Appel esignWS pour une signature de type {}", typeSignature.getTypeSignature());
				ESignSanteSignatureReportWithProof report = null;

				try {
					report = esignWS.signatureFSE(hashFSE, idFacturationPS, typeFlux, signers,
							params.getRequestID(), openidTokens);

				} catch (RestClientException e) {
					throwExceptionRequestError(e, "Exception sur appel esignWS. Service inaccessible",
							HttpStatus.SERVICE_UNAVAILABLE);
				}
							
				archivagePreuveSiOK(report, params.getRequestID(), user, params.getDate());
				
				byte[] signatureValue = report.getDocSigne().getBytes();
				Resource resource = new FileResource(signatureValue, "BINARY_SIGNATURE_VALUE" );
		        return new ResponseEntity<>(resource,HttpStatus.OK);

		    }
	
	private void archivagePreuveSiOK(ESignSanteSignatureReportWithProof report, String requestID, UserInfo userToPersit,
			Date now) {
		
		if (report == null) {
			throwExceptionRequestError("Exception technique sur appel esignWS", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (!report.isValide()) {
			StringBuilder err = new StringBuilder();
			for (Erreur erreur : report.getErreurs()) {
				err.append(erreur.getCodeErreur()); 
				err.append(": ");
				err.append(erreur.getMessage());
				err.append(";");
			}
			throwExceptionRequestError("La signature n'est pas valide. Erreur(s): " + err.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		checkExistMetadataInUserInfo(userToPersit);
		
		log.debug(" START archivage de la preuve en BDD pour request id: {}", requestID);
		ProofStorage proof = new ProofStorage(requestID, userToPersit.getSubjectOrganization(),
				userToPersit.getPreferredUsername(), userToPersit.getGivenName(), userToPersit.getFamilyName(), now,
				report.getPreuve());
		try {
			dao.archivePreuve(proof);
		} catch (Exception e) {
			log.debug(e.toString());
			throwExceptionRequestError(e,
					"Requête abandonnée pour problème d'archivage de la preuve en base de données. ",
					HttpStatus.SERVICE_UNAVAILABLE);
		}

		log.debug("Fin archivage de la preuve en BDD pour request id: {}", requestID);
	}

	private UserInfo decodeBase64UserInfoJSON(String jsonUserInfoBase64) {
		UserInfo userInfo = null;
		try {
			userInfo = Helper.jsonBase64StringToUserInfo(jsonUserInfoBase64);
		} catch (JsonProcessingException e) {
			throwExceptionRequestError(e, "Exception en parsant le userInfo reçu: " + jsonUserInfoBase64,
					HttpStatus.BAD_REQUEST);
		} catch (UnsupportedEncodingException | IllegalArgumentException e) {
			throwExceptionRequestError(e,
					"Le userinfo fourni ne semble pas codé en base 64. UserInfo reçu : " + jsonUserInfoBase64,
					HttpStatus.BAD_REQUEST);
		}
		return userInfo;
	}

		private ESignSanteSignatureReportWithProof executeAskSignature(TYPE_SIGNATURE typeSignature, MultipartFile file, ParametresSign params) {
			
		checkNotEmptyInputParameters(params.getAccessToken(), file, params.getJsonUserInfo(), params.getJsonPscReponse());
		
		java.io.File fileToSign = getMultiPartFile(file);	
			
		// verification du type de fichier
		String typeFile = checkTypeFile(fileToSign);
		// contrôle uniquement des PDFs
		if ((typeSignature.equals(TYPE_SIGNATURE.PADES)) && (!typeFile.equals(APPLICATION_PDF))) {
				throwExceptionRequestError(
						"Requête rejetée car le fichier transmis semble ne pas être un PDF, type détecté  " + typeFile,
						HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			}
		
		// construction du OpenIDToken
		var user = params.getUserinfo();		
		List<OpenidToken> openidTokens = setOpenIdTokens(params);

		// liste des signataires
		List<String> signers = setSigners(typeSignature, user);

		// Appel esignsante
		log.debug("Appel esignWS pour une signature de type {}", typeSignature.getTypeSignature());
		ESignSanteSignatureReportWithProof report = null;
		try {
			if ((TYPE_SIGNATURE.PADES.toString()).equals(typeSignature.getTypeSignature())) {
				report = esignWS.signaturePades(fileToSign, signers, params.getRequestID(), openidTokens);
			} else {
				report = esignWS.signatureXades(fileToSign, signers, params.getRequestID(), openidTokens);
			}

		} catch (RestClientException e) {
			throwExceptionRequestError(e, "Exception sur appel esignWS. Service inaccessible",
					HttpStatus.SERVICE_UNAVAILABLE);
		}

		archivagePreuveSiOK(report, params.getRequestID(), user, params.getDate());

		return report;
	}

	private List<String> setSigners(TYPE_SIGNATURE typeSignature, UserInfo userinfo) {
		List<String> retour = new ArrayList<>();
		String signer = SIGNER_XADES;

		if (typeSignature.getTypeSignature().equals(TYPE_SIGNATURE.PADES.toString())) {
			signer = SIGNER_PADES;
		}
		signer = signer.concat(userinfo.getFamilyName()).concat(" (").concat(userinfo.getSubjectNameID()).concat(")");
		log.error("signer: " + signer);
		retour.add(signer);
		return retour;
	}

	public void checkNotEmptyInputParameters(String accessToken, MultipartFile file, String userinfo, String tokenValidationResponse) {
		if ((accessToken == null) || (file == null) || (userinfo == null) || (tokenValidationResponse == null)) {
			throwExceptionRequestError(
					"Au moins un paramètre indispensable parmi {'file', 'userinfo', 'accessToken', 'tokenValidationResponse'} est nul ou non fourni",
					HttpStatus.BAD_REQUEST);
		}

		if ((accessToken.isEmpty()) || (accessToken.isBlank()) || (file.getSize() <= 0) || (userinfo.isEmpty())
				|| (userinfo.isBlank()) || (tokenValidationResponse.isEmpty())	|| (tokenValidationResponse.isBlank()) ) {
			throwExceptionRequestError("Au moins un paramètre parmi {'file', 'userinfo', 'accessToken', 'tokenValidationResponse'} est vide",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	private ParametresSign prepareAppelEsignWSPSCDatas (final String xUserInfo, final String xIntrospectionResponse ) {
		
		ParametresSign params = new ParametresSign();
		String decodedIntro = null;
		String decodedUserInfo = null;
		
		//L' accesstoken doit être dans les headers . Exception si pb
		String accessToken = getAccessToken();
		params.setAccessToken(accessToken);
						
		
		if (withGravitee.equals("true")) { //Fonctionnement avec gravitee: les infos doivent être dans les headers 
			log.debug("getting introspection result and userinfo from header's request...");
			log.debug("...response introspection PSC extrait des headers: {} ", xIntrospectionResponse);
			checkPSCHeaderNotEmpty (xUserInfo,  xIntrospectionResponse);
			try {	
				decodedIntro = Helper.decodeBase64toString(xIntrospectionResponse);
				log.debug("...... response PSC decodée : {} ", decodedIntro);
				log.debug("...userInfo extrait des headers: {}" , xUserInfo);			
				decodedUserInfo = Helper.decodeBase64toString(xUserInfo);
				log.debug("...... userInfo décodé: {} ", decodedUserInfo);	
			} catch (UnsupportedEncodingException | IllegalArgumentException e) {				
				e.printStackTrace();
				throwExceptionRequestError("Erreur technique sur décodage des données PSC (réponse introspection ou UserInfo)", HttpStatus.INTERNAL_SERVER_ERROR);
			}	
				
		}
		else { //Fonctionnement sans gravitee => appel à PSC pour vérifier le token et récupérer le userInfo
				
			log.debug("calling PSC to check validity of access token and get userInfo ...");
			decodedIntro = pscApi.getIntrospectionResult(accessToken);
			log.debug("responsePSC CALL PSC: {}", decodedIntro);
			//HttpStatus tmpStatus = Helper.parsePSCresponse(decodedIntro);
			Helper.parsePSCresponse(decodedIntro);
			decodedUserInfo = pscApi.getUserInfo(accessToken);
			log.debug("userInfo CALL PSC: {}" ,decodedUserInfo);
		}
		
		//mise à jour de la structure 
		params.setJsonPscReponse(decodedIntro);
		params.setJsonUserInfo(decodedUserInfo);
		try {
			params.setUserinfo(Helper.jsonStringToUserInfo(decodedUserInfo));
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			throwExceptionRequestError("Erreur technique lors du parse du UserInfo", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		params.setDate(new Date());
		// génération d'un UUDI
		params.setRequestID(Helper.generateRequestId());

		return params;
	}
	
	public 	List<OpenidToken> setOpenIdTokens(ParametresSign params) {
	List<OpenidToken> openidTokens = new ArrayList<>();
	var openidToken = new OpenidToken();
	openidToken.setAccessToken(params.getAccessToken());
	openidToken.setIntrospectionResponse(params.getJsonPscReponse());
	openidToken.setUserInfo(params.getJsonUserInfo());
	
	openidTokens.add(openidToken);
	return openidTokens;
	}
	
	
	public void checkPSCHeaderNotEmpty (final String xUserInfo, final String xIntrospectionResponse) {
		if ((xUserInfo == null) || (xUserInfo.isEmpty()) || (xUserInfo.isBlank()) ){
			throwExceptionRequestError("Erreur technique sur lecture du  header X-Userinfo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if ((xIntrospectionResponse == null) || (xIntrospectionResponse.isEmpty()) || (xIntrospectionResponse.isBlank()) ){
			throwExceptionRequestError("Erreur technique sur lecture du  header X-Introspection-Response", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//vérifie que les champs à persister existen dans le UserInfo (conséquence de l'annotation JsonIgnoreProperties)
	public void checkExistMetadataInUserInfo(UserInfo userToPersit) {
		if (!checkNotEmptyData(userToPersit.getFamilyName())) {
			throwExceptionRequestError("Le champ 'FamilyName' du USerInfo est vide ou n'existe pas", HttpStatus.BAD_REQUEST);
		}
		if (!checkNotEmptyData(userToPersit.getGivenName())) {
			throwExceptionRequestError("Le champ 'GivenName' du USerInfo est vide ou n'existe pas", HttpStatus.BAD_REQUEST);
		}
		if (!checkNotEmptyData(userToPersit.getPreferredUsername())) {
			throwExceptionRequestError("Le champ 'PreferredUsername' du USerInfo est vide ou n'existe pas", HttpStatus.BAD_REQUEST);
		}
		if (!checkNotEmptyData(userToPersit.getSubjectOrganization())) {
			throwExceptionRequestError("Le champ 'SubjectOrganization' du USerInfo est vide ou n'existe pas", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public  Boolean checkNotEmptyData(String userInfoField) {	
		Boolean response = true;
		if ((userInfoField == null) || (userInfoField.isEmpty()) || (userInfoField.isBlank()) ){
			response = false;
		}
       return response;
	}
}
