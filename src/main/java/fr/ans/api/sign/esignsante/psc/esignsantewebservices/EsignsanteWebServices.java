package fr.ans.api.sign.esignsante.psc.esignsantewebservices;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

public interface EsignsanteWebServices {

	public ResponseEntity<String[]> getCa() throws URISyntaxException ;
}
 