/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.delegate.AbstractApiDelegate;
import fr.ans.api.sign.esignsante.psc.api.delegate.AsksignatureApiDelegateImpl;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc
//@TestPropertySource(locations="classpath:application-test-withoutgravitee.properties")
//@ActiveProfiles("test-withgravitee")
@Slf4j
public class AskApiIntegrationWithGraviteeTest {

	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	EsignsanteCall esignWS;

	final String accessToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2NTcyNjg2NDYsImlhdCI6MTY1NzI2ODU4NiwiYXV0aF90aW1lIjoxNjU3MjY4NTg2LCJqdGkiOiI2ZmY0OWMwNi1kNWM4LTQxYTgtYTZjZS03MjZmMWJiNGQzY2IiLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjQ1NjY3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiODcxNmQ1OTktMWJjYS00ODU4LThkMjAtZGQ2YzhhMzNmYTYzIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBzY29wZV9hbGwiLCJzaWQiOiI4NzE2ZDU5OS0xYmNhLTQ4NTgtOGQyMC1kZDZjOGEzM2ZhNjMiLCJhY3IiOiJlaWRhczMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6Ijg5OTcwMDI0NTY2NyJ9.izsTSHGd_jkrRtY2VplyiyZwlUQ8E4WRS1mjcaDJPXfRa_UB36ekgUcz2-5a69lOXRI1Y3Xre3KfZ-G2rgYlA2UN0NOOzR0mAfELwzJToi2f0hEo_2WkqALxu9fceDHGPfdWypUIBjnY0v8NlGi4goCTFtWFXKFyarec9-qgPOwi_D9GcqP_GQylehH1bCiBNL0TImR7-73nlbMrPEtMUtqzr58S5G2eTirk9NBcziCepDovprrmIQ8ktuJW73DlHoq90ocRkBYXCtmTkdFlOjJ_GLwYhC9UONShB5iiIPCMK2ZrZ1RSD0ooEOuP4HPlsBC6rJ_oHJs4c2JoYXEY8A";
	
	String reponsePSCActifBase64 = null;
	
	String userInfobase64 = null;
	
	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws IOException {
	// La requête client ne comprends pas les headers pour le userInfo et la réponse à l'introspection	
	// => pour la génération de la doc, on utilise les tests 'sans gravitee'
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
		
		reponsePSCActifBase64 = Files.readString(
					new ClassPathResource("PSC/PSC200_xIntrospectionResponse_activeTrue.txt").getFile().toPath());
		
		assertTrue(reponsePSCActifBase64.endsWith("YWN0aXZlIjp0cnVlfQ=="));
		
		userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	assertTrue(userInfobase64.endsWith("TVEUwMDIxODg5In0="));
			}

	@Test
	@DisplayName("ask XADES 200")
	public void askXADESWithGraviteeTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
		
