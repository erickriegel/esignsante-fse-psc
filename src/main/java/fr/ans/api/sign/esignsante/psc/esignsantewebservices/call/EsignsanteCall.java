package fr.ans.api.sign.esignsante.psc.esignsantewebservices.call;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void signatureXadesWithProof() {
		
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
