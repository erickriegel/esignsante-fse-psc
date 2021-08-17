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
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
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
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<List<String>>  re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" Réception d'une demande des CAs");
		
       //Controle du header 'accept'
		/*
		if (acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE)) {
		}
		else {
			log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
					+ " so no example is generated");
		}
		*/
	
		List<String> results = esignWS.getCA();
		re = new ResponseEntity<>(results, HttpStatus.OK);
		return re;
	}
}
