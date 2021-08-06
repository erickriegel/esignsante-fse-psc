package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.Optional;

import javax.validation.Valid;

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
	//return re;
	return null;
}

}