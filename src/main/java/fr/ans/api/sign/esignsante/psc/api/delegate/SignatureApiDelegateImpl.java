package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.SignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.ReponseDocumentSigne;
import fr.ans.api.sign.esignsante.psc.model.RequeteSignatureRecue;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignatureApiDelegateImpl extends AbstractApiDelegate implements SignatureApiDelegate {

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
	public ResponseEntity<ReponseDocumentSigne> signeDocument(RequeteSignatureRecue requeteSignatureRecue) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<ReponseDocumentSigne> re;
		log.trace(" MMM Demande d'une demande de signature de document");
		if (!(acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE))) {
			log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
					+ " so no example is generated");
			return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED); // oups un break...
		}

		log.trace("ReceiveDocument: " + requeteSignatureRecue.getDocumentASigner());
		log.trace("ReceiveToken: " + requeteSignatureRecue.getToken());
		if (requeteSignatureRecue.getToken().contentEquals("nonValide")) {
			re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			final ReponseDocumentSigne returned = new ReponseDocumentSigne();
			returned.setIdMongoDB("Id de preuve revoyé par esignsante-psc");
			returned.setDocumentSigne("Ceci est le docucument signé par esignsante xxxxxxx");
			re = new ResponseEntity<>(returned, HttpStatus.OK);
		}
		return re;
	}
}
