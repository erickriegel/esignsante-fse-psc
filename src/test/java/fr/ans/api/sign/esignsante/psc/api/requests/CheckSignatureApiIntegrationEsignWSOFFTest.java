/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
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

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest // (properties = "file.encoding=UTF-8")
@AutoConfigureMockMvc

public class CheckSignatureApiIntegrationEsignWSOFFTest {

	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();
	}

	final String body = "{\"code\":\"503 SERVICE_UNAVAILABLE\",\"message\":\"Exception sur appel esignWS. Service inaccessible\"}";

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("CheckSignature XADES. Cas non passant esignWS OFF")
	public void checkSignXADES_esignWS_OFFTest() throws Exception {

		ResultActions returned = mockMvc
				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/xades")
						.file("file",
								Files.readAllBytes(
										new ClassPathResource("EsignSanteWS/Xades/signedpom.xml").getFile().toPath()))
						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable()).andExpect(content().json(body));

		returned.andDo(document("checkSignXADES/esginWS_OFF"));
	}

	@Test
	@DisplayName("CheckSignature PADES. Cas non passant esignWS OFF")
	public void checkSignPADES_esignWS_OFFTest() throws Exception {

		ResultActions returned = mockMvc
				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/pades")
						.file("file",
								Files.readAllBytes(
										new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED.pdf").getFile().toPath()))
						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable()).andExpect(content().json(body));

		returned.andDo(document("checkSignPADES/esginWS_OFF"));
	}

	 @Test
		@DisplayName("PADES  cas non passant. Header 'accept' non valide")
		public void checkPADESBadAcceptHeaderTest() throws Exception {


	    	MockMultipartFile fileSignedPDF = new MockMultipartFile(
	    			"file",
	    			"ANS_SIGNED.pdf",
	    			null,
					Files.readAllBytes(
							new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED.pdf").getFile().toPath()));

	    	
	    	ResultActions returned = mockMvc
	 				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/pades")
	 						.file(fileSignedPDF)				
	 						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_XML))
	 				.andExpect(status().isNotAcceptable());
	    	
			returned.andDo(document("checkSignPADES/KO_NotAcceptable"));
		}
	    

	    @Test
		@DisplayName("XADES  cas non passant. Header 'accept' non valide")
		public void checkXADESBadAcceptHeaderTest() throws Exception {

	    	MockMultipartFile fileSignedXML = new MockMultipartFile(
	    			"file",
	    			"signedipsfra.xml",
	    			null,
					Files.readAllBytes(
							new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));

	    	
	    	
	    	ResultActions returned = mockMvc
	 				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/xades")
	 						.file(fileSignedXML)				
	 						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_XML))
	 				.andExpect(status().isNotAcceptable());
	    	
			returned.andDo(document("checkSignXADES/KO_NotAcceptable"));
		}
	    
	    
	  

}
