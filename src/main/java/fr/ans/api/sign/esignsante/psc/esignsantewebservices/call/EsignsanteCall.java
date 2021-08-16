package fr.ans.api.sign.esignsante.psc.esignsantewebservices.call;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.ProofMongoRepository;
import fr.ans.esignsante.ApiClient;
import fr.ans.esignsante.api.XadesApi;
import fr.ans.esignsante.model.ESignSanteSignatureReport;
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
	
	
	//tmp à déplacer
	 @Autowired
 	MongoTemplate mongoTemplate;
     
     @Autowired
     ProofMongoRepository repo; 
	//fin tmp à déplacer
	
	public ApiClient confApiClient() {
		esignWSApiClient = new ApiClient();
		esignWSApiClient.setBasePath(esignConf.getBasePath());
		
		log.info("Appel esignante Webservices pour basePAth = " + esignConf.getBasePath());
		return esignWSApiClient;
	}
	
	public void signatureXadesWithProof() {
		
		esignWSApiClient = confApiClient();
		
//		String basePath = "http://localhost:8081/esignsante/v1";
//		esignWSApiClient = new ApiClient();
//		esignWSApiClient.setBasePath(basePath);
		
	 List<String> signers = new ArrayList<String>();
	 signers.add("medecin1");
	 signers.add("patient1");
	 //File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\main\\resources\\application.properties");
	 File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\test\\resources\\EsignSanteWS\\Xades\\pom.xml");
	 String secret = "password";
		Long idSignConf = 1L;
		
	 log.debug("appel esignsanteWebservices pour une demande de signature en xades");
		log.debug("paramètres: basePath: " + esignWSApiClient.getBasePath() + " idSignConf: " + idSignConf + " file: " + fileToSign.getName() );
		log.debug("fileExist:" + fileToSign.exists() );
		
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
	
String requestId = "TODO";
	 XadesApi api = new XadesApi(esignWSApiClient);
	 
	 
	 
	   Date now = new Date() ;
	   ProofStorage prof1 = new ProofStorage(
			   "CABINET M SPECIALISTE0021889", //SubjectOrganization
			   "899700218896", //preferred_username
			   "ROBERT", //given_name
			   "SPECIALISTE0021889", //family name
			   now, //timestamp
			   null // TODO
			   );

	   mongoTemplate.save(prof1);

	   String proofId = prof1.getId();
	   log.info("ID de la preuve persistée: " + proofId );
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
	
	public ESignSanteValidationReport chekSignatureXades() {
		
		esignWSApiClient = confApiClient();
		
//		String basePath = "http://localhost:8081/esignsante/v1";
//		esignWSApiClient = new ApiClient();
//		esignWSApiClient.setBasePath(basePath);
		
	
	 ESignSanteValidationReport report = null;
	 List<String> signers = new ArrayList<String>();
	 signers.add("medecin1");
	 signers.add("patient1");
	 //File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\main\\resources\\application.properties");
	 File fileToCheck = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\test\\resources\\EsignSanteWS\\Xades\\pomSigne.txt");
	 String secret = "password";
		Long idSignConf = 1L;
		
	 log.debug("appel esignsanteWebservices pour une VERIF de signature en xades");
		log.debug("paramètres: basePath: " + esignWSApiClient.getBasePath() + " idSignConf: " + idSignConf + " file: " + fileToCheck.getName() );
		log.debug("fileExist:" + fileToCheck.exists() );
		
	
	 XadesApi api = new XadesApi(esignWSApiClient);
		try {
	 report = api.verifSignatureXades(idSignConf, fileToCheck);
	 log.debug("sortie de fct");
	 log.debug("report valid?: " + report.isValide());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	return report;
	}

}
