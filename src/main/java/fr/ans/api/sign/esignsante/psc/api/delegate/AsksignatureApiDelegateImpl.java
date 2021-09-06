package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.PreuveDAO;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.api.sign.esignsante.psc.utils.PSCTokenStatus;
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
	public ResponseEntity<org.springframework.core.io.Resource> postAskSignaturePades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<org.springframework.core.io.Resource> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.debug("Réception d'une demande de signature Pades");
		
		//TODO...(à factoriser avec XADES) ...
		log.debug("userInfo {}", userinfo.toString());
		// génération d'un UUDI
				String requestID = Helper.generateRequestId();
				List<String> signers = new ArrayList<String>();

				File fileToSign = null;
				try {
					fileToSign = checkInputFile(file);
				} catch (IOException e) {
					log.error("!!! TODO gérer Exception plantage recup du fichier");
							
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}		
				
				//extraction des champs pour sauvegarde
				try {
					java.util.Map<String, String> userToPersit = Helper.jsonStringToPartialMap(userinfo);
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					log.error("!!! TODO gérer Exception erreur parse du USerINfo");
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				Date now = new Date();


				////////////////////////////////////////////////////////////////////////////////////////////////
				log.error("!!!!! Appel sans openidToken à cause de  'Illegal character(s) in message header value:' ");
				ESignSanteSignatureReportWithProof report = esignWS.signaturePades(fileToSign, signers, requestID,
						null);
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				
		// return re;
		return null;
	}

	@Override
	public ResponseEntity<String> /* ResponseEntity<Document> */ postAsksignatureXades(
			@ApiParam(value = "", required = true) @RequestHeader(value = "access_token", required = true) String accessToken,
			@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,
			@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false) String userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		// TODO 415 si pas les 2application/JSON et application
		// ResponseEntity<Document> re = new
		// ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		ResponseEntity<String> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

		log.debug("Réception d'une demande de signature Xades");

		// TODO vérifier que les champs ne sont pas nuls => requête reçue invalide
		if (accessToken.isEmpty()) {
			re = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.debug("Requete refusée car les paramètres reçus ne sont pas conformes");
			// throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
			// "postAsksignatureXades requête rejetée pour accessToken 'empty' ");
			return re;
		}

		// Vérification du Token
		PSCTokenStatus status = isValidPSCToken(accessToken);
		// TODO Gérer le status
		log.error("!!!!!!!!!!!!!!! ATTENTION BYPASS gestion de l accessToken => Token tjrs VALID !!!!!!!!!!!!!!!! ");
		// FIN TODO Gérer le status

		// génération d'un UUDI
		String requestID = Helper.generateRequestId();

		// construction du OpenIDToken
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		/*
		 * try { openidToken.setUserInfo(Helper.encodeBase64(userinfo)); } catch
		 * (UnsupportedEncodingException e2) { // TODO Auto-generated catch block
		 * e2.printStackTrace(); }
		 */
		openidToken.setAccessToken(accessToken);
		/*
		 * try { openidToken.setIntrospectionResponse( Helper.encodeBase64(
		 * "{\"exp\":1628250495,\"iat\":1628250435,\"auth_time\":1628250434,\"jti\":\"0cc00f99-3f1b-4799-9222-a944ca82c310\",\"iss\":\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"typ\":\"Bearer\",\"azp\":\"ans-poc-bas-psc\",\"nonce\":\"\",\"session_state\":\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\",\"preferred_username\":\"899700218896\",\"email_verified\":false,\"acr\":\"eidas3\",\"scope\":\"openid profile email scope_all\",\"client_id\":\"ans-poc-bas-psc\",\"username\":\"899700218896\",\"active\":true}"
		 * )); } catch (UnsupportedEncodingException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); }
		 */
		List<String> signers = new ArrayList<String>();

		////////////////////////////////////////////////
		// BYPASS DONNEES EN DUR
		openidToken.setIntrospectionResponse("aaa");
		openidToken.setAccessToken("bbb");
		openidToken.setUserInfo("ccc");
		signers.add("signataire1");
		signers.add("signataire2");

		// FIN BYPASS DONNEES EN DUR
		////////////////////////////////////////////////

		openidTokens.add(openidToken);

		log.debug("userInfo {}", userinfo.toString());

		File fileToSign = null;
		try {
			log.debug("avant appel multipartFileToFile");
			fileToSign = multipartFileToFile(file);
//			log.debug("fileToCheck length" + fileToSign.length());
//			log.debug("fileToCheck isFile" + fileToSign.isFile());

			log.debug("appel esignsante pour un contrôle de siganture en xades");

			// extraction des champs à persister dans MongoDB
			java.util.Map<String, String> userToPersit = Helper.jsonStringToPartialMap(userinfo);
			Date now = new Date();

			log.debug("openidToken: {} \n {} \n {} \n", openidTokens.get(0).getUserInfo(),
					openidTokens.get(0).getAccessToken(), openidTokens.get(0).getIntrospectionResponse());

			////////////////////////////////////////////////////////////////////////////////////////////////
			log.error("!!!!! Appel sans openidToken à cause de  'Illegal character(s) in message header value:' ");
			openidTokens = null;
			ESignSanteSignatureReportWithProof report = esignWS.signatureXades(fileToSign, signers, requestID,
					openidTokens);
			///////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (report== null)

			// log.error("!!!!! Appel sans openidToken à cause de 'Illegal character(s) in
			// message header value:' ");
			log.debug("retour appel esignsante xades - formatage de la réponse");
//			log.debug("doc signé: {}", report.getDocSigne());
			Document signedDoc = new Document();

			// report OK ou pas ???
			log.debug("report ... \n \tValide {} \n \tErrors {} \n \tDocSigné {}", report.isValide(),
					report.getErreurs(), Helper.decodeBase64(report.getDocSigne()));

			// vrai archivage de la pruve
			log.debug(" START archivage de la preuve en BDD");
			ProofStorage proof = new ProofStorage(requestID, userToPersit.get(Helper.SUBJECT_ORGANIZATION),
					userToPersit.get(Helper.PREFERRED_USERNAME), userToPersit.get(Helper.GIVEN_NAME),
					userToPersit.get(Helper.FAMILY_NAME), now, report.getPreuve());

			dao.archivePreuve(proof);
			log.debug("FIN archivage de la preuve en BDD");

			// log.debug("PReuve {}", report.getPreuve());

			String signedStringBase64 = report.getDocSigne();
			// log.debug("signedStringBase64 {}", signedStringBase64);
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
			// re = new ResponseEntity<>(signedDoc, HttpStatus.OK);
			// FIN test avec FIle
			///////////////////////////////////////////////

			//////////////////////////////////////////////
			// test avec Input Stream
			/*
			 * InputStream targetStream = new ByteArrayInputStream(signedString.getBytes());
			 * Document returned = new Document(); returned.setFile(new
			 * InputStreamResource(targetStream));
			 */

			////////////////////////////////////////////////
			// test ....
			Resource resource = new ByteArrayResource(signedString.getBytes());
			Document returned = new Document();
			returned.setFile(resource);
			// HttpHeaders httpHeaders = new HttpHeaders();
			// httpHeaders.setContentType(MediaType.TEXT_PLAIN);
			// re = new ResponseEntity<>(returned,httpHeaders, HttpStatus.OK);
			re = new ResponseEntity<>(signedString, HttpStatus.OK);

		} catch (IOException e) {
			// Pb technique sur fichier reçu. => erreur 500
			e.printStackTrace();
			re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 à confirmer
		}

		return re;
	}

	private PSCTokenStatus isValidPSCToken(String token) {
		PSCTokenStatus retour = PSCTokenStatus.NO_RESPONSE;
		try {
			String result = pscApi.isTokenActive(token);
			retour = Helper.parsePSCresponse(result);
			log.debug("Appel PSC intropesction: reponse = {}, \n PSCTokenStatus = {}", result,
					retour.getPSCTokenStatus());
		} catch (JsonProcessingException e1) {
			log.error("Exception sur introspection PSC erreur: JsonProcessingException \n {}", e1.getMessage());
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			log.error("Exception sur introspection PSC erreur: UnsupportedEncodingException \n {}", e1.getMessage());
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			log.error("Exception sur introspection PSC erreur: URISyntaxException \n {}", e1.getMessage());
			e1.printStackTrace();
		}
		return retour;
	}

	private void testRetourXML(String docSigne) {
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("file", new ByteArrayResource(docSigne.getBytes()) {

			@Override
			public String getFilename() {
				return "docSigned.xml";
			}

		}, MediaType.APPLICATION_XML);
		bodyBuilder.build();
		// bodyBuilder.
	}

	private File checkInputFile(MultipartFile file) throws IOException {
		File fileToSign = null;
		fileToSign = multipartFileToFile(file);
		log.debug("fileToCheck length" + fileToSign.length());
		log.debug("fileToCheck isFile" + fileToSign.isFile());
		return fileToSign;
	}
}
