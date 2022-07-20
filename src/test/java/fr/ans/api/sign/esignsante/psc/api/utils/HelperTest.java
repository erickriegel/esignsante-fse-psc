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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException;
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
		String userinfo = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
		String encodedUserinfo = Helper.encodeBase64(userinfo);
		fr.ans.api.sign.esignsante.psc.model.UserInfo obUserInfo = Helper.jsonBase64StringToUserInfo(encodedUserinfo);
		assertEquals("SPECIALISTE0021889",obUserInfo.getFamilyName());
		assertEquals("ROBERT", obUserInfo.getGivenName());
		assertEquals("899700218896", obUserInfo.getPreferredUsername());
		assertEquals("CABINET M SPECIALISTE0021889", obUserInfo.getSubjectOrganization());
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
		userinfo = "{\"SecteurXXX\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
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
