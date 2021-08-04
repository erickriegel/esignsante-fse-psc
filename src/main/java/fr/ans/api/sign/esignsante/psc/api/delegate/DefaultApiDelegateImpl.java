package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.DefaultApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class DefaultApiDelegateImpl. Implementation du EndPopint racine
 */

@Service
@Slf4j
public class DefaultApiDelegateImpl extends AbstractApiDelegate implements DefaultApiDelegate {

//	final String HEADER_TYPE = "application/json";

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
		ResponseEntity<Set<Operation>> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" OOOOO Réception d'une demande des opérations disponibles");
		if (acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE)) {
			final Set<Operation> setOperations = new HashSet<Operation>();
			setOperations.add(setOperation("/", "Liste des opérations disponibles", "", ""));

			setOperations.add(setOperation("/ca",
					"Opération qui permet au client de prendre connaissance des Autorités de Certification de confiance utilisées par la plateforme.",
					"", ""));

			setOperations.add(setOperation("/asksignature/xades",
					"Opération qui permet au client de demander de signer un document au format XADES Baseline-B.", "",
					"Access_token, accept:json, usertoken"));

			re = new ResponseEntity<>(setOperations, HttpStatus.OK);

		} else {
			log.warn(" 777777777777777777777777777777777777");
			log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
					+ " so no example is generated");
		}
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
