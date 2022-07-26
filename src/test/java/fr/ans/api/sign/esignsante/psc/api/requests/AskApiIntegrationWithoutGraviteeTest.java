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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.api.delegate.AbstractApiDelegate;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.prosantedatas.PSCData;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test-withoutgravitee.properties")
public class AskApiIntegrationWithoutGraviteeTest {

	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	EsignsanteCall esignWS;
	
	@MockBean
	PSCData pscCall;
	

	final String accessToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2NTcyNjg2NDYsImlhdCI6MTY1NzI2ODU4NiwiYXV0aF90aW1lIjoxNjU3MjY4NTg2LCJqdGkiOiI2ZmY0OWMwNi1kNWM4LTQxYTgtYTZjZS03MjZmMWJiNGQzY2IiLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjQ1NjY3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiODcxNmQ1OTktMWJjYS00ODU4LThkMjAtZGQ2YzhhMzNmYTYzIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBzY29wZV9hbGwiLCJzaWQiOiI4NzE2ZDU5OS0xYmNhLTQ4NTgtOGQyMC1kZDZjOGEzM2ZhNjMiLCJhY3IiOiJlaWRhczMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6Ijg5OTcwMDI0NTY2NyJ9.izsTSHGd_jkrRtY2VplyiyZwlUQ8E4WRS1mjcaDJPXfRa_UB36ekgUcz2-5a69lOXRI1Y3Xre3KfZ-G2rgYlA2UN0NOOzR0mAfELwzJToi2f0hEo_2WkqALxu9fceDHGPfdWypUIBjnY0v8NlGi4goCTFtWFXKFyarec9-qgPOwi_D9GcqP_GQylehH1bCiBNL0TImR7-73nlbMrPEtMUtqzr58S5G2eTirk9NBcziCepDovprrmIQ8ktuJW73DlHoq90ocRkBYXCtmTkdFlOjJ_GLwYhC9UONShB5iiIPCMK2ZrZ1RSD0ooEOuP4HPlsBC6rJ_oHJs4c2JoYXEY8A";
	
	String reponsePSCActif = null;
	
	String userInfo = null;
	
	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws IOException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
		
		reponsePSCActif = Files.readString(
					new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		assertTrue(reponsePSCActif.endsWith("\"username\":\"899700218896\",\"active\":true}"));
		
		userInfo = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_une_ligne.json").getFile().toPath());
    	assertTrue(userInfo.endsWith("\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}"));
			}

	@Test
	@DisplayName("ask XADES 200")
	public void askXADESTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
		
		report.setDocSigne(Files.readString(
				new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml.base64.txt").getFile().toPath()));
		
    	report.setValide(true);    	
    	report.setErreurs(new ArrayList<Erreur>());
		
    	Mockito.doReturn(reponsePSCActif).when(pscCall).getIntrospectionResult(any());
    	Mockito.doNothing().when(pscCall).isPSCvalidateToken(any());
    	Mockito.doReturn(userInfo).when(pscCall).getUserInfo(any());
    	
    	
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
     
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")
  				.file(fileXML) 
  				.headers(httpHeaders))
    			.andExpect(status().isOk());
    	  	 
 		assertEquals("application/xml", returned.andReturn().getResponse().getContentType());	
		    	 
    	 String content = returned.andReturn().getResponse().getContentAsString();
    	 System.out.println("content  " + content);
    	 assertTrue(content.contains("<ds:X509SubjectName>"));
    	 assertTrue(content.contains("URI=\"#xades-id"));
    	 
  		returned.andDo(document("signXADES/OK"));              
	}
	
	
	@Test
	@DisplayName("ask XADES token non actif")
	public void askXADESTokenKOTest() throws Exception {
		
		String reponsePSCNonActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activefalse.json").getFile().toPath());    	
		
		
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

   	Mockito.doReturn(reponsePSCNonActif).when(pscCall).getIntrospectionResult(any());
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")
  				.file(fileXML)
  				.header(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken)
                .accept("application/json,application/xml"))
  				.andExpect(status().isUnauthorized());
 				    	 
