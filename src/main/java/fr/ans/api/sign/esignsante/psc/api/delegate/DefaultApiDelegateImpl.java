package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.DefaultApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Operation;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class DefaultApiDelegateImpl. Implementation du EndPopint racine
 */

@Service
@Slf4j
public class DefaultApiDelegateImpl extends AbstractApiDelegate implements DefaultApiDelegate {

//	final String HEADER_TYPE = "application/json";

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	EsignsanteCall esignWS;
	
	/**
	 * The log.
	 */
//	Logger log = LoggerFactory.getLogger(DefaultApiDelegateImpl.class);

	/**
	 * Gets the operations.
	 *
	 * @return the operations
	 */
	@Override
	public ResponseEntity<Set<Operation>> getOperations() {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Set<Operation>> re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		log.trace("Réception d'une demande des opérations disponibles");
		
		//vérification du Headers accept
		if (!isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)) {
			log.debug("Demande getOperation rejetée (NOT_IMPLEMENTED) Accept Head = APPLICATION/JSON non présent");
			return re;
		}

			final Set<Operation> setOperations = new HashSet<Operation>();
			setOperations.add(setOperation("/", "Liste des opérations disponibles", "", ""));

			setOperations.add(setOperation("/ca",
					"Opération qui permet au client de prendre connaissance des Autorités de Certification de confiance utilisées par la plateforme.",
					"", ""));

			setOperations.add(setOperation("/asksignature/xades",
					"Opération qui permet au client de demander de signer un document au format XADES Baseline-B.", "",
					"Access_token, accept:json, usertoken"));

			re = new ResponseEntity<>(setOperations, HttpStatus.OK);

		
	
		return re;
	}

	private Operation setOperation(String path, String description, String payload, String requiredHeaders) {
		Operation op = new Operation();
		op.setPath(path);
		op.setDescription(description);
		op.setRequiredHeaders(requiredHeaders);
		op.setPayload(payload);
		return op;
	}
	
}