		report.setDocSigne(Files.readString(
				new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml.base64.txt").getFile().toPath()));
		
    	report.setValide(true);    	
    	report.setErreurs(new ArrayList<Erreur>());
   
//    	final String bodyXMLOK = (Files.readString(
//				new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));
		
    	
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

     	 Mockito.doReturn(report).when(esignWS).signatureXades(any(File.class),any(List.class),any(),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_XML);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_USERINFO,userInfobase64 );
       	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_INTROSPECTION_RESPONSE, reponsePSCActifBase64);
    	 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")
  				.file(fileXML) 
  				.headers(httpHeaders))
    			.andExpect(status().isOk());
    	  	 
 		assertEquals("application/xml", returned.andReturn().getResponse().getContentType());	
		    	 
    	 String content = returned.andReturn().getResponse().getContentAsString();
    	 assertTrue(content.contains("<ds:X509SubjectName>"));
    	 assertTrue(content.contains("URI=\"#xades-id"));
    	 
				
    	 //TODO contrôle de l'archivage
    	 
  		//returned.andDo(document("signXADES/OK"));
               
	}
	

	@Test
	@DisplayName("ask PADES 200")
	public void askPADESWithGraviteeTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
				
		report.setDocSigne("MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwEAADGCAp0wggKZAgEBMIGfMIGKMQswCQYDVQQGEwJGUjETMBEGA1UECgwKQVNJUC1TQU5URTEXMBUGA1UECwwOMDAwMiAxODc1MTI3NTExFzAVBgNVBAsMDklHQy1TQU5URSBURVNUMTQwMgYDVQQDDCtURVNUIEFDIElHQy1TQU5URSBFTEVNRU5UQUlSRSBPUkdBTklTQVRJT05TAhAdXeJePcFL3n2mp7PxEb86MA0GCWCGSAFlAwQCAQUAoIHPMBEGCiqBegGBOoN0AQIxAxYBVDAWBgoqgXoBgTqDdAEFMQgWBjEyMzQ1NjAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0yMjA4MjMxNDUwNTlaMC0GCSqGSIb3DQEJNDEgMB4wDQYJYIZIAWUDBAIBBQChDQYJKoZIhvcNAQELBQAwOwYJKoZIhvcNAQkEMS4ELERCKytSREFIdnpORG1remFnOTVmaVpDUTFyYVZXRTFRY1pxYjdnTkRsd3M9MA0GCSqGSIb3DQEBCwUABIIBAEw2tMqTFtR2c4bm7Gi4H66Zx5i0sThyoBjdtveRLovpxoxebPHPvHbfnLdCsVStiRAxRaUZ0lY4eMHWWcFskdA6pDrac9JGlcJiGEarHbBbw7AtzAXvsEda2cWOLveVEWkuESCLnpefIYTR0m8HTdg4yfimX1l+Oa9tb+OvBiFMJe9Qx7QH7ErxdB21EAZUBfLCEkwLsPceG0FEPHuJ/ZCJRcOgGAoBG5cQYJwgwJWZfvc6rtVO3WDCHz0r6EGDicLQ8fM+t1HtjQXLqPa3GcRtB3Jc8QvFoBCb0aZjaJkQplgqDDztE1f3GoTJcLLKZlx5uwTWOk9e4De/Hm3feicAAAAAAAA=");
		
        	    	
    	MockMultipartFile filePDF = new MockMultipartFile(
    			"file",
    			"ANS.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));

    	
    	 Mockito.doReturn(report).when(esignWS).signaturePades(any(File.class),any(List.class),any(),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_PDF);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_USERINFO,userInfobase64 );
       	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_INTROSPECTION_RESPONSE, reponsePSCActifBase64 );
    	 
 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(filePDF)		
  				.headers(httpHeaders))
  				.andExpect(status().isOk());
 				
    	 assertEquals("application/pdf", returned.andReturn().getResponse().getContentType());
    	
   	     			 
    			 //TODO contrôle de l'archivage
    			 
  		//returned.andDo(document("signPADES/OK"));
               
	}

	@Test
	@DisplayName("ask PADES fichier non pdf")
	public void askPADESWithGraviteeNotPDF() throws Exception {
				
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

   	
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_PDF);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_USERINFO,userInfobase64 );
       	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_INTROSPECTION_RESPONSE, reponsePSCActifBase64 );
    	 
    	 
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(fileXML)
  				.headers(httpHeaders))
  				.andExpect(status().isUnsupportedMediaType());
 				
  		//returned.andDo(document("signPADES/BadFileFormat"));
               
	}
	
	@Test
	public void checkNotEmptyInputParametersTest () throws IOException {
		MockMultipartFile file = new MockMultipartFile(
	    			"file",
	    			"ipsfra.xml",
	    			null,
					Files.readAllBytes(
							new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));
		
		MockMultipartFile emptyFile = new MockMultipartFile(
    			"file",
    			"empty.zzz",
    			null,
				Files.readAllBytes(
						new ClassPathResource("empty.zzz").getFile().toPath()));
		//cas non passant
		checkNotEmptyInputParametersKO (null,null,null, null);
		checkNotEmptyInputParametersKO ("token",null,"userinfo", "tokenValidationResponse");
		checkNotEmptyInputParametersKO ("token",emptyFile,"userinfo", "tokenValidationResponse");
		checkNotEmptyInputParametersKO (null,emptyFile,null, "tokenValidationResponse");
		checkNotEmptyInputParametersKO (null,emptyFile,"userinfo", "tokenValidationResponse");
		checkNotEmptyInputParametersKO ("token",file,null, "tokenValidationResponse");
		checkNotEmptyInputParametersKO (null,file,null, "tokenValidationResponse");
		checkNotEmptyInputParametersKO ("",file,"userinfo", "tokenValidationResponse");
		checkNotEmptyInputParametersKO ("token",file,"", "tokenValidationResponse");
		checkNotEmptyInputParametersKO ("token",file,null, "tokenValidationResponse");
		checkNotEmptyInputParametersKO (null,file,"userinfo", "tokenValidationResponse");
		checkNotEmptyInputParametersKO (null,file,"userinfo", null);
		checkNotEmptyInputParametersKO ("token",file,"userinfo", null);
		//cas  passant
		AsksignatureApiDelegateImpl ask = new AsksignatureApiDelegateImpl();
		try {
			ask.checkNotEmptyInputParameters("token",file, "userinfo","tokenValidationResponse"); 
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	private void checkNotEmptyInputParametersKO(String accessToken, MultipartFile file, String userinfo, String tokenValidationResponse ) {
		var askKO = new AsksignatureApiDelegateImpl();
		try {
			askKO.checkNotEmptyInputParameters(accessToken, file, userinfo, tokenValidationResponse); 
		assertTrue(false);
		}
		catch (Exception e) {
			assertEquals("fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException",e.getClass().getName());
		}
	}
}
