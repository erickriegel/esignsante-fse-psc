package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
////import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
//
//
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
//@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})// pour restdocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})// pour restdocs

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc // voir si pas à supprimer ...
@ComponentScan("fr.ans.api.sign.esignsante.psc.api")

public class DefaultApiIntegrationBisTest {

	  /** The mock mvc. */
    @Autowired
    private MockMvc mockMvc;
	
	/*
	 * setUp pour restdocs
	 */
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
	  RestDocumentationContextProvider restDocumentation) {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
	      .apply(documentationConfiguration(restDocumentation)).build();
	}
	
  
	
    private static final Snippet RESPONSE_HEADERS_STATUS_OK = HeaderDocumentation.responseHeaders(headerWithName("Status").description("status de la réponse 200"));

    /**
     * Vérification de la liste des services disponibles.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Vérification de la liste des services disponibles.")
    public void rootGetBisTest() throws Exception {
    	//final String body = "[\"/\"]";
    	final String body = "[\"/\",\"/signature/test\"]";
    	
    	
//        mockMvc.perform(get("/").accept("application/json"))
//                .andExpect(status().isOk()).andExpect(content().json(body)).andDo(print());
    	
    	 Snippet responseFields = responseFields(
                 fieldWithPath("array").type(STRING).description("Liste"));
//    	
//    	  ResultActions returned = mockMvc.perform(get("/").accept("application/json"))
//          .andExpect(status().isOk()).andExpect(content().json(body));
    	  
//    	returned.andDo(print()); //pour debug console: a supprimer
//    	 returned.andDo(document("Liste_des_services"));
    	  
    	 
//  ******************************************* ICI    	 
//    	  mockMvc.perform(get("/").accept("application/json"))
//          .andExpect(status().isOk()).andDo(document("Liste_des_services"),
//    			  preprocessResponse(prettyPrint()),
//    	    	  requestHeaders(headerWithName("accept").description(" doit être application/json")),
//    	    	  null, //responseFields,
//    	    	  RESPONSE_HEADERS_STATUS_OK);
//    	    	  
//    	 ******************************************* FIN ICI
    	    		    
//    			  responseFields(beneathPath("weather.temperature"), 
//  						fieldWithPath("high").description(
//  								"The forecast high in degrees celcius") );
//    	  mockMvc.perform(get("/").accept("application/json"))
//    	          .andExpect(status().isOk()).andExpect(content().json(body));.andDo(document("Liste_des_services", preprocessResponse(prettyPrint()),
//    	  requestHeaders(
//    		        headerWithName("accept").description(" doit être application/json");
//    		    
    }
    
}
