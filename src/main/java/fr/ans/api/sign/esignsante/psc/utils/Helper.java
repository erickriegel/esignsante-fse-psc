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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {

	// champs du UserInfo
	public final static String GIVEN_NAME = "given_name";
	public final static String PREFERRED_USERNAME = "preferred_username";
	public final static String SUBJECT_ORGANIZATION = "SubjectOrganization";
	public final static String FAMILY_NAME = "family_name";

	// champs reponse intropestion PSC
	public final static String TOKEN_ACTIVE_FIELD = "active";
	public final static String TOKEN_ACTIVE_FALSE = "false";
	public final static String TOKEN_ACTIVE_TRUE = "true";

	// type de Header
	public final static String APPLICATION_JSON = "application/json";
	public final static String APPLICATION_XML = "application/xml";
	public final static String APPLICATION_PDF = "application/pdf";

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

	public static String decodeBase64toString(String stringToDecode) throws UnsupportedEncodingException {
		byte[] decodedBytes = Base64.getDecoder().decode(stringToDecode);
		return new String(decodedBytes, "UTF-8");
	}

	public static byte[] decodeBase64toByteArray(String stringToDecode) throws UnsupportedEncodingException {
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

	public static HttpStatus parsePSCresponse(String reponsePSC) {
		HttpStatus retour = HttpStatus.INTERNAL_SERVER_ERROR;
		log.debug("parsePSCresponse IN with {}", reponsePSC);

		ObjectNode node;
		try {
			node = new ObjectMapper().readValue(reponsePSC, ObjectNode.class);

			if (node.has(TOKEN_ACTIVE_FIELD)) {
				String token_active_field = node.get(TOKEN_ACTIVE_FIELD).asText();
				if (token_active_field.equalsIgnoreCase(TOKEN_ACTIVE_TRUE)) {
					retour = HttpStatus.OK;
				} else {
					retour = HttpStatus.BAD_REQUEST;
				}
			} else {
				retour = HttpStatus.INTERNAL_SERVER_ERROR;
				log.error("Reponse invalide introspection PSC: champ {} non trouvé", Helper.TOKEN_ACTIVE_FIELD);
			}

		} catch (Exception e) {
			log.error("Erreur technique durant le parse de la reponse d'intropection PSC: champ {} ", e.getMessage());
			e.printStackTrace();
			retour = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		log.debug("parsePSCresponse OUT status =  {}", retour);
		return retour;
	}
}
