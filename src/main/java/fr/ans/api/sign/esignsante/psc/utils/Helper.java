package fr.ans.api.sign.esignsante.psc.utils;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//import org.bson.BsonDocument;
import org.bson.Document;

import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {

	private Helper() {
	}

	/*
	 * Génération d'un objet ProofStorage avec les données extraites du UserInfo et
	 * génération d'un requestID
	 */
	public static ProofStorage convertUserInfoToProofStorage(UserInfo userinfo) {
		Date now = new Date();
		ProofStorage proof = new ProofStorage(Helper.generateRequestId(), userinfo.getSubjectOrganization(),
				userinfo.getPreferredUsername(), userinfo.getGivenName(), userinfo.getFamilyName(), now);
		return proof;
	}

	public static ProofStorage convertUserInfoToProofStorage(UserInfo userinfo, org.bson.Document bsonProof) {
		Date now = new Date();
		ProofStorage proof = new ProofStorage(Helper.generateRequestId(), userinfo.getSubjectOrganization(),
				userinfo.getPreferredUsername(), userinfo.getGivenName(), userinfo.getFamilyName(), now, bsonProof);
		return proof;
	}

	/*
	 * génération d'un identifiant 'metier' unique transmis sur appel à esignsante
	 * pour une signature avec preuve et persisté
	 */
	public static String generateRequestId() {
		final String requestID = UUID.randomUUID().toString();
		return requestID;
	}

	public static String encodeBase64(String stringToEncode) {
		return Base64.getEncoder().encodeToString(stringToEncode.getBytes());
	}

	public static String decodeBase64(String stringToDecode) {
		byte[] decodedBytes = Base64.getDecoder().decode(stringToDecode);
		return new String(decodedBytes);

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

}
