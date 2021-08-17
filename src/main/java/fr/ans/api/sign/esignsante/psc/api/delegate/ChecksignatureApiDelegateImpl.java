package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import java.util.stream.Collectors;

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
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.Erreur;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import fr.ans.api.sign.esignsante.psc.model.Error;
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
	log.trace("Réception d'une demande Checksignature Pades");
	
	//TODO factorisation avec postChecksignatureXades
	File fileToCheck = null;
	try {
		fileToCheck = multipartFileToFile(file);
		log.debug("fileToCheck length" + fileToCheck.length());
		log.debug("fileToCheck isFile" + fileToCheck.isFile());

		log.debug("appel esignsante pour un contrôle de siganture en PADES");
		ESignSanteValidationReport report = esignWS.chekSignaturePades(fileToCheck);
		log.debug("retour appel esignsante pour un contrôle de siganture en PADES - formatage de la réponse");
		log.debug("vreport {}", report.isValide());

		List<Erreur> erreurs = report.getErreurs();
		
		List<Error> errors = Helper.mapListErreurToListError(erreurs);
		
		Result result = new Result();
		result.setValid(report.isValide());
		result.setErrors(errors);
		
		re = new ResponseEntity<>(result,HttpStatus.OK);
	} catch (IOException e) {
		// Pb technique sur fichier reçu. => erreur 500 
		e.printStackTrace();
		re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500 à confirmer
	}
	return re;
}

@Override
public ResponseEntity<Result> postChecksignatureXades(@ApiParam(value = "") @Valid @RequestPart(value = "file", required = false) MultipartFile file) {
	final Optional<String> acceptHeader = getAcceptHeader();
	ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	log.trace("Réception d'une demande Checksignature Xades."  );

	
	assert file!=null;
	
	File fileToCheck = null;
	try {
		fileToCheck = multipartFileToFile(file);
		log.debug("fileToCheck length" + fileToCheck.length());
		log.debug("fileToCheck isFile" + fileToCheck.isFile());

		log.debug("appel esignsante pour un contrôle de siganture en xades");
		ESignSanteValidationReport report = esignWS.chekSignatureXades(fileToCheck);
		log.debug("retour appel esignsante pour un contrôle de siganture en xades - formatage de la réponse");
		log.debug("vreport {}", report.isValide());

		List<Erreur> erreurs = report.getErreurs();
		
		List<Error> errors = Helper.mapListErreurToListError(erreurs);
		
		Result result = new Result();
		result.setValid(report.isValide());
		result.setErrors(errors);
		
		re = new ResponseEntity<>(result,HttpStatus.OK);
	} catch (IOException e) {
		// Pb technique sur fichier reçu. => erreur 500 
		e.printStackTrace();
		re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500 à confirmer
	}

	
	
   
	return re;

}

//private static List<Error> mapListErreurToListError (List<Erreur> erreurs) {
//	CollectionTransformer transformer = new CollectionTransformer<Integer, String>() {
//        @Override  
//        String transform(Integer e) {
//            return e.toString();
//        }
//    };
//	return transformer.transform(erreurs);
//	
//
//	}


 
}