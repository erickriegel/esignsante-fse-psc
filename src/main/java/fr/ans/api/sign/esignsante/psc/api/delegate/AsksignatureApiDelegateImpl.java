package fr.ans.api.sign.esignsante.psc.api.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.AsksignatureApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.model.Error;
import fr.ans.api.sign.esignsante.psc.model.Result;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.Erreur;
import fr.ans.esignsante.model.OpenidToken;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe DefaultApiDelegateImpl. Implementation des EndPoints /checksignature/*
 */
@Service
@Slf4j
public class AsksignatureApiDelegateImpl extends AbstractApiDelegate implements AsksignatureApiDelegate  {
	
	@Autowired
	EsignsanteCall esignWS;
	
	@Override
	public ResponseEntity<Document> postAskSignaturePades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false)  String userinfo) {	
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		log.trace("Réception d'une demande de signature Pades");
	
		//return re;
		return null;
	}

	@Override
	public ResponseEntity<Document> postAsksignatureXades(@ApiParam(value = "" ,required=true) @RequestHeader(value="access_token", required=true) String accessToken,@ApiParam(value = "Objet contenant le fichier ) signer et le UserInfo") @Valid @RequestPart(value = "file", required = true) MultipartFile file,@ApiParam(value = "") @Valid @RequestPart(value = "userinfo", required = false)  String userinfo) {
		final Optional<String> acceptHeader = getAcceptHeader();
		ResponseEntity<Document> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
		
		//génération d'un UUDI
		String requestID = Helper.generateRequestId();
		
		//construction du OpenIDToken
		List<OpenidToken> openidTokens = new ArrayList<OpenidToken>();
		OpenidToken openidToken = new OpenidToken();
		openidToken.setUserInfo(userinfo);
		openidToken.setAccessToken(accessToken);
		log.error("TODO ICI: reponseINtorpection tmp en dur");
		openidToken.setIntrospectionResponse("{\"exp\":1628250495,\"iat\":1628250435,\"auth_time\":1628250434,\"jti\":\"0cc00f99-3f1b-4799-9222-a944ca82c310\",\"iss\":\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"typ\":\"Bearer\",\"azp\":\"ans-poc-bas-psc\",\"nonce\":\"\",\"session_state\":\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\",\"preferred_username\":\"899700218896\",\"email_verified\":false,\"acr\":\"eidas3\",\"scope\":\"openid profile email scope_all\",\"client_id\":\"ans-poc-bas-psc\",\"username\":\"899700218896\",\"active\":true}");
		openidTokens.add(openidToken);
		
		List<String> signers = new ArrayList<String>();
		
		log.trace("Réception d'une demande de signature Xades");
		
		log.debug("user {}", userinfo);
		
		File fileToSign = null;
		try {
			fileToSign = multipartFileToFile(file);
			log.debug("fileToCheck length" + fileToSign.length());
			log.debug("fileToCheck isFile" + fileToSign.isFile());

			log.debug("appel esignsante pour un contrôle de siganture en xades");
			
			//extraction du userInfo des champs à perister dans MongoDB
			//avec Google
//			UserInfo s = g.fromJson(userinfo, UserInfo.class) ;
//			JSONObject json = new JSONObject(userinfo);
//			
			
			ESignSanteSignatureReportWithProof report = esignWS.signatureXades(fileToSign, signers, 
					requestID, openidTokens);
			log.debug("retour appel esignsante xades - formatage de la réponse");
			log.debug("doc signé: {}", report.getDocSigne());
			Document signedDoc = new  Document();		
//			Resource resource = new Re
//			signedDoc.setFile(report.getDocSigne());
			
			re = new ResponseEntity<>(signedDoc,HttpStatus.OK);
		} catch (IOException e) {
			// Pb technique sur fichier reçu. => erreur 500 
			e.printStackTrace();
			re = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500 à confirmer
		}

		
//		log.debug("user getFamilyName{}", userinfo.getFamilyName());
//		log.debug("user preferred_username{}", userinfo.getPreferredUsername());
//		log.debug("user givenName{}", userinfo.getGivenName());
		//log.debug("user givenName{}", user.getGivenName());
		
//		Path filepath = null;
//		try {
//			filepath = multipartFileToFile(file, FileSystems.getDefault().getPath("."));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		try {
//			log.debug("file: {} filepath {}", file.getResource().contentLength(), filepath.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
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
