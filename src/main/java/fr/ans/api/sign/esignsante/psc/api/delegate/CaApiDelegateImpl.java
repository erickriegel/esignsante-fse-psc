package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.CaApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.EsignsanteWebServices;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CaApiDelegateImpl. Implementation du EndPopint /ca
 */
@Service
@Slf4j
public class CaApiDelegateImpl extends AbstractApiDelegate implements CaApiDelegate{

	@Autowired
	EsignsanteWebServices esignWS;
	
	@Override
	public ResponseEntity<List<String>> getCa()  {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<List<String>>  re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 11111 Réception d'une demande des ca");
		/*
		if (acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE)) {
		}
		else {
			log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
					+ " so no example is generated");
		}
		*/
		try {
			log.trace(" 11111 entree du try");
			//test appel à esignsante
			ResponseEntity<String[]> test = esignWS.getCa();
			log.info(test.toString());
			List<String> tmp = new ArrayList<String>(Arrays.asList(test.getBody()));
			re = new ResponseEntity<>(tmp,HttpStatus.OK );
		} catch /*(URISyntaxException e)*/ (Exception e){
			log.error("plantage sur appel esignsateWS /ca");
			log.info("msg: " + e.getMessage()); // 
			log.info("cause: " + e.getCause().getMessage());  //Connection refused: connect => esignsanteWS absent 
			log.info("causeBis: " + e.getClass().getName()); //class org.springframework.web.client.ResourceAccessException => esignsanteWS absent
			e.printStackTrace();
			String erreurClasse = e.getClass().getName(); 
			switch(erreurClasse) {
			  case  "org.springframework.web.client.ResourceAccessException":
			    // code block
			    break;
			  default:
			    // code block
			}
			
		}
		log.trace(" FIN 11111 Réception d'une demande des ca");
		return re;
	}
}
