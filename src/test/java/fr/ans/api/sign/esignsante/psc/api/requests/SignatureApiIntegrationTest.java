package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.stream.Collectors;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import lombok.extern.slf4j.Slf4j;

//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import fr.ans.api.sign.esignsante.psc.model.RequeteSignatureRecue;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.maskLinks;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.replacePattern;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // pour restdocs
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
//@ComponentScan("fr.ans.api.sign.esignsante.psc.api")
@Slf4j
public class SignatureApiIntegrationTest {
	
     

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
	
    
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    /**
     * Demande de signature cas passant
     *
     * @throws Exception the exception
     */
    //@Test
    @DisplayName("Demande de signature Cas passant.")
    public void appelDemandeDeSignature_CasPassant_Test() throws Exception {

    	
//    	requeteBody.setToken("Ceci est Token valide");
//    	requeteBody.setDocumentASigner("Ceci est le document à signer");
//    	
//        formatRequeteJson (requeteBody);
//        
//        ResultActions returned= mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON_UTF8)
//                .content(requestJson)).andExpect(status().isOk());
//               
//   	  
//    	  returned.andDo(print()); //pour debug console: a supprimer
//    	  returned.andDo(document(titreDocument +"/OK"));

    }
    
    
          @Test
    	  @DisplayName("Demande de signature KO: userINfo non en base 64")
    	    public void demandeDeSignature_UserInfoNonbase64Test() throws Exception {
  	    

       		MockMultipartFile filemXml = new MockMultipartFile("file", "pom.xml", null,
        	             Thread.currentThread().getContextClassLoader().getResourceAsStream("pomxx.xml"));
       		InputStream data = Thread.currentThread().getContextClassLoader().getResourceAsStream("EsignSanteWS/Xades/pom.xml");

//       		log.error("ici : " + new BufferedReader(new InputStreamReader(data))
//       			    .lines().parallel().collect(Collectors.joining("\n")));

        	 
    	    	  

    }
    	 
          
        //  @Test
    	  @DisplayName("Demande de signature KO: type de la charge incorrecte.")
    	    public void appelDemandeDeSignature_MediaTypeCharge_KO_Test() throws Exception {
//  	    	   // erreur 415
//        	  MediaType badMediaType = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType());
//        	  byte[] peuImporte = {1,2,3}; 
//      	            
//        	  ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_OCTET_STREAM)
//                      .contentType(MediaType.APPLICATION_OCTET_STREAM).content(peuImporte)).andExpect(status().isUnsupportedMediaType());
//        	  
//    	    	  returned.andDo(print()); //pour debug console: a supprimer
//    	    	  returned.andDo(document(titreDocument+"/StreamKo"));   	  
    }
    
          
    	  //@Test
    	  @DisplayName("Demande de signature KO: token invalide")
  	    public void appelDemandeDeSignature_InvalidToken_Test() throws Exception {
	
//    			requeteBody.setToken("nonValide");
//    	    	requeteBody.setDocumentASigner("Ceci est le document à signer (UTF8)");
//    	    
//    	    	 formatRequeteJson (requeteBody);
//  	    	  
//    	    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON_UTF8)
//    	                 .content(requestJson)).andExpect(status().isBadRequest());
//  	    	  returned.andDo(print()); //pour debug console: a supprimer
//  	    	  returned.andDo(document(titreDocument+"/TokenKo"));
    	  }  
 
    	  
    	    //@Test   	    
    	    public void test_Restdoc_CasPassant_Test() throws Exception {

//    	    	requeteBody.setToken("TokenPSC valide");
//    	    	requeteBody.setDocumentASigner("DocumentNonsigne");
//    	    	
//    	        formatRequeteJson (requeteBody);
//
//    	       
//   	           	    	  
//    	    	  this.mockMvc.perform(RestDocumentationRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON_UTF8)
//      	                .content(requestJson)).andExpect(status().isOk())
//    	          .andExpect(status().isOk())
//    	    	  .andDo(document(titreDocument +"/OK2"));   	    	  
    	    			  
//    	    	  requestParameters(
//    	    					    parameterWithName("token")
//    	    					        .description("token fourni par PSC String xxxxxxxx..."),
//    	    					    parameterWithName("documentASigner").description("Feuille de soin à signer. Formats possibles: string, pdf,..")));
    	    	  
    	    	  
//    	    	  this.mockMvc.perform(RestDocumentationRequestBuilders
//    	    			    .post(endPoint)
//    	    			    .param("token", "abcdefg")
//    	    			    .param("documentASigner", "azerty"));
    	    	  
//    	    	  responseFields( 
//    	    			  fieldWithPath("idMongoDB")
//    								.description("id de la preuve stockée dans MongoDB <balise></balise> Caractères spéciaux:& é \" $ ù ç @"), 
//    				      fieldWithPath("documentSigne").description("Document signé par esignsante avec les 2 signatures. Base 64.")); 
    	    }
    	    
    	  

//    public void formatRequeteJson (RequeteSignatureRecue requeteBody) {
//    	final ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        try {
//			requestJson = ow.writeValueAsString(requeteBody);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        return;
//}
}
