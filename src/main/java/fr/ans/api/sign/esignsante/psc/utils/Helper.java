/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.esignsante.model.Erreur;
import  lombok.extern.slf4j.Slf4j;
import static fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException.throwExceptionRequestError;


@Slf4j
public class Helper {

	// champs du UserInfo
	public static final  String GIVEN_NAME = "given_name";
	public static final String PREFERRED_USERNAME = "preferred_username";
	public static final String SUBJECT_ORGANIZATION = "SubjectOrganization";
	public static final String FAMILY_NAME = "family_name";

	// champs reponse intropestion PSC
	public static final String TOKEN_ACTIVE_FIELD = "active";
	public static final String TOKEN_ACTIVE_FALSE = "false";
	public static final String TOKEN_ACTIVE_TRUE = "true";

	
	public static final String TYPE_FLUX_DEFAULT = "T";

	private Helper() {
	}
	/*
	 * génération d'un identifiant 'metier' unique transmis sur appel à esignsante
	 * pour une signature avec preuve et persisté
	 */
	public static String generateRequestId() {
		final String requestID = UUID.randomUUID().toString();
		return requestID;
	}

	public static String encodeBase64(String stringToEncode) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(stringToEncode.getBytes("UTF-8"));
	}

	public static String decodeBase64toString(String stringToDecode) throws UnsupportedEncodingException, IllegalArgumentException{
		byte[] decodedBytes = Base64.getDecoder().decode(stringToDecode);
		return new String(decodedBytes, "UTF-8");
	}


	
	public static byte[] decodeBase64toByteArray(String stringToDecode) throws UnsupportedEncodingException, IllegalArgumentException {
		return Base64.getDecoder().decode(stringToDecode);
	}

	public static List<Error> mapListErreurToListError(List<Erreur> erreurs) {

		List<Error> errors = erreurs.stream().map(erreur -> {
			Error er = new Error();
			er.setCode(erreur.getCodeErreur());
			er.setMessage(erreur.getMessage());
			return er;

		}).collect(Collectors.toList());

		return errors;
	}

	public static UserInfo jsonBase64StringToUserInfo(String sUserInfoEncodedBase64)
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		return new ObjectMapper().readValue(Helper.decodeBase64toString(sUserInfoEncodedBase64), UserInfo.class);
	}
	
	public static UserInfo jsonStringToUserInfo(String sUserInfo)
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		return new ObjectMapper().readValue(sUserInfo, UserInfo.class);
	}

//	public static HttpStatus parsePSCresponse(String reponsePSC) {
//		HttpStatus retour = HttpStatus.INTERNAL_SERVER_ERROR;
//		log.debug("parsePSCresponse IN with {}", reponsePSC);
//
//		ObjectNode node;
//		try {
//			node = new ObjectMapper().readValue(reponsePSC, ObjectNode.class);
//
//			if (node.has(TOKEN_ACTIVE_FIELD)) {
//				String token_active_field = node.get(TOKEN_ACTIVE_FIELD).asText();
//				if (token_active_field.equalsIgnoreCase(TOKEN_ACTIVE_TRUE)) {
//					retour = HttpStatus.OK;
//				} else {
//					retour = HttpStatus.UNAUTHORIZED;
//				}
//			} else {
//				retour = HttpStatus.INTERNAL_SERVER_ERROR;
//				log.error("Reponse invalide introspection PSC: champ {} non trouvé", Helper.TOKEN_ACTIVE_FIELD);
//			}
//
//		} catch (Exception e) {
//			log.error("Erreur technique durant le parse de la reponse d'intropection PSC: champ {} ", e.getMessage());
//			log.debug(e.toString());
//			retour = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//
//		log.debug("parsePSCresponse OUT status =  {}", retour);
//		return retour;
//	}
	public static void parsePSCresponse(String reponsePSC) {
		log.debug("parsePSCresponse IN with {}", reponsePSC);
		HttpStatus status = HttpStatus.OK;
		String msg = "";
		ObjectNode node;
		try {
			node = new ObjectMapper().readValue(reponsePSC, ObjectNode.class);

			if (node.has(TOKEN_ACTIVE_FIELD)) {
				String token_active_field = node.get(TOKEN_ACTIVE_FIELD).asText();
				if (token_active_field.equalsIgnoreCase(TOKEN_ACTIVE_TRUE)) {
				} else {
					msg = "Le token fourni n'est pas reconnu comme un token valide";
					status = HttpStatus.UNAUTHORIZED;
					//throwExceptionRequestError("Le token fourni n'est pas reconnu comme un token valide", HttpStatus.UNAUTHORIZED);					
				}
			} else {
				log.error("Reponse invalide introspection PSC: champ {} non trouvé", Helper.TOKEN_ACTIVE_FIELD);
				msg = "Erreur lors du parse de la réponse d'introspection de PSC";
				status =  HttpStatus.INTERNAL_SERVER_ERROR;
				//throwExceptionRequestError("Erreur lors du parse de la réponse d'introspection de PSCide", HttpStatus.INTERNAL_SERVER_ERROR);				
			}

		} catch (Exception e) {
			log.error("Erreur technique durant le parse de la reponse d'intropection PSC: champ {} ", e.getMessage());
			log.debug(e.toString());
			throwExceptionRequestError("Erreur lors du parse de la réponse d'introspection de PSCide", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (status.compareTo(HttpStatus.OK)!=0)  {
			throwExceptionRequestError(msg,status);
		}
	}
}
