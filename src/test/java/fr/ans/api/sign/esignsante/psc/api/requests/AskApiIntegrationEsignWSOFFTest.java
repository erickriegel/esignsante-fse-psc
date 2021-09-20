/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.reactive.server.HeaderAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc

public class AskApiIntegrationEsignWSOFFTest {

	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
	}

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	final String body = "{\"code\":\"503 SERVICE_UNAVAILABLE\",\"message\":\"Exception sur appel esignWS. Service inaccessible\"}";
	final String accessToken = "accessTokenValue";
	
	@MockBean
	ProsanteConnectCalls pscApi;
	
	@Test
	@DisplayName("Ask signature XADES Cas non passant esignWS OFF")
	public void askSignXADES_esignWS_OFFTest() throws Exception {

		String reponsePSCActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
		
		MockMultipartFile fileXML = new MockMultipartFile(
    			"file",
    			"ipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));

		final String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	
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
				.andExpect(status().isServiceUnavailable()).andExpect(content().json(body));

		assertEquals(returned.andReturn().getResponse().getContentType(), "application/json");	
		returned.andDo(document("signXADES/esginWS_OFF")); // RestDcos
	}

	
	@Test
	@DisplayName("Ask signature PADES Cas non passant esignWS OFF")
	public void askSignPADES_esignWS_OFFTest() throws Exception {

		String reponsePSCActif = Files.readString(
				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		MockMultipartFile filePDF = new MockMultipartFile(
    			"file",
    			"ANS.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));

		final String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    	
		Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
		
		
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
				.andExpect(status().isServiceUnavailable());
				
		assertEquals(returned.andReturn().getResponse().getContentType(), "application/json");	
		
				returned.andExpect(content().json(body));
				
//String result = returned.andReturn().getResponse().getContentAsString();
 
		returned.andDo(document("signPADES/esginWS_OFF")); 
	}
}
