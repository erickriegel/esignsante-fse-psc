package fr.ans.api.sign.esignsante.psc.esignsantewebservices.call;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.ans.esignsante.ApiClient;
import fr.ans.esignsante.api.CaApi;
import fr.ans.esignsante.api.PadesApi;
import fr.ans.esignsante.api.XadesApi;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.OpenidToken;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/*
 * Classe qui fait les appels à EsiagnsanteWebservices pour la signature des documents
 * et les contrôles de document signés.
 */
public class EsignsanteCall {

	ApiClient esignWSApiClient = null;
	
	@Autowired
	EsignsanteConfigurationProperties esignConf;
	
	
	
	public ApiClient confApiClient() {
		esignWSApiClient = new ApiClient();
		esignWSApiClient.setBasePath(esignConf.getBasePath());
		
		log.info("Appel esignante Webservices pour basePath = " + esignConf.getBasePath());
		return esignWSApiClient;
	}
	
	public void signatureXadesWithProofDebug() {
		
		esignWSApiClient = confApiClient();
		
		
	 List<String> signers = new ArrayList<String>();
	 signers.add("medecin1");
	 signers.add("patient1");
	 //File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\main\\resources\\application.properties");
	 File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\test\\resources\\EsignSanteWS\\Xades\\pom.xml");
	 String secret = "password";
		Long idSignConf = 1L;
		
	 log.debug("appel esignsanteWebservices pour une demande de signature en xades avec: ");
		log.debug("paramètres: basePath: " + esignWSApiClient.getBasePath() + " idSignConf: " + idSignConf + " file: " + fileToSign.getName() );
//	    log.debug("basePath: " + esignWSApiClient.getBasePath() + " idSignConf: " + idSignConf + " file: " + fileToSign.getName(),
//	    		//esignWSApiClient.getBasePath() , idSignConf);
//		log.debug("fileExist:" + fileToSign.exists() );
		
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
	
String requestId = "TODO";
	 XadesApi api = new XadesApi(esignWSApiClient);
	 
	 
	  // String proofId = prof1.getId();
	   
		try {
			
//		OpenidToken tok;
//		tok.setAccessToken();

			ESignSanteSignatureReportWithProof report = api.signatureXadesWithProof(
			esignConf.getSecret(),
			esignConf.getSignatureConfId(),
			fileToSign, 
			signers,
			esignConf.getProofConfId(),
		    requestId,
			esignConf.getProofTag(),
			esignConf.getAppliantId(),
			openidTokens);
//	throws RestClientException
//  api.signatureXadesWithProof(secret, idSignConf, fileToSign, signers, idSignConf, secret, secret, secret, tok)
			
	// report = api.signatureXades(secret, idSignConf, fileToSign, signers);
	 log.debug("sortie de fct");
	 log.debug(report.getDocSigne().toString());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	
	}
	
	public ESignSanteSignatureReportWithProof signatureXades(File fileToSign, 
			List<String> signers, String requestId, List<OpenidToken> openidTokens) throws JsonProcessingException {
		
		esignWSApiClient = confApiClient();
		
	 log.debug("appel esignsanteWebservices pour une demande de signature en xades avec: ");

	 ObjectMapper objectMapper = new ObjectMapper();
	 String sOpenidTokens = objectMapper.writeValueAsString(openidTokens);
	 log.debug("sOPenIdTOken {}", sOpenidTokens); 
	
	 XadesApi api = new XadesApi(esignWSApiClient);
		log.debug("paramètres: basePath: {} \n idSignConf: {} \n proofConfId: {} \n",
				esignWSApiClient.getBasePath(), esignConf.getSignatureConfId(), esignConf.getProofConfId());
		ESignSanteSignatureReportWithProof report = null;
		try {
			report = api.signatureXadesWithProof(
			esignConf.getSecret(),
			esignConf.getSignatureConfId(),
			fileToSign, 
			signers,
			esignConf.getProofConfId(),
		    requestId,
			esignConf.getProofTag(),
			esignConf.getAppliantId(),
			openidTokens);

			
	//		String test = " sOPenIdTOken [{\"accessToken\":\"ceci_est_un_accessToken\",\"introspectionResponse\":\"{\\\"exp\\\":1628250495,\\\"iat\\\":1628250435,\\\"auth_time\\\":1628250434,\\\"jti\\\":\\\"0cc00f99-3f1b-4799-9222-a944ca82c310\\\",\\\"iss\\\":\\\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\\\",\\\"sub\\\":\\\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\\\",\\\"typ\\\":\\\"Bearer\\\",\\\"azp\\\":\\\"ans-poc-bas-psc\\\",\\\"nonce\\\":\\\"\\\",\\\"session_state\\\":\\\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\\\",\\\"preferred_username\\\":\\\"899700218896\\\",\\\"email_verified\\\":false,\\\"acr\\\":\\\"eidas3\\\",\\\"scope\\\":\\\"openid profile email scope_all\\\",\\\"client_id\\\":\\\"ans-poc-bas-psc\\\",\\\"username\\\":\\\"899700218896\\\",\\\"active\\\":true}\",\"userInfo\":\"{\\\"Secteur_Activite\\\":\\\"SA07^1.2.250.1.71.4.2.4\\\",\\\"sub\\\":\\\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\\\",\\\"email_verified\\\":false,\\\"SubjectOrganization\\\":\\\"CABINET M SPECIALISTE0021889\\\",\\\"Mode_Acces_Raison\\\":\\\"\\\",\\\"preferred_username\\\":\\\"899700218896\\\",\\\"given_name\\\":\\\"ROBERT\\\",\\\"Acces_Regulation_Medicale\\\":\\\"FAUX\\\",\\\"UITVersion\\\":\\\"1.0\\\",\\\"Palier_Authentification\\\":\\\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\\\",\\\"SubjectRefPro\\\":{\\\"codeCivilite\\\":\\\"M\\\",\\\"exercices\\\":[{\\\"codeProfession\\\":\\\"10\\\",\\\"codeCategorieProfessionnelle\\\":\\\"C\\\",\\\"codeCiviliteDexercice\\\":\\\"M\\\",\\\"nomDexercice\\\":\\\"SPECIALISTE0021889\\\",\\\"prenomDexercice\\\":\\\"ROBERT\\\",\\\"codeTypeSavoirFaire\\\":\\\"\\\",\\\"codeSavoirFaire\\\":\\\"\\\",\\\"activities\\\":[{\\\"codeModeExercice\\\":\\\"L\\\",\\\"codeSecteurDactivite\\\":\\\"SA07\\\",\\\"codeSectionPharmacien\\\":\\\"\\\",\\\"codeRole\\\":\\\"\\\",\\\"numeroSiretSite\\\":\\\"\\\",\\\"numeroSirenSite\\\":\\\"\\\",\\\"numeroFinessSite\\\":\\\"\\\",\\\"numeroFinessetablissementJuridique\\\":\\\"\\\",\\\"identifiantTechniqueDeLaStructure\\\":\\\"\\\",\\\"raisonSocialeSite\\\":\\\"CABINET M SPECIALISTE0021889\\\",\\\"enseigneCommercialeSite\\\":\\\"\\\",\\\"complementDestinataire\\\":\\\"\\\",\\\"complementPointGeographique\\\":\\\"\\\",\\\"numeroVoie\\\":\\\"1\\\",\\\"indiceRepetitionVoie\\\":\\\"\\\",\\\"codeTypeDeVoie\\\":\\\"R\\\",\\\"libelleVoie\\\":\\\"PASTEUR\\\",\\\"mentionDistribution\\\":\\\"\\\",\\\"bureauCedex\\\":\\\"\\\",\\\"codePostal\\\":\\\"75009\\\",\\\"codeCommune\\\":\\\"75109\\\",\\\"codePays\\\":\\\"\\\",\\\"telephone\\\":\\\"\\\",\\\"telephone2\\\":\\\"\\\",\\\"telecopie\\\":\\\"\\\",\\\"adresseEMail\\\":\\\"\\\",\\\"codeDepartement\\\":\\\"75\\\",\\\"ancienIdentifiantDeLaStructure\\\":\\\"\\\",\\\"autoriteDenregistrement\\\":\\\"\\\"}]}]},\\\"SubjectOrganizationID\\\":\\\"\\\",\\\"SubjectRole\\\":[\\\"10^1.2.250.1.213.1.1.5.5\\\"],\\\"PSI_Locale\\\":\\\"1.2.250.1.213.1.3.1.1\\\",\\\"SubjectNameID\\\":\\\"899700218896\\\",\\\"family_name\\\":\\\"SPECIALISTE0021889\\\"}\"}]";
			//String test = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"Mode_Acces_Raison\":\"\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"Acces_Regulation_Medicale\":\"FAUX\",\"UITVersion\":\"1.0\",\"Palier_Authentification\":\"APPPRIP3^1.2.250.1.213.1.5.1.1.1\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"codeCategorieProfessionnelle\":\"C\",\"codeCiviliteDexercice\":\"M\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\",\"codeSavoirFaire\":\"\",\"activities\":[{\"codeModeExercice\":\"L\",\"codeSecteurDactivite\":\"SA07\",\"codeSectionPharmacien\":\"\",\"codeRole\":\"\",\"numeroSiretSite\":\"\",\"numeroSirenSite\":\"\",\"numeroFinessSite\":\"\",\"numeroFinessetablissementJuridique\":\"\",\"identifiantTechniqueDeLaStructure\":\"\",\"raisonSocialeSite\":\"CABINET M SPECIALISTE0021889\",\"enseigneCommercialeSite\":\"\",\"complementDestinataire\":\"\",\"complementPointGeographique\":\"\",\"numeroVoie\":\"1\",\"indiceRepetitionVoie\":\"\",\"codeTypeDeVoie\":\"R\",\"libelleVoie\":\"PASTEUR\",\"mentionDistribution\":\"\",\"bureauCedex\":\"\",\"codePostal\":\"75009\",\"codeCommune\":\"75109\",\"codePays\":\"\",\"telephone\":\"\",\"telephone2\":\"\",\"telecopie\":\"\",\"adresseEMail\":\"\",\"codeDepartement\":\"75\",\"ancienIdentifiantDeLaStructure\":\"\",\"autoriteDenregistrement\":\"\"}]}]},\"SubjectOrganizationID\":\"\",\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"PSI_Locale\":\"1.2.250.1.213.1.3.1.1\",\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";
			
			log.debug("sortie de fct");
	 log.debug(report.getDocSigne().toString());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	 return report;
	}
	
	
	
	public ESignSanteValidationReport chekSignatureXades(File fileToCheck) {
		
		esignWSApiClient = confApiClient(); //juste le BasePath
	
	 ESignSanteValidationReport report = null;
	 List<String> signers = new ArrayList<String>();

//	 File fileToCheck = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\test\\resources\\EsignSanteWS\\Xades\\pomSigne.txt");
	long idCheckSign = 	 esignConf.getCheckSignatureConfId();
	 log.debug("appel esignsanteWebservices pour une VERIF de signature en xades");
		log.debug("paramètres: basePath: " + esignWSApiClient.getBasePath() +
				" id checkSign d esignsante: " + idCheckSign + " file: " + fileToCheck.getName() );
		log.debug("fileExist:" + fileToCheck.exists() );
		
	
	 XadesApi api = new XadesApi(esignWSApiClient);
		try {
	 report = api.verifSignatureXades(idCheckSign, fileToCheck);
	 log.debug("sortie de fct");
	 log.debug("report valid?: " + report.isValide());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante checkXades");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	return report;
	}
	
public ESignSanteValidationReport chekSignaturePades(File fileToCheck) {
		
		esignWSApiClient = confApiClient(); //juste le BasePath
	
	 ESignSanteValidationReport report = null;
	
	long idCheckSign = 	 esignConf.getCheckSignatureConfId();
	
	 log.debug("appel esignsanteWebservices pour une VERIF de signature en PADES");
	 log.debug("paramètres: basePath: " + esignWSApiClient.getBasePath() +
				" id checkSign d esignsante: " + idCheckSign + " file: " + fileToCheck.getName() );
	 log.debug("fileExist:" + fileToCheck.exists() );
		
	
	 PadesApi api = new PadesApi(esignWSApiClient);
		try {
	 report = api.verifSignaturePades(idCheckSign, fileToCheck);
	 log.debug("report valid?: " + report.isValide());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante checkPades");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	return report;
	}

public List<String> getCA() {
	List<String> results = null;
	esignWSApiClient = confApiClient();
	CaApi api = new CaApi(esignWSApiClient);
	results = api.getCA();
	return results;
}
}
