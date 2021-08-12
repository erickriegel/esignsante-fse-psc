package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;

import fr.ans.api.sign.esignsante.psc.api.ChecksignatureApiDelegate;

import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.model.Result;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /checksignature/*
 */

@Service
@Slf4j
public class ChecksignatureApiDelegateImpl extends AbstractApiDelegate implements ChecksignatureApiDelegate {

	
@Override
public ResponseEntity<Result> postChecksignaturePades(@ApiParam(value = "", required=true) @Valid @RequestPart(value = "file", required = true)  Document file) {
	final Optional<String> acceptHeader = getAcceptHeader();
	ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	log.trace(" 2222 Réception d'une demande Checksignature Pades");
	//return re;
	return null;
}

@Override
public ResponseEntity<Result> postChecksignatureXades(@ApiParam(value = "", required=true) @Valid @RequestPart(value = "file", required = true)  Document file) {
	final Optional<String> acceptHeader = getAcceptHeader();
	ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	log.trace(" 2323 Réception d'une demande Checksignature Xades");
	
	//TODO recup param
	//tmp MIse au point => appel avec fichier des resources
	 Long idVerifSignConf = 1L;
	    File fileToCheck = null;
	log.debug("appel esignsante pour un contrôle de siganture en xades");
	log.debug("paramètres:  idVerifSignConf: " + idVerifSignConf + "file: " + fileToCheck.getName() );
	log.debug("fileExist:" + fileToCheck.exists() );
	
	


	
	//return re;
	return null;
}

}