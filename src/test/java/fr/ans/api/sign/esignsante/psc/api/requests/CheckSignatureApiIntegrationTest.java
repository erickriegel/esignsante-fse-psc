/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.charset.StandardCharsets;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.Erreur;


/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // pour restdocs
@SpringBootTest //(properties = "file.encoding=UTF-8")
@AutoConfigureMockMvc

public class CheckSignatureApiIntegrationTest {

	 private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
	            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
	
	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
	  RestDocumentationContextProvider restDocumentation) {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
	      .apply(documentationConfiguration(restDocumentation)).build();
	   
	   
	}
	
    /** The mock mvc. */
    @Autowired
    private MockMvc mockMvc;
	    
    @MockBean
    EsignsanteCall esignWS;
    
    final String bodyOK = "{\"valid\":true,\"errors\":[]}";

    
    ESignSanteValidationReport report;
    List<Erreur> erreurs;
    
    @Test
	@DisplayName("CheckSignature XADES. Cas passant")
	public void checkSignXADES_OKTest() throws Exception {

    	
    	report = new ESignSanteValidationReport();
    	report.setValide(true);    	
    	report.setErreurs(new ArrayList<Erreur>());
   
    	
    	MockMultipartFile fileSignedXML = new MockMultipartFile(
    			"file",
    			"signedipsfra.xml",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));

    	//TODO argumentMatcher sur le début du nom de fichier
    	 Mockito.doReturn(report).when(esignWS).chekSignatureXades(any(File.class));
    		
  		ResultActions returned = mockMvc
				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/xades")
						.file(fileSignedXML)				
						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(bodyOK));
		
		returned.andDo(document("checkSignXADES/OK")); // RestDcos
	}

    @Test
	@DisplayName("CheckSignature PADES. Cas passant")
	public void checkSignPADES_OKTest() throws Exception {

    	
    	report = new ESignSanteValidationReport();
    	report.setValide(true);    	
    	report.setErreurs(new ArrayList<Erreur>());
   
    	
    	MockMultipartFile fileSignedPDF = new MockMultipartFile(
    			"file",
    			"ANS_SIGNED.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED.pdf").getFile().toPath()));

    	//TODO argumentMatcher sur le début du nom de fichier
    	 Mockito.doReturn(report).when(esignWS).chekSignaturePades(any(File.class));
  		
   		ResultActions returned = mockMvc
 				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/pades")
 						.file(fileSignedPDF)				
 						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
 				.andExpect(status().isOk()).andExpect(content().json(bodyOK));
		
		returned.andDo(document("checkSignPADES/OK")); 
	}

  
   
}
