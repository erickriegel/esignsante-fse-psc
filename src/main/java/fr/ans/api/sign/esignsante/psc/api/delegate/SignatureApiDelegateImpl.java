package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.DefaultApiDelegate;
import fr.ans.api.sign.esignsante.psc.api.SignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.ReponseDocumentSigne;
import fr.ans.api.sign.esignsante.psc.model.RequeteSignatureRecue;

@Service
public class SignatureApiDelegateImpl extends ApiDelegate implements SignatureApiDelegate {
	

	/**
	 * The log.
	 */
	Logger log = LoggerFactory.getLogger(DefaultApiDelegateImpl.class);

	/**
	 * Gets the operations.
	 *
	 * @return the operations
	 */
	@Override
	public ResponseEntity<ReponseDocumentSigne> signeDocument(RequeteSignatureRecue requeteSignatureRecue) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<ReponseDocumentSigne> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED); // A modifier ...
		if (acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE)) {
			System.err.println("ReceiveDocument: " + requeteSignatureRecue.getDocumentASigner());
			System.err.println("ReceiveToken: " + requeteSignatureRecue.getToken());
			final ReponseDocumentSigne returned = new ReponseDocumentSigne();
			returned.setIdMongoDB("TiensTonIdMongoDB...");
			returned.setDocumentSigne("Blabla..Ce document est signé");
			re = new ResponseEntity<>(returned, HttpStatus.OK);
		} else {
			log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
					+ " so no example is generated");
		}
		return re;
	}
}
