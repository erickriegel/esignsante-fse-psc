package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /checksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate  {
	

	
	@Override
	public ResponseEntity<Document> postAskSignaturePades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "") @Valid @RequestPart(value = "file", required = true) MultipartFile file,@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false)  UserInfo userinfo) {	
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 3333 Réception d'une demande de signature Pades");
	
		//return re;
		return null;
	}

	@Override
	public ResponseEntity<Document> postAsksignatureXades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "") @Valid @RequestPart(value = "file", required = true) MultipartFile file,@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false)  UserInfo userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 4444 Réception d'une demande de signature Xades");
		log.debug("userINfo {}", userinfo.toString());
		log.debug("file {}", userinfo.toString());
		
		Path filepath = null;
		try {
			filepath = multipartFileToFile(file, FileSystems.getDefault().getPath("."));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//			fileToCheck = file.getResource().;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.debug("KOKO");
//		}
//		log.debug("file: " + file.getContentType());
		
		try {
			log.debug("file: {} filepath {}", file.getResource().contentLength(), filepath.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		String secret = "password";
//		Long idSignConf = 1L;
//		
//		File fileToSign = new File(getClass().getResource("fichier.xml").getFile());
//		List<String> signers = new ArrayList<String>();
//		signers.add("MedecinX");
//		signers.add("PatientX");
//		
//		log.debug("appel esignsante pour une demande de siganture en xades");
//		log.debug("paramètres:  idSignConf: " + idSignConf + "file: " + fileToSign.getName() );
//		log.debug("fileExist:" + fileToSign.exists() );
		
		
//		ESignSanteSignatureReport response = esignWS.signatureXades(secret, idSignConf, fileToSign, signers);
//		
//		log.debug("erreur: " + response.getErreurs().toString());
//		log.debug("doc signe: " + response.getDocSigne().toString());
//		
		//return re;
		return null;
		
	}

	
	
}
