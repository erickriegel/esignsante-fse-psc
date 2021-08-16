package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.ChecksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
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

	@Autowired
	EsignsanteCall esignWS;
	
@Override
 
public ResponseEntity<Result> postChecksignaturePades(@ApiParam(value = "") @Valid @RequestPart(value = "file", required = true) MultipartFile file) {
	final Optional<String> acceptHeader = getAcceptHeader();
	ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	log.trace(" 2222 Réception d'une demande Checksignature Pades");
	//return re;
	return null;
}

@Override
public ResponseEntity<Result> postChecksignatureXades(@ApiParam(value = "") @Valid @RequestPart(value = "file", required = false) MultipartFile file) {
	final Optional<String> acceptHeader = getAcceptHeader();
	ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	log.trace("Réception d'une demande Checksignature Xades. fileName: "  );
	File fileToCheck = null;
	assert file!=null;
	Path filepath = null;
	try {
		filepath = multipartFileToFile(file, FileSystems.getDefault().getPath("."));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
//	try {
//		fileToCheck = file.getResource().;
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		log.debug("KOKO");
//	}
//	log.debug("file: " + file.getContentType());
	
	try {
		log.debug("file: {} filepath {}", file.getResource().contentLength(), filepath.toString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//	try {
//		fileToCheck = File.createTempFile("MyAppName-", ".tmp");
//		file.transferTo(fileToCheck);
//		
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	

	
	
	log.debug("appel esignsante pour un contrôle de siganture en xades");
	esignWS.chekSignatureXades();
	log.debug("retour appel esignsante pour un contrôle de siganture en xades - formatage de la réponse");

	
	


	
	//return re;
	return null;
}


 
}