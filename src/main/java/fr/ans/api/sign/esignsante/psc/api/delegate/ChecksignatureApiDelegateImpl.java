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
import fr.ans.api.sign.esignsante.psc.utils.TYPE_SIGNATURE;
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

	public ResponseEntity<Result> postChecksignaturePades(
			@ApiParam(value = "") @Valid @RequestPart(value = "file", required = true) MultipartFile file) {
		final Optional<String> acceptHeader = getAcceptHeader();
		log.debug("Demande Checksignature Pades reçue");
	
		return execute(TYPE_SIGNATURE.PADES, file);
	}

	@Override
	public ResponseEntity<Result> postChecksignatureXades(
			@ApiParam(value = "") @Valid @RequestPart(value = "file", required = false) MultipartFile file) {

		log.debug("Réception d'une demande Checksignature Xades.");
		return execute(TYPE_SIGNATURE.XADES, file);
	}

	private HttpStatus interceptErrorCheckSignature(Exception e) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		log.debug("message {} \n", e.getMessage());
		log.debug("toString {} \n", e.toString());
		log.debug("class Name {} \n", e.getClass().getName());
		log.debug("class Canoicalname {} \n", e.getClass().getCanonicalName());

		switch (e.getClass().getCanonicalName()) {

		case "org.springframework.web.client.HttpServerErrorException.NotImplemented":
			// 501: fichier non xm par exemple
			log.error("Echec de l'appel à esignsanteWS => HttpServerErrorException.NotImplemented");
			log.error("\t 501: cause possible: erreur sur le type de fichier fourni (par exemple non xml pour Xades)");
			status = HttpStatus.NOT_IMPLEMENTED;
			break;

		case "org.springframework.web.client.ResourceAccessException":
			// esignWS OFF par exemple
			log.error("Echec de l'appel à esignsanteWS => ResourceAccessException (esignsante non dispo");
			status = HttpStatus.SERVICE_UNAVAILABLE;
			break;

		case "org.springframework.web.client.HttpClientErrorException.NotFound":
			// 404: erreur sur l'id de la conf d'esignsWS
			log.error("Echec de l'appel à esignsanteWS => HttpClientErrorException.NotFound");
			log.error("\t cause possible: identifiant de configuration esignWS invalid.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			break;

		default:
			// comprend erreur 400: requête mal formée, 404 id conf introuvable.
			log.error("Echec de l'appel à esignsanteWS: Exception non gérée");
		}
		return status;
	}

	private Result esignsanteReportToResult(ESignSanteValidationReport report) {
		log.debug("retour appel esignsante OK pour un contrôle de signature - formatage de la réponse");
		log.debug("report {}", report.isValide());

		List<Erreur> erreurs = report.getErreurs();
		List<Error> errors = Helper.mapListErreurToListError(erreurs);

		Result result = new Result();
		result.setValid(report.isValide());
		result.setErrors(errors);
		
		return result;
	}
	
	private File getMultiParrtFile(MultipartFile file) {
		File fileToCheck = null;
		try {
			fileToCheck = multipartFileToFile(file);
			log.debug("fileToCheck isFile: {} \t name: {} \t length {}", fileToCheck.getName(), fileToCheck.isFile(), fileToCheck.length());
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return fileToCheck;
	}
	private ResponseEntity<Result> execute(TYPE_SIGNATURE typeSignature, MultipartFile file ) {
		ResponseEntity<Result> re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		//controle du acceptheader
		if (!isAcceptHeaderPresent(getAcceptHeaders(), Helper.APPLICATION_JSON)) {
			log.debug("Demande CheckSignature rejetée (NOT_IMPLEMENTED) Accept Head = APPLICATION/JSON non présent");
			return re;
		}		
        
		//assert file != null; => sinon MultipartException en amont de cette méthode
		

		File fileToCheck = getMultiParrtFile(file);
		if (fileToCheck == null) {
			return re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}

		ESignSanteValidationReport report = null;

		try {
			if (typeSignature.equals(TYPE_SIGNATURE.XADES)) {
			report = esignWS.chekSignatureXades(fileToCheck);
			}
			else {
				report = esignWS.chekSignaturePades(fileToCheck);
			}
		} catch (Exception e) {		
			return new ResponseEntity<>(interceptErrorCheckSignature(e)); 
		}
		Result result = esignsanteReportToResult(report); 
		re = new ResponseEntity<>(result, HttpStatus.OK);

		return re;
	}
}