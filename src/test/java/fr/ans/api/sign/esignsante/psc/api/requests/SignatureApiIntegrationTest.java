package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.ans.api.sign.esignsante.psc.model.RequeteSignatureRecue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // pour restdocs
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("fr.ans.api.sign.esignsante.psc.api")

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
	
    private RequeteSignatureRecue requeteBody = new RequeteSignatureRecue();
    
    private final static String titreDocument = "signature";
    
    private final static String endPoint = "/signature/test";
    
    private String requestJson;
    
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    /**
     * Vérification de la liste des services disponibles.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Demande de signature Cas passant.")
    public void appelDemandeDeSignature_CasPassant_Test() throws Exception {

    	requeteBody.setToken("JeSuisUnToken");
    	requeteBody.setDocumentASigner("SignMePlease");
    	
        formatRequeteJson (requeteBody);


//        final MvcResult result; = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).contentType(APPLICATION_JSON_UTF8)
//                .content(requestJson)).andExpect(status().isOk()).andDo(print()).andReturn();

//     	  ResultActions returned = mockMvc.perform(get("/").accept("application/json"))
//        .andExpect(status().isOk()).andExpect(content().json(body));

        
        ResultActions returned= mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)).andExpect(status().isOk());
               
       
        assertTrue(true,"Code retour attendu : OK");
        assertTrue(true,"Blabla xxxxxxxxx");
        //assertTrue(retour.containsHeader("application/json"),"le type retrouné doit être application/json" );
        
      
    	  
    	  returned.andDo(print()); //pour debug console: a supprimer
    	  returned.andDo(document(titreDocument));
    	  System.err.println(returned.andReturn().toString());
    }
    
    
          @Test
    	  @DisplayName("Demande de signature KO: header accept non application/json.")
    	    public void appelDemandeDeSignature_HeaderKO_Test() throws Exception {
  	    	    	    	
        	  requeteBody.setToken("JeSuisUnToken");
          	requeteBody.setDocumentASigner("SignMePlease");
          	
              formatRequeteJson (requeteBody);
      	            
//        	  ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_OCTET_STREAM)
//        			  .contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotAcceptable());
//        	  
//                      assertTrue(true,"Code retour attendu : NotImplemented");
//    	    	  returned.andDo(print()); //pour debug console: a supprimer
//    	    	  returned.andDo(document(titreDocument));
              
              
        	  ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_OCTET_STREAM)
        			  .contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isNotAcceptable());
        	  
                      assertTrue(true,"Code retour attendu : NotImplemented");
    	    	  returned.andDo(print()); //pour debug console: a supprimer
    	    	  returned.andDo(document(titreDocument));  
    	    	  

    }
    	 
          
          @Test
    	  @DisplayName("Demande de signature KO: type de la charge incorrect.")
    	    public void appelDemandeDeSignature_MediaTypeCharge_KO_Test() throws Exception {
  	    	   // erreur 415
        	  MediaType badMediaType = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType());
        	  byte[] peuImporte = {1,2,3}; 
      	            
        	  ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_OCTET_STREAM)
                      .contentType(MediaType.APPLICATION_OCTET_STREAM).content(peuImporte)).andExpect(status().isUnsupportedMediaType());
        	  
                      assertTrue(true,"Code retour attendu : Unsupported Media Type (415)");
    	    	  returned.andDo(print()); //pour debug console: a supprimer
    	    	  returned.andDo(document(titreDocument));   	  
    }
    
          
    	  @Test
    	  @DisplayName("Demande de signature KO: token invalide")
  	    public void appelDemandeDeSignature_InvalidToken_Test() throws Exception {
	
    			requeteBody.setToken("nonValide");
    	    	requeteBody.setDocumentASigner("SignMePlease");
    	    
    	    	 formatRequeteJson (requeteBody);
    	    	 
//  	    	  ResultActions returned = mockMvc.perform(get("/").accept("application/json"))
//  	          .andExpect(status().isOk()).andExpect(content().json(body));
  	    	  
    	    	 ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.post(endPoint).accept(MediaType.APPLICATION_JSON).contentType(APPLICATION_JSON_UTF8)
    	                 .content(requestJson)).andExpect(status().isBadRequest());
    	    	 assertTrue(true,"Code retour attendu : BadRequest");
    	         assertTrue(true,"Blabla token invalide"); 
  	    	  returned.andDo(print()); //pour debug console: a supprimer
  	    	  returned.andDo(document(titreDocument));
    	  }  
 

    public void formatRequeteJson (RequeteSignatureRecue requeteBody) {
    	final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
			requestJson = ow.writeValueAsString(requeteBody);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return;
}
}
