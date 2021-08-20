package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
//import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.PreuveDAO;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.OpenidToken;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;


/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /checksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate {

	@Autowired
	EsignsanteCall esignWS;

	@Autowired
	PreuveDAO dao;

	@Override
	public ResponseEntity<Document> postAskSignaturePades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace("Réception d'une demande de signature Pades");

		// return re;
		return null;
	}

	@Override
	public ResponseEntity<Document> postAsksignatureXades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

		// controle des donnees
		// POUR test
		if (userinfo == null) {
			re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return re;
//   if (secret == null) {
//       throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'secret' when calling signatureXadesWithProof");
//   }
		}

		// génération d'un UUDI
		String requestID = Helper.generateRequestId();

		// construction du OpenIDToken
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		openidToken.setUserInfo(userinfo);
		openidToken.setAccessToken(accessToken);
		log.error("TODO ICI: reponseINtorpection tmp en dur");
		
		openidToken.setIntrospectionResponse(
				"{\"exp\":1628250495,\"iat\":1628250435,\"auth_time\":1628250434,\"jti\":\"0cc00f99-3f1b-4799-9222-a944ca82c310\",\"iss\":\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"typ\":\"Bearer\",\"azp\":\"ans-poc-bas-psc\",\"nonce\":\"\",\"session_state\":\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\",\"preferred_username\":\"899700218896\",\"email_verified\":false,\"acr\":\"eidas3\",\"scope\":\"openid profile email scope_all\",\"client_id\":\"ans-poc-bas-psc\",\"username\":\"899700218896\",\"active\":true}");
//		openidToken.setIntrospectionResponse("reponse intro");
		openidTokens.add(openidToken);

		List<String> signers = new ArrayList<String>();

		log.trace("Réception d'une demande de signature Xades");

		log.debug("user {}", userinfo.toString());

		File fileToSign = null;
		try {
			fileToSign = multipartFileToFile(file);
			log.debug("fileToCheck length" + fileToSign.length());
			log.debug("fileToCheck isFile" + fileToSign.isFile());

			log.debug("appel esignsante pour un contrôle de siganture en xades");
 
			//extraction des champs à persister dans MongoDB 
			java.util.Map<String, String> userToPersit = Helper.jsonStringToPartialMap(userinfo);
			Date now = new Date();

			log.debug("appel persistence avant appel esignsante");
			org.bson.Document bsonBidon = org.bson.Document.parse("{}");
			// pour test stokage avant appel esignsante
			ProofStorage proofbefore = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
					userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
					userToPersit.get(Helper.FAMILY_NAME), now, bsonBidon);
			dao.archivePreuve(proofbefore);
			log.debug("fin 1er appel persistence");
			
					log.debug("openidToken: {} \n {} \n {} \n", 
					openidTokens.get(0).getUserInfo(),
					openidTokens.get(0).getAccessToken(),
					openidTokens.get(0).getIntrospectionResponse());

					
			ESignSanteSignatureReportWithProof report = esignWS.signatureXades(fileToSign, signers,
					requestID, null /*openidTokens*/);
			
			log.error("!!!!! ppel sans openidToken à cause de  'Illegal character(s) in message header value:' ");
			log.debug("retour appel esignsante xades - formatage de la réponse");
//			log.debug("doc signé: {}", report.getDocSigne());
			Document signedDoc = new Document();

			log.debug("essaie de conversion du rapprt en JSON/BSON");
			ObjectMapper mapper = new ObjectMapper();
			 String sJsonReport = mapper.writeValueAsString(report);
			org.bson.Document bson = org.bson.Document.parse(sJsonReport);
			
			// vrai archivage
			log.debug("essaie d'archivage de la preuve en BDD");
			ProofStorage proof = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
					userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
					userToPersit.get(Helper.FAMILY_NAME), now, bson);
			dao.archivePreuve(proof);
			log.debug("FIN archivage de la preuve en BDD");
		
		//	log.debug("PReuve {}", report.getPreuve());
			log.debug("signed {}", report.getDocSigne());
			
			String signedStringBase64 = report.getDocSigne();
		//	log.debug("signedStringBase64 {}", signedStringBase64);
			String signedString = Helper.decodeBase64(signedStringBase64);
			log.debug("signedString {}", signedString);
			
			
//			File fileOut  = new File();
//			Path pathOut = new Pat
//					
//			pathOut = Files.writeString(Path.of("my", "path"), signedString, StandardCharset.UTF_8);
//			
//			signedDoc.setFile(resource);
//			Resource resource = fileOut;
//			FileWriter writer = new FileWriter(fileOut);
//			writer.write(signedString);
//			writer.close();

			 File tmpFile = File.createTempFile("response", ".xml");
			    FileWriter writer = new FileWriter(tmpFile);
			    writer.write(signedString);
			    writer.close();
			 log.debug("écriture du fichier tmp");
			 log.debug("File name {} exist {} path {} length {} " ,tmpFile.getName(), tmpFile.exists(), tmpFile.getAbsolutePath(),
					 tmpFile.length());
			 
			 Resource resourceOut = new FileSystemResource(tmpFile);
			    signedDoc.setFile(resourceOut);
			    log.debug("ICI");
		//	re = new ResponseEntity<>(signedDoc, HttpStatus.OK);
			    re = new ResponseEntity<>(null, HttpStatus.OK);
		} catch (IOException e) {
			// Pb technique sur fichier reçu. => erreur 500
			e.printStackTrace();
			re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 à confirmer
		}

		return re;
		

	}

}
