package fr.ans.api.sign.esignsante.psc.esignsantewebservices.call;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.ans.esignsante.ApiClient;
import fr.ans.esignsante.api.XadesApi;
import fr.ans.esignsante.model.ESignSanteSignatureReport;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/*
 * Classe qui fait les appels à EsiagnsanteWebservices pour la signature des documents
 * et les contrôles de document signés.
 */
public class EsignsanteCall {

	ApiClient esignWSApiClient = null;
	
	public void chekSignatureXades() {
		
		String basePath = "http://localhost:8081/esignsante/v1";
		esignWSApiClient = new ApiClient();
		esignWSApiClient.setBasePath(basePath);
		
	
	 ESignSanteSignatureReport report;
	 List<String> signers = new ArrayList<String>();
	 signers.add("medecin1");
	 signers.add("patient1");
	 File fileToSign = new File("C:\\Users\\cjuillard\\eclipse-workspace\\esignsante-psc\\src\\main\\resources\\application.properties");
	 
	 String secret = "password";
		Long idSignConf = 3L;
		
	 log.debug("appel esignsanteWebservices pour une demande de signature en xades");
		log.debug("paramètres: basePath: " + basePath + " idSignConf: " + idSignConf + " file: " + fileToSign.getName() );
		log.debug("fileExist:" + fileToSign.exists() );
		
	
	 XadesApi api = new XadesApi(esignWSApiClient);
		try {
	 report = api.signatureXades(secret, idSignConf, fileToSign, signers);
	 log.debug("sortie de fct");
	 log.debug(report.getDocSigne().toString());
	 log.debug("nbError: " + report.getErreurs().size());
		} catch (Exception e) {
			log.debug("plantage appel esignsante");
			log.debug(e.getMessage());
			log.debug(e.toString());
		
		}
	
	}
}
