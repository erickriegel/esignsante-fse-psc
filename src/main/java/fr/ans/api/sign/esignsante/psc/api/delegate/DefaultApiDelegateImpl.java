package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.DefaultApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.Operation;
import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
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
		
		//tmp: test pour écriture MongoDB Online
	//	 todelete();
		 //fin tmp: test pour écriture MongoDB Online
		 
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
	
	private void todelete() {
		log.info("test ecriture BDD => use mongoTemplate ");
		   ArchiveSignature arch1 = new ArchiveSignature( new Date(),"Transmis1","Med1" );
		   ArchiveSignature arch2 = new ArchiveSignature( new Date(),"Transmis2","Med2" );
		
		
		   mongoTemplate.save(arch1, "collTodelete");
		   mongoTemplate.save(arch2, "collTodelete");
	}
}
