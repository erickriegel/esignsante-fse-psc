/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

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

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc
public class CheckSignatureApiIntegrationBadFileTest {
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
    
    
	  @Test
			@DisplayName("XADES  cas non passant. Erreur sur le type de fichier")
			public void checkXADESBadFileFormatTest() throws Exception {

		    	MockMultipartFile fileSignedPDF = new MockMultipartFile(
		    			"file",
		    			"ANS_SIGNED.pdf",
		    			null,
						Files.readAllBytes(
								new ClassPathResource("EsignSanteWS/Pades/ANS_SIGNED.pdf").getFile().toPath()));

		    	
		    	HttpHeaders httpHeaders = new HttpHeaders();

		    	Mockito.doThrow(
		    	    	HttpServerErrorException.create(HttpStatus.NOT_IMPLEMENTED,"501", httpHeaders,"[no body]".getBytes(),null )).when(esignWS)
				 .chekSignatureXades(any(File.class));
		    	
		    	ResultActions returned = mockMvc
		 				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/xades")
		 						.file(fileSignedPDF)				
		 						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
		 				.andExpect(status().isNotImplemented());
		    	
				returned.andDo(document("checkSignXADES/BadFileFormat"));
			}
	  
	  
	  @Test
		@DisplayName("PADES  cas non passant. Erreur sur le type de fichier")
		public void checkPADESBadFileFormatTest() throws Exception {

	    	MockMultipartFile fileSignedPDF = new MockMultipartFile(
	    			"file",
	    			"signedipsfra.xml",
	    			null,
					Files.readAllBytes(
							new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));

	    	
	    	HttpHeaders httpHeaders = new HttpHeaders();

	    	Mockito.doThrow(
	    	    	HttpServerErrorException.create(HttpStatus.NOT_IMPLEMENTED,"501", httpHeaders,"[no body]".getBytes(),null )).when(esignWS)
			 .chekSignaturePades(any(File.class));
	    	
	    	ResultActions returned = mockMvc
	 				.perform(MockMvcRequestBuilders.multipart("/v1/checksignature/pades")
	 						.file(fileSignedPDF)				
	 						.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON))
	 				.andExpect(status().isNotImplemented());
	    	
			returned.andDo(document("checkSignPADES/BadFileFormat"));
		}
}