  		returned.andDo(document("signXADES/TokenKo"));
               
	}


	@Test
	@DisplayName("ask FSE 200")
	public void askFSETest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
				
		report.setDocSigne("bm9uRWRpdGFibGU=");
		
    	Mockito.doReturn(reponsePSCActif).when(pscCall).getIntrospectionResult(any());
    	//Mockito.doReturn(true).when(pscCall).isPSCvalidateToken(any());
    	Mockito.doNothing().when(pscCall).isPSCvalidateToken(any());
    	Mockito.doReturn(userInfo).when(pscCall).getUserInfo(any());
    	
    	
    	 Mockito.doReturn(report).when(esignWS).signatureFSE(any(byte[].class),any(String.class),any(String.class),any(List.class),any(String.class),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_OCTET_STREAM);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/fse")
    			.param("hashFSE","DB++RDAHvzNDmkzag95fiZCQ1raVWE1QcZqb7gNDlws=")
 				.param("idFacturationPS","987654")
 				.param("typeFlux","T")
  				.headers(httpHeaders))
  				.andExpect(status().isOk());
 				
    	 System.out.println("coucou");
    	 
  //  	 assertEquals("application/octet-stream", returned.andReturn().getResponse().getContentType());
    	    			 
  		returned.andDo(document("signFSE/OK"));
               
	}
	
	
	@Test
	@DisplayName("ask PADES token non actif")
	public void askPADESTokenKOTest() throws Exception {
		
		String reponsePSCNonActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activefalse.json").getFile().toPath());    	
		
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ANS_SIGNED.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED.pdf").getFile().toPath()));
    	    	
    	Mockito.doReturn(reponsePSCNonActif).when(pscCall).getIntrospectionResult(any());

    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(fileXML)
  				.header(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION,accessToken)
                .accept("application/json,application/pdf"))
  				.andExpect(status().isUnauthorized());
 				
  		returned.andDo(document("signPADES/TokenKo"));
               
	}

	@Test
	@DisplayName("ask PADES fichier non pdf")
	public void askPADESNotPDF() throws Exception {
				
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

      	Mockito.doReturn(reponsePSCActif).when(pscCall).getIntrospectionResult(any());
  //  	Mockito.doReturn(true).when(pscCall).isPSCvalidateToken(any());
    	Mockito.doReturn(userInfo).when(pscCall).getUserInfo(any());
    	
    	
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_PDF);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
    	 
    	 
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(fileXML)
  				.headers(httpHeaders))
  				.andExpect(status().isUnsupportedMediaType());
 				
  		returned.andDo(document("signPADES/BadFileFormat"));
               
	}
	
	@Test
	@DisplayName("ask PADES 200")
	public void askPADESTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
				
		report.setDocSigne("bm9uRWRpdGFibGU=");
		
        	    	
    	MockMultipartFile filePDF = new MockMultipartFile(
    			"file",
    			"ANS.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));

    	Mockito.doReturn(reponsePSCActif).when(pscCall).getIntrospectionResult(any());
    	//Mockito.doReturn(true).when(pscCall).isPSCvalidateToken(any());
    	Mockito.doNothing().when(pscCall).isPSCvalidateToken(any());
    	Mockito.doReturn(userInfo).when(pscCall).getUserInfo(any());
    	
    	
    	 Mockito.doReturn(report).when(esignWS).signaturePades(any(File.class),any(List.class),any(),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_PDF);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(filePDF)		
  				.headers(httpHeaders))
  				.andExpect(status().isOk());
 				
    	 assertEquals("application/pdf", returned.andReturn().getResponse().getContentType());
    	    			 
  		returned.andDo(document("signPADES/OK"));
               
	}
	
	
	@Test
	@DisplayName("ask FSE token non actif")
	public void askFSETokenKOTest() throws Exception {
		
		String reponsePSCNonActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activefalse.json").getFile().toPath());    	
		    
    	Mockito.doReturn(reponsePSCNonActif).when(pscCall).getIntrospectionResult(any());

    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/fse")
    			.param("hashFSE","DB++RDAHvzNDmkzag95fiZCQ1raVWE1QcZqb7gNDlws=")
 				.param("idFacturationPS","987654")
  				.header(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION,accessToken)
                .accept("application/json,application/octet-stream"))
  				.andExpect(status().isUnauthorized());
 				
  		returned.andDo(document("signFSE/TokenKo"));
               
	}

}
