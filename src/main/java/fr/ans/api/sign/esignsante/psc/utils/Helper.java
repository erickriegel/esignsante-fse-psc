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

	public final static String GIVEN_NAME = "given_name";
	public final static String PREFERRED_USERNAME= "preferred_username";
	public final static String SUBJECT_ORGANIZATION = "SubjectOrganization";
	public final static String FAMILY_NAME = "family_name";
	
	private Helper() {
	}

	/*
	 * Génération d'un objet ProofStorage avec les données extraites du UserInfo et
	 * génération d'un requestID
	 */
	public static ProofStorage convertUserInfoToProofStorage(UserInfo userinfo) {
		// ICI à corriger !!!!!!!!!!!!!
//		Date now = new Date();
//		ProofStorage proof = new ProofStorage(Helper.generateRequestId(), userinfo.getSubjectOrganization(),
//				userinfo.getPreferredUsername(), userinfo.getGivenName(), userinfo.getFamilyName(), now);
//		return proof;
		return null;
	}

	public static ProofStorage convertUserInfoToProofStorage(UserInfo userinfo, org.bson.Document bsonProof) {
		// ICI à correiger !!!!!!!!!!!!
//		Date now = new Date();
//		ProofStorage proof = new ProofStorage(Helper.generateRequestId(), userinfo.getSubjectOrganization(),
//				userinfo.getPreferredUsername(), userinfo.getGivenName(), userinfo.getFamilyName(), now, bsonProof);
//		return proof;
		return null;
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
	
	public static UserInfo jsonStringToUserInfo(String sUserInfo) throws JsonMappingException, JsonProcessingException {
		return new ObjectMapper().readValue(sUserInfo, UserInfo.class);  	
	}

	public static Map<String,String> jsonStringToPartialMap(String sUserInfo) throws JsonMappingException, JsonProcessingException {
		final ObjectNode node = new ObjectMapper().readValue(sUserInfo, ObjectNode.class);
		
		
		Map<String, String> data = new HashMap<String, String>();
		if (node.has(GIVEN_NAME)) {   
			data.put(GIVEN_NAME, node.get(GIVEN_NAME).asText());
		}

		if (node.has(PREFERRED_USERNAME)) {
			data.put(PREFERRED_USERNAME, node.get(PREFERRED_USERNAME).asText());
		}

		if (node.has(FAMILY_NAME)) {   
			data.put(FAMILY_NAME, node.get(FAMILY_NAME).asText());
		}

		if (node.has(SUBJECT_ORGANIZATION)) {  
			data.put(SUBJECT_ORGANIZATION, node.get(SUBJECT_ORGANIZATION).asText());
		}
		return data;  	
	}

}
