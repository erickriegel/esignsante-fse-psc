package fr.ans.api.sign.esignsante.psc.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;


import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {

	// champs du UserInfo
	public final static String GIVEN_NAME = "given_name";
	public final static String PREFERRED_USERNAME= "preferred_username";
	public final static String SUBJECT_ORGANIZATION = "SubjectOrganization";
	public final static String FAMILY_NAME = "family_name";
	
	// champs reponse intropestion PSC
	public final static String TOKEN_ACTIVE_FIELD = "active";
	public final static String TOKEN_ACTIVE_FALSE = "false";
	public final static String TOKEN_ACTIVE_TRUE = "true";
	
	//type de Header
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
		return Base64.getEncoder().encodeToString(stringToEncode.getBytes("UTF-8") );
	}

	public static String decodeBase64(String stringToDecode) throws UnsupportedEncodingException {
		byte[] decodedBytes = Base64.getDecoder().decode(stringToDecode);
		//return new String(decodedBytes);
			return new String(decodedBytes, "UTF-8");
	}

	public static List<Error> mapListErreurToListError(List<Erreur> erreurs) {

		// List<Error> errors = new ArrayList<Error>();

		List<Error> errors = erreurs.stream().map(erreur -> {
			Error er = new Error();
			er.setCode(erreur.getCodeErreur());
			er.setMessage(erreur.getMessage());
			return er;

		}).collect(Collectors.toList());

		return errors;
	}
	
	public static UserInfo jsonBase64StringToUserInfo(String sUserInfoEncodedBase64) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException  {
		return new ObjectMapper().readValue(Helper.decodeBase64(sUserInfoEncodedBase64), UserInfo.class);  	
	}

	public static Map<String,String> jsonStringBase64ToPartialMap(String sUserInfoBase64Json) throws JsonProcessingException, UnsupportedEncodingException, IllegalArgumentException {
		final ObjectNode node = new ObjectMapper().readValue(Helper.decodeBase64(sUserInfoBase64Json), ObjectNode.class);
		Boolean allFieldsExist = true;
		log.debug("jsonStringToPartialMap IN");
		Map<String, String> data = new HashMap<String, String>();
		if (node.has(GIVEN_NAME)) {   
			data.put(GIVEN_NAME, node.get(GIVEN_NAME).asText());
		}
		else {
			allFieldsExist = false;
			log.error("Champ: {} non trouvé dans le UserInfo transmis", GIVEN_NAME);
		}
			

		if (node.has(PREFERRED_USERNAME)) {
			data.put(PREFERRED_USERNAME, node.get(PREFERRED_USERNAME).asText());
		}
		else {
			allFieldsExist = false;
			log.error("Champ: {} non trouvé dans le UserInfo transmis", PREFERRED_USERNAME);
		}

		if (node.has(FAMILY_NAME)) {   
			data.put(FAMILY_NAME, node.get(FAMILY_NAME).asText());
		}
		else {
			allFieldsExist = false;
			log.error("Champ: {} non trouvé dans le UserInfo transmis", FAMILY_NAME);
		}

		if (node.has(SUBJECT_ORGANIZATION)) {  
			data.put(SUBJECT_ORGANIZATION, node.get(SUBJECT_ORGANIZATION).asText());
		}
		else {
			allFieldsExist = false;
			log.error("Champ: {} non trouvé dans le UserInfo transmis", SUBJECT_ORGANIZATION);
		}
		
//		if (allFieldsExist == false) 
//		{
//			//throw new JsonMappingException("IL existe au moins un champ non trouvé dans le USerInfo transmis");
//			throw new XXXException();
//		}
//		
		log.debug("jsonStringToPartialMap OUT: parse UserInfo OK");
		return data;  	
	}

	public static HttpStatus parsePSCresponse(String reponsePSC) {
		HttpStatus retour = HttpStatus.INTERNAL_SERVER_ERROR;
		log.debug("parsePSCresponse IN with {}" , reponsePSC);

		ObjectNode node;
		try {
			node = new ObjectMapper().readValue(reponsePSC, ObjectNode.class);

			if (node.has(TOKEN_ACTIVE_FIELD)) {   
                 String token_active_field = node.get(TOKEN_ACTIVE_FIELD).asText();
				if (token_active_field.equalsIgnoreCase(TOKEN_ACTIVE_TRUE)) {
					retour = HttpStatus.OK;
				}
				else 
                {
					retour = HttpStatus.BAD_REQUEST;	
				}
			}
			else { 
				retour = HttpStatus.INTERNAL_SERVER_ERROR;
				log.error("Reponse invalide introspection PSC: champ {} non trouvé", Helper.TOKEN_ACTIVE_FIELD);
			}

		} catch (Exception e) {
			log.error("Erreur technique durant le parse de la reponse d'intropection PSC: champ {} ", e.getMessage());
			e.printStackTrace();
			retour = HttpStatus.INTERNAL_SERVER_ERROR;
		} 
		
		log.debug("parsePSCresponse OUT status =  {}" , retour);
		return retour;
	}
}
