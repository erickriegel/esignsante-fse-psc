/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest // (properties = "file.encoding=UTF-8")
public class HelperTest {

	final public String STRING_TO_ENCODE = new String("Chaîne de caractère UTF8 &é\"(,;!");
	final public String STRING_TO_DECODE = "Q2hhw65uZSBkZSBjYXJhY3TDqHJlIFVURjggJsOpIigsOyE=";
	final public byte[] BYTE = { 5, 127, 12, -2, -127 };

	@Test
	public void encodeBase64() {
		String test;
		try {
			test = Helper.encodeBase64(STRING_TO_ENCODE);
			log.debug("encodebase64");
			log.debug(test);
			assertEquals(0, test.compareTo(STRING_TO_DECODE));
		} catch (UnsupportedEncodingException e) {
			assertTrue(false, "UnsupportedEncodingException durant le codage...");
			e.printStackTrace();
		}
	}

	@Test
	public void decodeStringBase64() {
		String test;
		try {
			test = Helper.decodeBase64toString(STRING_TO_DECODE);
			log.debug("decodebase64");
			log.debug(test);
			assertEquals(0, test.compareTo(STRING_TO_ENCODE));
		} catch (UnsupportedEncodingException e) {
			assertTrue(false, "UnsupportedEncodingException durant le decodage...");
			e.printStackTrace();
		}

	}

	@Test
	public void decodeBytesBase64() throws IOException {
		String BYTE_ENCODED = Base64.getEncoder().encodeToString(BYTE);
		try {
			byte[] BYTE_DECODED = Helper.decodeBase64toByteArray(BYTE_ENCODED);
			assertTrue(Arrays.equals(BYTE, BYTE_DECODED));
		} catch (UnsupportedEncodingException e) {
			assertTrue(false, "UnsupportedEncodingException durant le decodage...");
			e.printStackTrace();
		}

	}

	@Test
	public void decodeStringNotEncodedBase64Test() {
		String test;
		try {
			test = "éàç_-a";
			Helper.decodeBase64toString(test);
			assertTrue(false);
		} catch ( IllegalArgumentException | UnsupportedEncodingException e) {

		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	

	@Test
	public void converterErrorErreurTest() {
		log.debug("test conversion Erreur(esignsante) en Error (esigsante-psc");
		final String codeErreur1 = "codeErreur1";
		final String codeErreur2 = "codeErreur2";
		final String message1 = "message1";
		final String message2 = "message2";
		List<Erreur> erreurs = new ArrayList<Erreur>();
		Erreur erreur1 = new Erreur();
		erreur1.setCodeErreur(codeErreur1);
		erreur1.setMessage(message1);
		Erreur erreur2 = new Erreur();
		erreur2.setCodeErreur(codeErreur2);
		erreur2.setMessage(message2);
		erreurs.add(erreur1);
		erreurs.add(erreur2);

		Error error1 = new Error();
		error1.setCode(codeErreur1);
		error1.setMessage(message1);
		Error error2 = new Error();
		error2.setCode(codeErreur2);
		error2.setMessage(message2);

		List<Error> errors = Helper.mapListErreurToListError(erreurs);
		assertEquals(errors.size(), erreurs.size());

		errors.stream().forEach(error -> {
			Erreur er = new Erreur();
			er.setMessage(error.getMessage());
			er.setCodeErreur(error.getCode());
			assertTrue(erreurs.contains(er));
		});

		errors.stream().forEach(error -> log.debug("code :" + error.getCode() + " msg :" + error.getMessage()));
	}

	@Test
	public void generateRequestIdTest() {
		String test = null;
		test = Helper.generateRequestId();
		assertNotNull(test);
		assertFalse(test.isEmpty());
		assertFalse(test.isBlank());
		assertTrue(test.length() > 30);
		assertNotEquals(0, test.compareTo(Helper.generateRequestId()));
	}

	@Test
	public void parsePSCResponseTest() throws IOException {

		Resource resource = new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json");
		String reponsePSCActif = Files.readString(resource.getFile().toPath());

		String reponsePSCNonActif = "{\"active\":false}";
		String reponsePSCKO = "Mauvaise réponse..";

		// OK
		try {
			Helper.parsePSCresponse(reponsePSCActif);
		} catch (Exception e) {
			assertTrue(false);
		}
		

		// Non actif
		try {
			Helper.parsePSCresponse(reponsePSCNonActif);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException);
		}
	
		// Erreur tech
		try {
			Helper.parsePSCresponse(reponsePSCKO);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException);
		}
	}

