package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.util.BsonUtils;
//import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofDTO;
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

	@Autowired
	ProsanteConnectCalls pscApi;
	
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
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo)  {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

		//pour test connexion prosanteConnect
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2Mjg3ODE1NTUsImlhdCI6MTYyODc4MTQ5NSwiYXV0aF90aW1lIjoxNjI4NzgxNDk0LCJqdGkiOiI0MzkyNmNmMS0zZTExLTQ4MTItOWE2ZS1hNjMxYjk0ZjZhZDciLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjE4ODk2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiMDdhOWRmMDYtNjAxMy00YWRhLWE2YWMtOTU0NDRmN2IwOWUxIiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgc2NvcGVfYWxsIiwiYWNyIjoiZWlkYXMzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiI4OTk3MDAyMTg4OTYifQ.Va0NR0lKcyZgRD3tUUb-BpzKYGUCh0gvOFbt-_c9w1ociNkG2hJTo6XxgAMEOvcpM4aVPedyhIYRyKAOaEsICH5VPVP0zvHvbtoatkFRPH_Zro05jkhvsW8X4XTuY06EJdHxUiMRZC_AqclQ1QIR5vc-0XBWFPW9vt5Q-qOqqTPLWrnDxqdVqBfwkVoKlh1Jnlv6vilSqBIsQbs_76dN8T0cChzmtk0kCJcpnXyTy__PLDLpgHITDGnLONwbaP0ofxEjTZ9OgISWQCd5GKWiv5iZrebka0Dvbjkooqt5DhIlGi30l2vLs7-eowrcxsfJGgwzJafppga0nIvBiQ83oA";
		try {
			String result = pscApi.isTokenActive(token);
			log.info("resultat PSC aceesToken en dur: {}",result);
			//TODO parser Active ou pas
		} catch (JsonProcessingException e1) {
			//log.error("INtrospection PSC erreur: JsonProcessingException);
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// controle des donnees
		// POUR test
//		if (userinfo == null) {
//			re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			return re;
////   if (secret == null) {
////       throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'secret' when calling signatureXadesWithProof");
////   }
//		}

		// génération d'un UUDI
		String requestID = Helper.generateRequestId();

		// construction du OpenIDToken
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		/*
		try {
			openidToken.setUserInfo(Helper.encodeBase64(userinfo));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		*/
		openidToken.setAccessToken(accessToken);
		log.error("TODO ICI: reponseINtorpection tmp en dur");
		/*try {
		openidToken.setIntrospectionResponse(
				Helper.encodeBase64(
				"{\"exp\":1628250495,\"iat\":1628250435,\"auth_time\":1628250434,\"jti\":\"0cc00f99-3f1b-4799-9222-a944ca82c310\",\"iss\":\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"typ\":\"Bearer\",\"azp\":\"ans-poc-bas-psc\",\"nonce\":\"\",\"session_state\":\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\",\"preferred_username\":\"899700218896\",\"email_verified\":false,\"acr\":\"eidas3\",\"scope\":\"openid profile email scope_all\",\"client_id\":\"ans-poc-bas-psc\",\"username\":\"899700218896\",\"active\":true}"));
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
*/
		List<String> signers = new ArrayList<String>();

		log.trace("Réception d'une demande de signature Xades");

		////////////////////////////////////////////////
		//                 BYPASS DONNEES EN DUR 
		openidToken.setIntrospectionResponse("aaa");
		openidToken.setAccessToken("bbb");
		openidToken.setUserInfo("ccc");
		signers.add("signataire1");
		signers.add("signataire2");
	
		//        FIN         BYPASS DONNEES EN DUR 
		////////////////////////////////////////////////		

		openidTokens.add(openidToken);

		
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

//			log.debug("appel persistence avant appel esignsante");
//			org.bson.Document bsonBidon = org.bson.Document.parse("{}");
//			// pour test stokage avant appel esignsante
//			ProofStorage proofbefore = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
//					userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
//					userToPersit.get(Helper.FAMILY_NAME), now, bsonBidon);
//			dao.archivePreuve(proofbefore);
//			log.debug("fin 1er appel persistence");
			
					log.debug("openidToken: {} \n {} \n {} \n", 
					openidTokens.get(0).getUserInfo(),
					openidTokens.get(0).getAccessToken(),
					openidTokens.get(0).getIntrospectionResponse());

					////////////////////////////////////////////////////////////////////////////////////////////////
			ESignSanteSignatureReportWithProof report = esignWS.signatureXades(fileToSign, signers,
					requestID, openidTokens);
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			//if (report== null) 
			
		//	log.error("!!!!! Appel sans openidToken à cause de  'Illegal character(s) in message header value:' ");
			log.debug("retour appel esignsante xades - formatage de la réponse");
//			log.debug("doc signé: {}", report.getDocSigne());
			Document signedDoc = new Document();

//			log.debug("essaie de conversion de la preuve extraite du rapprt esignsante en JSON/BSON");
//			ObjectMapper mapper = new ObjectMapper();
//			 String sJsonReport = mapper.writeValueAsString(report /*report.getPreuve()*/);
//			org.bson.Document bson = org.bson.Document.parse(sJsonReport);
//			
			// vrai archivage
			log.debug("essaie d'archivage de la preuve en BDD");
			ProofStorage proof = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
					userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
					userToPersit.get(Helper.FAMILY_NAME), now, report.getPreuve());
			
			//org.bson.Document bson = BsonUtils.asBson(proof);
			//Boolean isConvertEnable = BsonUtils.supportsBson(proof);
		
			dao.archivePreuve(proof);
			log.debug("FIN archivage de la preuve en BDD");
		
		//	log.debug("PReuve {}", report.getPreuve());
			
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

			///////////////////////////////////////////////
			// test avec FIle
//			 File tmpFile = File.createTempFile("response", ".xml");
//			    FileWriter writer = new FileWriter(tmpFile);
//			    writer.write(signedString);
//			    writer.close();
//			 log.debug("écriture du fichier tmp");
//			 log.debug("File name {} exist {} path {} length {} " ,tmpFile.getName(), tmpFile.exists(), tmpFile.getAbsolutePath(),
//					 tmpFile.length());
//			 
//			 Resource resourceOut = new FileSystemResource(tmpFile);
//			    signedDoc.setFile(resourceOut);
//			    log.debug("ICI");
		//	re = new ResponseEntity<>(signedDoc, HttpStatus.OK);
			// FIN test avec FIle
			///////////////////////////////////////////////
 
			//////////////////////////////////////////////
			// test avec Input Stream
		//	InputStream targetStream = new ByteArrayInputStream(signedString.getBytes());
			InputStream targetStream = new ByteArrayInputStream(signedString.getBytes());
			Document returned = new Document();
			returned.setFile(new InputStreamResource(targetStream));
			
			    re = new ResponseEntity<>(returned, HttpStatus.OK);
		} catch (IOException e) {
			// Pb technique sur fichier reçu. => erreur 500
			e.printStackTrace();
			re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 à confirmer
		}

		return re;
		

	}

}
