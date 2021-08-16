package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.model.RichDocument;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /checksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate  {
	

	
	@Override
	public ResponseEntity<Document> postAskSignaturePades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "") @Valid @RequestPart(value = "documenttosign", required = false)  RichDocument documenttosign) {	
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 3333 Réception d'une demande de signature Pades");
	
		//return re;
		return null;
	}

	@Override
	public ResponseEntity<Document> postAsksignatureXades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "", required=true) @Valid @RequestPart(value = "documenttosign", required = true)  RichDocument documenttosign) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 4444 Réception d'une demande de signature Xades");
		
		String secret = "password";
		Long idSignConf = 1L;
		
		File fileToSign = new File(getClass().getResource("fichier.xml").getFile());
		List<String> signers = new ArrayList<String>();
		signers.add("MedecinX");
		signers.add("PatientX");
		
		log.debug("appel esignsante pour une demande de siganture en xades");
		log.debug("paramètres:  idSignConf: " + idSignConf + "file: " + fileToSign.getName() );
		log.debug("fileExist:" + fileToSign.exists() );
		
		
//		ESignSanteSignatureReport response = esignWS.signatureXades(secret, idSignConf, fileToSign, signers);
//		
//		log.debug("erreur: " + response.getErreurs().toString());
//		log.debug("doc signe: " + response.getDocSigne().toString());
//		
		//return re;
		return null;
		
	}

	
	
}