	@Test
	public void jsonBase64ToUserInfoTest()
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
				
        //userInfo sans champ authMode	
        String userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		String encodedUserinfo = Helper.encodeBase64(userinfo);
		fr.ans.api.sign.esignsante.psc.model.UserInfo obUserInfo = Helper.jsonBase64StringToUserInfo(encodedUserinfo);
		assertEquals("SPECIALISTE0021889",obUserInfo.getFamilyName());
		assertEquals("ROBERT", obUserInfo.getGivenName());
		assertEquals("899700218896", obUserInfo.getPreferredUsername());
		assertEquals("CABINET M SPECIALISTE0021889", obUserInfo.getSubjectOrganization());
		//userInfo avec champ authMode
		encodedUserinfo = "eyJTZWN0ZXVyX0FjdGl2aXRlIjoiU0EwN14xLjIuMjUwLjEuNzEuNC4yLjQiLCJzdWIiOiJmOjU1MGRjMWM4LWQ5N2ItNGIxZS1hYzhjLThlYjQ0NzFjZjlkZDo4OTk3MDAyNDU2NjciLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIlN1YmplY3RPcmdhbml6YXRpb24iOiJDQUJJTkVUIE0gRE9DMDAyNDU2NiIsIk1vZGVfQWNjZXNfUmFpc29uIjoiIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiODk5NzAwMjQ1NjY3IiwiZ2l2ZW5fbmFtZSI6IktJVCIsIkFjY2VzX1JlZ3VsYXRpb25fTWVkaWNhbGUiOiJGQVVYIiwiVUlUVmVyc2lvbiI6IjEuMCIsImF1dGhNb2RlIjoiQ0FSRCIsIlBhbGllcl9BdXRoZW50aWZpY2F0aW9uIjoiQVBQUFJJUDNeMS4yLjI1MC4xLjIxMy4xLjUuMS4xLjEiLCJTdWJqZWN0UmVmUHJvIjp7ImNvZGVDaXZpbGl0ZSI6Ik0iLCJleGVyY2ljZXMiOlt7ImNvZGVQcm9mZXNzaW9uIjoiMTAiLCJjb2RlQ2F0ZWdvcmllUHJvZmVzc2lvbm5lbGxlIjoiQyIsImNvZGVDaXZpbGl0ZURleGVyY2ljZSI6IkRSIiwibm9tRGV4ZXJjaWNlIjoiRE9DMDAyNDU2NiIsInByZW5vbURleGVyY2ljZSI6IktJVCIsImNvZGVUeXBlU2F2b2lyRmFpcmUiOiJTIiwiY29kZVNhdm9pckZhaXJlIjoiU00yNiIsImFjdGl2aXRpZXMiOlt7ImNvZGVNb2RlRXhlcmNpY2UiOiJMIiwiY29kZVNlY3RldXJEYWN0aXZpdGUiOiJTQTA3IiwiY29kZVNlY3Rpb25QaGFybWFjaWVuIjoiIiwiY29kZVJvbGUiOiIiLCJudW1lcm9TaXJldFNpdGUiOiIiLCJudW1lcm9TaXJlblNpdGUiOiIiLCJudW1lcm9GaW5lc3NTaXRlIjoiIiwibnVtZXJvRmluZXNzZXRhYmxpc3NlbWVudEp1cmlkaXF1ZSI6IiIsImlkZW50aWZpYW50VGVjaG5pcXVlRGVMYVN0cnVjdHVyZSI6IlI4MzcxNCIsInJhaXNvblNvY2lhbGVTaXRlIjoiQ0FCSU5FVCBNIERPQzAwMjQ1NjYiLCJlbnNlaWduZUNvbW1lcmNpYWxlU2l0ZSI6IiIsImNvbXBsZW1lbnREZXN0aW5hdGFpcmUiOiJDQUJJTkVUIE0gRE9DIiwiY29tcGxlbWVudFBvaW50R2VvZ3JhcGhpcXVlIjoiIiwibnVtZXJvVm9pZSI6IjEiLCJpbmRpY2VSZXBldGl0aW9uVm9pZSI6IiIsImNvZGVUeXBlRGVWb2llIjoiUiIsImxpYmVsbGVWb2llIjoiTk9JUiIsIm1lbnRpb25EaXN0cmlidXRpb24iOiIiLCJidXJlYXVDZWRleCI6Ijc1MDA5IFBBUklTIiwiY29kZVBvc3RhbCI6Ijc1MDA5IiwiY29kZUNvbW11bmUiOiIiLCJjb2RlUGF5cyI6Ijk5MDAwIiwidGVsZXBob25lIjoiIiwidGVsZXBob25lMiI6IiIsInRlbGVjb3BpZSI6IiIsImFkcmVzc2VFTWFpbCI6IiIsImNvZGVEZXBhcnRlbWVudCI6IiIsImFuY2llbklkZW50aWZpYW50RGVMYVN0cnVjdHVyZSI6IjQ5OTcwMDI0NTY2NzAwOCIsImF1dG9yaXRlRGVucmVnaXN0cmVtZW50IjoiQ05PTS9DTk9NL0NOT00ifSx7ImNvZGVNb2RlRXhlcmNpY2UiOiJTIiwiY29kZVNlY3RldXJEYWN0aXZpdGUiOiJTQTAxIiwiY29kZVNlY3Rpb25QaGFybWFjaWVuIjoiIiwiY29kZVJvbGUiOiIiLCJudW1lcm9TaXJldFNpdGUiOiIiLCJudW1lcm9TaXJlblNpdGUiOiIiLCJudW1lcm9GaW5lc3NTaXRlIjoiMEIwMTM5Mzc0IiwibnVtZXJvRmluZXNzZXRhYmxpc3NlbWVudEp1cmlkaXF1ZSI6IjFCMDA1ODk4MiIsImlkZW50aWZpYW50VGVjaG5pcXVlRGVMYVN0cnVjdHVyZSI6IkYwQjAxMzkzNzQiLCJyYWlzb25Tb2NpYWxlU2l0ZSI6IkhPUElUQUwgR0VORVJJUVVFICBGSU4gVkFSSSIsImVuc2VpZ25lQ29tbWVyY2lhbGVTaXRlIjoiIiwiY29tcGxlbWVudERlc3RpbmF0YWlyZSI6IiIsImNvbXBsZW1lbnRQb2ludEdlb2dyYXBoaXF1ZSI6IiIsIm51bWVyb1ZvaWUiOiIxMCIsImluZGljZVJlcGV0aXRpb25Wb2llIjoiIiwiY29kZVR5cGVEZVZvaWUiOiJSIiwibGliZWxsZVZvaWUiOiJERSBQQVJJUyIsIm1lbnRpb25EaXN0cmlidXRpb24iOiIiLCJidXJlYXVDZWRleCI6IlBBUklTIiwiY29kZVBvc3RhbCI6Ijc1MDA5IiwiY29kZUNvbW11bmUiOiIiLCJjb2RlUGF5cyI6IiIsInRlbGVwaG9uZSI6IiIsInRlbGVwaG9uZTIiOiIiLCJ0ZWxlY29waWUiOiIiLCJhZHJlc3NlRU1haWwiOiIiLCJjb2RlRGVwYXJ0ZW1lbnQiOiIiLCJhbmNpZW5JZGVudGlmaWFudERlTGFTdHJ1Y3R1cmUiOiIxMEIwMTM5Mzc0IiwiYXV0b3JpdGVEZW5yZWdpc3RyZW1lbnQiOiJDTk9NL0NOT00vQVJTIn1dfV19LCJTdWJqZWN0T3JnYW5pemF0aW9uSUQiOiJSODM3MTQiLCJTdWJqZWN0Um9sZSI6WyIxMF4xLjIuMjUwLjEuMjEzLjEuMS41LjUiXSwiUFNJX0xvY2FsZSI6IjEuMi4yNTAuMS4yMTMuMS4zLjEuMSIsIm90aGVySURzIjpbeyJpZGVudGlmaWFudCI6Ijg5OTcwMDI0NTY2NyIsIm9yaWdpbmUiOiJSUFBTIiwicXVhbGl0ZSI6MX1dLCJTdWJqZWN0TmFtZUlEIjoiODk5NzAwMjQ1NjY3IiwiZmFtaWx5X25hbWUiOiJET0MwMDI0NTY2In0";
		obUserInfo = Helper.jsonBase64StringToUserInfo(encodedUserinfo);
		assertEquals("DOC0024566",obUserInfo.getFamilyName());
		assertEquals("KIT", obUserInfo.getGivenName());
		
