package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import fr.ans.api.sign.esignsante.psc.api.CaApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CaApiDelegateImpl. Implementation du EndPopint /ca
 */
@Service
@Slf4j
public class CaApiDelegateImpl extends AbstractApiDelegate implements CaApiDelegate{

	@Autowired
	EsignsanteCall esignWS;
	
	@Override
	public ResponseEntity<List<String>> getCa()  {
		log.trace(" Réception d'une demande des CAs");
		ResponseEntity<List<String>>  re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		
		if (!isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)) {
			log.debug("Demande getCa rejetée (NOT_IMPLEMENTED) Accept Head = APPLICATION/JSON non présent");			
			return re;
		}
     
		List<String> results = null;
		try {
		 results = esignWS.getCA();
		} catch (ResourceAccessException e) {		
			log.error("Exception sur demande de CA (appel esignWS) {}" , e.getMessage());
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		re = new ResponseEntity<>(results, HttpStatus.OK);
		return re;
	}
}
