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
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // pour restdocs
//@ContextConfiguration
@SpringBootTest
//@DataMongoTest
@AutoConfigureMockMvc
//@ComponentScan("fr.ans.api.sign.esignsante.psc.api")

public class DefaultApiIntegrationTest {

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
	
//    @MockBean
//    private MongoTemplate mongoTemplate;

    /**
     * Vérification de la liste des services disponibles.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Vérification de la liste des services disponibles.")
    public void rootGetTest() throws Exception {
   
    	final String body =  "[{\"path\":\"/ca\",\"description\":\"Opération qui permet au client de prendre connaissance des Autorités de Certification de confiance utilisées par la plateforme.\",\"payload\":\"\",\"requiredHeaders\":\"\"},{\"path\":\"/asksignature/xades\",\"description\":\"Opération qui permet au client de demander de signer un document au format XADES Baseline-B.\",\"payload\":\"\",\"requiredHeaders\":\"Access_token, accept:json, usertoken\"},{\"path\":\"/\",\"description\":\"Liste des opérations disponibles\",\"payload\":\"\",\"requiredHeaders\":\"\"}]";
    	
       	    
    	
    	ResultActions returned = mockMvc.perform(get("/v1/").accept("application/json")
    			.contentType(MediaType.APPLICATION_JSON))
        	    .andExpect(status().isOk()).andExpect(content().json(body));
    	
        // .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8).json(body));
    	  returned.andDo(print()); //pour debug console: a supprimer
    	  returned.andDo(document("Liste_des_services/OK"));
    	  

    }       
}