		//userInfo avec des champs qui n'existent pas
		userinfo = "{\"SecteurXXX\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_LocaleXXX\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		encodedUserinfo = Helper.encodeBase64(userinfo);
		obUserInfo = Helper.jsonBase64StringToUserInfo(encodedUserinfo);
		assertEquals("SPECIALISTE0021889",obUserInfo.getFamilyName());
		assertEquals("ROBERT", obUserInfo.getGivenName());			
	}
	
	@Test
	public void jsonStringToUserInfoTest()
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		//cas jsonUserInfo sans champs 'otherIds'
		String userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		fr.ans.api.sign.esignsante.psc.model.UserInfo obUserInfo = Helper.jsonStringToUserInfo(userinfo);
		assertEquals("SPECIALISTE0021889",obUserInfo.getFamilyName());
		assertEquals("ROBERT", obUserInfo.getGivenName());
		assertEquals("899700218896", obUserInfo.getPreferredUsername());
		assertEquals("CABINET M SPECIALISTE0021889", obUserInfo.getSubjectOrganization());
		
		//cas jsonUserInfo avec champs 'otherIds' 
		userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700366240\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M DOC0036624\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700366240\",\"given_name\":\"KIT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"DR\",\"nomDexercice\":\"DOC0036624\",\"prenomDexercice\":\"KIT\",\"codeTypeSavoirFaire\":\"S\",\"codeSavoirFaire\":\"SM26\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"R95141\",\"raisonSocialeSite\":\"CABINET M DOC0036624\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"CABINET M DOC\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"NOIR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"75009 PARIS\",\"codePostal\":\"75009\",\"codeCommune\":\"\",\"codePays\":\"99000\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"\",\"ancienIdentifiantDeLaStructure\":\"499700366240007\",\"autoriteDenregistrement\":\"CNOM/CNOM/CNOM\"},{\"codeModeExercice\":\"S\",\"codeSecteurDactivite\":\"SA01\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"0B0172805\",\"numeroFinessetablissementJuridique\":\"1B0062023\",\"identifiantTechniqueDeLaStructure\":\"F0B0172805\",\"raisonSocialeSite\":\"HOPITAL GENERIQUE  FIN VARI\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"10\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"DE PARIS\",\"mentionDistribution\":\"\",\"bureauCedex\":\"PARIS\",\"codePostal\":\"75009\",\"codeCommune\":\"\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"\",\"ancienIdentifiantDeLaStructure\":\"10B0172805\",\"autoriteDenregistrement\":\"CNOM/CNOM/ARS\"}]}]},\"SubjectOrganizationID\":\"R95141\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"otherIds\":[{\"identifiant\":\"899700366240\",\"origine\":\"RPPS\",\"qualite\":1}],\"SubjectNameID\":\"899700366240\",\"family_name\":\"DOC0036624\"}";
		obUserInfo = Helper.jsonStringToUserInfo(userinfo);
		assertEquals("DOC0036624",obUserInfo.getFamilyName());
		assertEquals("KIT", obUserInfo.getGivenName());
		assertEquals("899700366240", obUserInfo.getPreferredUsername());
		assertEquals("CABINET M DOC0036624", obUserInfo.getSubjectOrganization());
		
		//UserInfo en ligne 
		// https://industriels.esante.gouv.fr/produits-services/pro-sante-connect/userinfo
		// {"Secteur_Activite":"SA07^1.2.250.1.71.4.2.4","sub":"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:ANS20210107161422","email_verified":false,"SubjectOrganization":"CAB MED BIS TOUBIB0023550","Mode_Acces_Raison":"","preferred_username":"ANS20210107161422","given_name":"Paul","Acces_Regulation_Medicale":"FAUX","UITVersion":"1.0","Palier_Authentification":"APPPRIP3^1.2.250.1.213.1.5.1.1.1","SubjectRefPro":{"codeCivilite":"M","exercices":[{"codeProfession":"10","codeCategorieProfessionnelle":"C","codeCiviliteDexercice":"M","nomDexercice":"Docteur OIDC","prenomDexercice":"Paul","codeTypeSavoirFaire":"","codeSavoirFaire":"","activities":[{"codeModeExercice":"L","codeSecteurDactivite":"SA07","codeSectionPharmacien":"","codeRole":"","codeGenreActivite":"GENR01","numeroSiretSite":"","numeroSirenSite":"","numeroFinessSite":"","numeroFinessetablissementJuridique":"","identifiantTechniqueDeLaStructure":"","raisonSocialeSite":"CAB MED BIS TOUBIB0023550","enseigneCommercialeSite":"","complementDestinataire":"","complementPointGeographique":"","numeroVoie":"","indiceRepetitionVoie":"","codeTypeDeVoie":"R","libelleVoie":"PARIS","mentionDistribution":"","bureauCedex":"","codePostal":"75009","codeCommune":"75109","codePays":"","telephone":"","telephone2":"","telecopie":"","adresseEMail":"","codeDepartement":"75","ancienIdentifiantDeLaStructure":"","autoriteDenregistrement":""},{"codeModeExercice":"L","codeSecteurDactivite":"SA07","codeSectionPharmacien":"","codeRole":"","codeGenreActivite":"GENR01","numeroSiretSite":"","numeroSirenSite":"","numeroFinessSite":"","numeroFinessetablissementJuridique":"","identifiantTechniqueDeLaStructure":"","raisonSocialeSite":"CABINET MEDICAL0023550","enseigneCommercialeSite":"","complementDestinataire":"","complementPointGeographique":"","numeroVoie":"2","indiceRepetitionVoie":"","codeTypeDeVoie":"R","libelleVoie":"LIBERTA","mentionDistribution":"","bureauCedex":"","codePostal":"75009","codeCommune":"75109","codePays":"","telephone":"","telephone2":"","telecopie":"","adresseEMail":"","codeDepartement":"75","ancienIdentifiantDeLaStructure":"","autoriteDenregistrement":""},{"codeModeExercice":"S","codeSecteurDactivite":"SA01","codeSectionPharmacien":"","codeRole":"","codeGenreActivite":"GENR01","numeroSiretSite":"","numeroSirenSite":"","numeroFinessSite":"","numeroFinessetablissementJuridique":"","identifiantTechniqueDeLaStructure":"","raisonSocialeSite":"HOPITAL GENERIQUE","enseigneCommercialeSite":"","complementDestinataire":"","complementPointGeographique":"","numeroVoie":"20","indiceRepetitionVoie":"","codeTypeDeVoie":"R","libelleVoie":"DE PARIS","mentionDistribution":"","bureauCedex":"","codePostal":"75020","codeCommune":"75120","codePays":"","telephone":"","telephone2":"","telecopie":"","adresseEMail":"","codeDepartement":"75","ancienIdentifiantDeLaStructure":"","autoriteDenregistrement":""},{"codeModeExercice":"S","codeSecteurDactivite":"SA43","codeSectionPharmacien":"","codeRole":"","codeGenreActivite":"GENR01","numeroSiretSite":"00000000016972","numeroSirenSite":"000000000","numeroFinessSite":"","numeroFinessetablissementJuridique":"","identifiantTechniqueDeLaStructure":"","raisonSocialeSite":"CONSEIL DES ORDRES","enseigneCommercialeSite":"","complementDestinataire":"","complementPointGeographique":"","numeroVoie":"28","indiceRepetitionVoie":"","codeTypeDeVoie":"R","libelleVoie":"DES INVALIDES","mentionDistribution":"","bureauCedex":"","codePostal":"75009","codeCommune":"75109","codePays":"","telephone":"","telephone2":"","telecopie":"","adresseEMail":"","codeDepartement":"75","ancienIdentifiantDeLaStructure":"","autoriteDenregistrement":""},{"codeModeExercice":"S","codeSecteurDactivite":"SA43","codeSectionPharmacien":"","codeRole":"","codeGenreActivite":"GENR01","numeroSiretSite":"00000000079608","numeroSirenSite":"000000000","numeroFinessSite":"","numeroFinessetablissementJuridique":"","identifiantTechniqueDeLaStructure":"","raisonSocialeSite":"ORGANISME ND7960","enseigneCommercialeSite":"","complementDestinataire":"","complementPointGeographique":"","numeroVoie":"2","indiceRepetitionVoie":"","codeTypeDeVoie":"R","libelleVoie":"MOULIN VERT","mentionDistribution":"","bureauCedex":"","codePostal":"75009","codeCommune":"75109","codePays":"","telephone":"","telephone2":"","telecopie":"","adresseEMail":"","codeDepartement":"75","ancienIdentifiantDeLaStructure":"","autoriteDenregistrement":""}]}]},"SubjectOrganizationID":"","SubjectRole":["10^1.2.250.1.213.1.1.5.5"],"PSI_Locale":"1.2.250.1.213.1.3.1.1","otherIds":[{"identifiant":"ANS20210107161422","origine":"EDIT","qualite":1}],"SubjectNameID":"ANS20210107161422","family_name":"NOM"}
		userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:ANS20210107161422\",\"email_verified\":false,\"SubjectOrganization\":\"CAB MED BIS TOUBIB0023550\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"ANS20210107161422\",\"given_name\":\"Paul\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"Docteur OIDC\",\"prenomDexercice\":\"Paul\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"codeGenreActivite\":\"GENR01\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CAB MED BIS TOUBIB0023550\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PARIS\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"},{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"codeGenreActivite\":\"GENR01\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET MEDICAL0023550\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"2\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"LIBERTA\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"},{\"codeModeExercice\":\"S\",\"codeSecteurDactivite\":\"SA01\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"codeGenreActivite\":\"GENR01\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"HOPITAL GENERIQUE\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"20\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"DE PARIS\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75020\",\"codeCommune\":\"75120\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"},{\"codeModeExercice\":\"S\",\"codeSecteurDactivite\":\"SA43\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"codeGenreActivite\":\"GENR01\",\"numeroSiretSite\":\"00000000016972\",\"numeroSirenSite\":\"000000000\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CONSEIL DES ORDRES\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"28\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"DES INVALIDES\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"},{\"codeModeExercice\":\"S\",\"codeSecteurDactivite\":\"SA43\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"codeGenreActivite\":\"GENR01\",\"numeroSiretSite\":\"00000000079608\",\"numeroSirenSite\":\"000000000\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"ORGANISME ND7960\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"2\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"MOULIN VERT\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"otherIds\":[{\"identifiant\":\"ANS20210107161422\",\"origine\":\"EDIT\",\"qualite\":1}],\"SubjectNameID\":\"ANS20210107161422\",\"family_name\":\"NOM\"}";		
		obUserInfo = Helper.jsonStringToUserInfo(userinfo); //pas d'exception => c'est OK
	}

	@Test
	public void jsonToUserInfoExcepTest() {

		String userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		String encodedUserinfo = "";

		// non encodé
		try {
			Helper.jsonBase64StringToUserInfo(userinfo);
			assertFalse(true);
		} catch (Exception e) {
		}

		// non UserUnfo
		//userinfo = "{\"SecteurXXX\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		//non parsable
		userinfo = "{\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		try {
			encodedUserinfo = Helper.encodeBase64(userinfo);
		} catch (UnsupportedEncodingException e) {
			assertTrue(false); // erreur non souhaitée..
		}
		// test
		try {
			Helper.jsonBase64StringToUserInfo(encodedUserinfo);
			assertTrue(false);
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			// OK
		}

		// non json
		userinfo = "NonJSON";
		try {
			encodedUserinfo = Helper.encodeBase64(userinfo);
		} catch (UnsupportedEncodingException e) {
			assertTrue(false);
		}
		// test
		try {
			Helper.jsonBase64StringToUserInfo(encodedUserinfo);
			assertTrue(false);
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			// OK
		}
	}	
}
