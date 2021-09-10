package fr.ans.api.sign.esignsante.psc.api.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;
import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;

@Slf4j
@SpringBootTest // (properties = "file.encoding=UTF-8")
public class HelperTest {

	final public String STRING_TO_ENCODE =new String("Chaîne de caractère UTF8 &é\"(,;!");
	final public String STRING_TO_DECODE ="Q2hhw65uZSBkZSBjYXJhY3TDqHJlIFVURjggJsOpIigsOyE=";
	
	@Test
	public void encodeBase64() {
	   String test;
	try {
		test = Helper.encodeBase64(STRING_TO_ENCODE);
		 log.debug("encodebase64");
		   log.debug(test);
		   assertEquals(test.compareTo(STRING_TO_DECODE),0);
	} catch (UnsupportedEncodingException e) {
		assertTrue(false,"UnsupportedEncodingException durant le codage...");
		e.printStackTrace();
	}
	  
	}

	/*
	@Test
	public void decodeBase64()  {
	   String test;
	try {
		test = Helper.decodeBase64(STRING_TO_DECODE);
		log.debug("decodebase64");
		   log.debug(test);
		   assertEquals(test.compareTo(STRING_TO_ENCODE),0);
	} catch (UnsupportedEncodingException e) {
		assertTrue(false,"UnsupportedEncodingException durant le decodage...");
		e.printStackTrace();
	}
	   
	}
	*/
	@Test
	public void converterErrorErreurTest() {
		log.debug("test conversion Erreur(esignsante) en Error (esigsante-psc");
		final String codeErreur1 = "codeErreur1";
		final String codeErreur2 = "codeErreur2";
		final String message1 = "message1";
		final String message2 = "message2";
		List<Erreur> erreurs = new ArrayList<Erreur>();
		Erreur erreur1= new Erreur();
		erreur1.setCodeErreur(codeErreur1);
		erreur1.setMessage(message1);
		Erreur erreur2= new Erreur();
		erreur2.setCodeErreur(codeErreur2);
		erreur2.setMessage(message2);
		erreurs.add(erreur1);
		erreurs.add(erreur2);
		
		Error error1= new Error();
		error1.setCode(codeErreur1);
		error1.setMessage(message1);
		Error error2= new Error();
		error2.setCode(codeErreur2);
		error2.setMessage(message2);
		
		List<Error> errors = Helper.mapListErreurToListError(erreurs);
		assertTrue(errors.size() == erreurs.size());
		
		errors.stream()
        .forEach(error -> {
        	Erreur er= new Erreur();
        	er.setMessage(error.getMessage());
        	er.setCodeErreur(error.getCode());
        	 	assertTrue(erreurs.contains(er));
        });
		

        errors.stream()
.forEach(error -> log.debug("code :" + error.getCode() + " msg :" + error.getMessage()));
	}
	
//	@Test
//	public void jsonToUserInfo() throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
//		String userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
//		UserInfo obUserInfo = Helper.jsonStringToUserInfo(userinfo);
//		assertEquals(obUserInfo.getFamilyName(), "SPECIALISTE0021889");
//		assertEquals(obUserInfo.getGivenName(), "ROBERT");
//		assertEquals(obUserInfo.getPreferredUsername(), "899700218896");
//		assertEquals(obUserInfo.getSubjectOrganization(), "CABINET M SPECIALISTE0021889");
//	}
	
	@Test
	public void JsonStringToMap() throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		String userinfoBase64 = Helper.encodeBase64("{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}");
		Map data = Helper.jsonStringBase64ToPartialMap(userinfoBase64);
		assertEquals(data.get(Helper.FAMILY_NAME), "SPECIALISTE0021889");
		assertEquals(data.get(Helper.GIVEN_NAME), "ROBERT");
		assertEquals(data.get(Helper.PREFERRED_USERNAME), "899700218896");
		assertEquals(data.get(Helper.SUBJECT_ORGANIZATION), "CABINET M SPECIALISTE0021889");
	}
	
	@Test
	public void JsonStringNotBase64ToMap() {
		String userinfoBase64 = null;	
		userinfoBase64 = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		
		Map data = null;
		try {
			data = Helper.jsonStringBase64ToPartialMap(userinfoBase64);
			assertFalse(true);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (UnsupportedEncodingException | IllegalArgumentException e) {
			// si ICI c'est OK
			e.printStackTrace();
		}
	}
	
	//@Test
//	public void StringBase64JSONNotUserINfoToMap() {
//		String userinfoBase64 = null;
//		try {
//			userinfoBase64 = Helper.encodeBase64("{\"Secteur_ActiviteXXX\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}");
//		} catch (UnsupportedEncodingException e) {
//			log.error("bug dans un test...");
//			e.printStackTrace();
//		}
//		
//		Map data = null;
//		try {
//			data = Helper.jsonStringBase64ToPartialMap(userinfoBase64);
//			assertFalse(true);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			assertFalse(true);
//	
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			assertFalse(true);
//		}
//	} catch (?????gException e) {
//		e.printStackTrace();
//		//ICI OK
//		//TODO Modifier la methode => exception si tous les champs ne sont pas trouvés
//		
//	}
//	}
	
	@Test
	public void StringBase64NotJSONToMap() {
		String userinfoBase64 = null;
		try {
			userinfoBase64 = Helper.encodeBase64("Ceci n est pas un JSON");
		} catch (UnsupportedEncodingException e) {
			log.error("bug dans un test...");
			e.printStackTrace();
		}
		
		Map data = null;
		try {
			data = Helper.jsonStringBase64ToPartialMap(userinfoBase64);
			assertFalse(true);
		} catch (JsonMappingException e) {			
			e.printStackTrace();
			assertFalse(true);			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			//ICI c'est OK
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

}
