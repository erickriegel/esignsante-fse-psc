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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import fr.ans.esignsante.model.ESignSanteSignatureReportWithProof;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc
@Slf4j
public class AskApiIntegrationTest {

	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	EsignsanteCall esignWS;
	
	@MockBean
	ProsanteConnectCalls pscApi;

	final String accessToken = "accessTokenValue";
	
	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@DisplayName("ask XADES 200")
	public void askXADESTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
		
		report.setDocSigne(Files.readString(
				new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml.base64.txt").getFile().toPath()));

		
		String reponsePSCActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	assertTrue(userInfobase64.endsWith("TVEUwMDIxODg5In0="));
		
    	report.setValide(true);    	
    	report.setErreurs(new ArrayList<Erreur>());
   
    	final String bodyXMLOK = (Files.readString(
				new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));
		
    	
    	MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

    	
    	//intro PSC
    	Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
    	
     	 Mockito.doReturn(report).when(esignWS).signatureXades(any(File.class),any(List.class),any(),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_XML);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add("access_token", accessToken);
    	 
 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")
  				.file(fileXML)
  				.file("userinfo", userInfobase64.getBytes())  				
  				.headers(httpHeaders))
    			 .andExpect(status().isOk());
    	
    	 
 		assertEquals("application/xml", returned.andReturn().getResponse().getContentType());	
		

    	    	 
    	 String content = returned.andReturn().getResponse().getContentAsString();
    	 System.out.println("content  " + content);
    	 assertTrue(content.contains("<ds:X509SubjectName>"));
    	 assertTrue(content.contains("URI=\"#xades-id"));
    	 
				
    	 //TODO contrôle de l'archivage
    	 
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

    	String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	
    	//intro PSC
    	Mockito.doReturn(reponsePSCNonActif).when(pscApi).isTokenActive(any());
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")
  				.file(fileXML)
  				.file("userinfo", userInfobase64.getBytes())
  				.header("access_token", accessToken)
                .accept("application/json,application/xml"))
  				.andExpect(status().isBadRequest());
 				
    	
    	 
  		returned.andDo(document("signXADES/TokenKo"));
               
	}


	@Test
	@DisplayName("ask PADES 200")
	public void askPADESTest() throws Exception {
		
		ESignSanteSignatureReportWithProof report = new ESignSanteSignatureReportWithProof();
		report.setValide(true);
		report.setErreurs(new ArrayList<Erreur>());
				
		String reponsePSCActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
//		report.setDocSigne(Files.readAllBytes(
//				new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED_base64.txt").getFile().toPath()).toString());
//		
		report.setDocSigne("bm9uRWRpdGFibGU=");
		
		
		String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	assertTrue(userInfobase64.endsWith("TVEUwMDIxODg5In0="));
		
        	    	
    	MockMultipartFile filePDF = new MockMultipartFile(
    			"file",
    			"ANS.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));

    	
    	//intro PSC
    	Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
    	
    	 Mockito.doReturn(report).when(esignWS).signaturePades(any(File.class),any(List.class),any(),any(List.class) );
    		
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_PDF);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add("access_token", accessToken);
    	 
 
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(filePDF)
  				.file("userinfo", userInfobase64.getBytes())  				
  				.headers(httpHeaders))
  				.andExpect(status().isOk());
 				
    	 assertEquals("application/pdf", returned.andReturn().getResponse().getContentType());
    	
   	     			 
    			 //TODO contrôle de l'archivage
    			 
  		returned.andDo(document("signPADES/OK"));
               
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

    	String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	
    	 HttpHeaders httpHeaders = new HttpHeaders();
    	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
    	 acceptedMedia.add(MediaType.APPLICATION_JSON);
    	 acceptedMedia.add(MediaType.APPLICATION_XML);
    	 httpHeaders.setAccept(acceptedMedia);
    	 httpHeaders.add("access_token", accessToken);
    	 
    	
    	//intro PSC
    	Mockito.doReturn(reponsePSCNonActif).when(pscApi).isTokenActive(any());
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(fileXML)
  				.file("userinfo", userInfobase64.getBytes())
  				.header("access_token",accessToken)
                .accept("application/json,application/pdf"))
  				.andExpect(status().isBadRequest());
 				
  		returned.andDo(document("signPADES/TokenKo"));
               
	}

	@Test
	@DisplayName("ask PADES fichier non pdf")
	public void askPADESNotPDF() throws Exception {
		
		
		String reponsePSCActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	assertTrue(userInfobase64.endsWith("TVEUwMDIxODg5In0="));
		
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
    	 httpHeaders.add("access_token", accessToken);
    	 
    	
    	//intro PSC
    	Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
    	  	
    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")
  				.file(fileXML)
  				.file("userinfo", userInfobase64.getBytes())
  				.header("access_token",accessToken)
                .accept("application/json,application/pdf"))
  				.andExpect(status().isBadRequest());
 				
  		returned.andDo(document("signPADES/BadFileFormat"));
               
	}
}
