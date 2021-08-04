package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.util.Optional;
import java.util.Set;

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
	public ResponseEntity<Document> postAskSignaturePades(@ApiParam(value = "" ,required=true) @RequestHeader(value="Access_token", required=true) String accessToken,@ApiParam(value = "" ) @RequestHeader(value="UserInfo", required=false) UserInfo userInfo,@ApiParam(value = "") @Valid @RequestPart(value = "file", required = false) MultipartFile file) {	
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 3333 Réception d'une demande de signature Pades");
		return re;
	}

	@Override
	public ResponseEntity<Document> postAsksignatureXades(@ApiParam(value = "" ,required=true) @RequestHeader(value="Access_token", required=true) String accessToken,@ApiParam(value = "" ) @RequestHeader(value="UserInfo", required=false) UserInfo userInfo,@ApiParam(value = "") @Valid @RequestPart(value = "file", required = false) MultipartFile file) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace(" 4444 Réception d'une demande de signature Xades");
		return re;
	}

}
