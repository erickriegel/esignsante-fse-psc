/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest // (properties = "file.encoding=UTF-8")
@AutoConfigureMockMvc

public class CAApiIntegrationTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

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

	@MockBean
	EsignsanteCall esignWS;

	/**
	 * Demande de la liste des CAs.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@DisplayName("Demande de la liste des CAs.")
	public void caGetTest() throws Exception {

		caGet("application/json");
		
		caGet("*/*");
		
		caGet(" application/xml , application/json  ");
	}
	
	
	@Test
	@DisplayName("Header wildard Bis")
	public void caGetHeaderWildcardBisTest() throws Exception {

		List<String> listeCA = new ArrayList<String>();
		listeCA.add(
				"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");
		listeCA.add("CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");

		final String body = "[\"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\",\"CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\"]";
		Mockito.doReturn(listeCA).when(esignWS).getCA();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("accept", "application/xml");
		httpHeaders.add("accept", "*/*");

		ResultActions returned = mockMvc.perform(get("/v1/ca")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andExpect(content().json(body));

		returned.andDo(document("CA/OK")); // RestDcos
	}
	
	

	
	@Test
	@DisplayName("Header wildard 3")
	public void caGetHeaderWildcardTerTest() throws Exception {

		List<String> listeCA = new ArrayList<String>();
		listeCA.add(
				"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");
		listeCA.add("CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");

		final String body = "[\"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\",\"CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\"]";
		Mockito.doReturn(listeCA).when(esignWS).getCA();

		ResultActions returned = mockMvc.perform(get("/v1/ca").accept("application/xml").accept("application/*")).andExpect(status().isOk())
				.andExpect(content().json(body));

		returned.andDo(document("CA/OK")); // RestDcos
	}

	@Test
	@DisplayName("Header wildard 4")
	public void caGetHeaderWildcardQuatroTest() throws Exception {

		List<String> listeCA = new ArrayList<String>();
		listeCA.add(
				"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");
		listeCA.add("CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");

		final String body = "[\"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\",\"CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\"]";
		Mockito.doReturn(listeCA).when(esignWS).getCA();

		ResultActions returned = mockMvc.perform(get("/v1/ca").accept("application/xml").accept("application/json")).andExpect(status().isOk())
				.andExpect(content().json(body));

		returned.andDo(document("CA/OK")); // RestDcos
	}

	
	@Test
	@DisplayName("/ca: cas non passant. Header 'accept' non valide")
	public void rootGetBadAcceptHeaderTest() throws Exception {

		ResultActions returned = mockMvc.perform(get("/v1/ca").accept(MediaType.APPLICATION_XML))
				.andExpect(status().isNotAcceptable());
		returned.andDo(document("CA/KO_NotAcceptable"));
	}
	
	 private void caGetWithRestDoc(String acceptHeader) throws Exception {
		 ResultActions returned = caGet(acceptHeader);
		 returned.andDo(document("CA/OK")); // RestDcos
	 }
	private ResultActions caGet(String acceptHeader) throws Exception {

		List<String> listeCA = new ArrayList<String>();
		listeCA.add(
				"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");
		listeCA.add("CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR");

		final String body = "[\"CN=TEST AC IGC-SANTE ELEMENTAIRE ORGANISATIONS,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\",\"CN=TEST AC RACINE IGC-SANTE ELEMENTAIRE,OU=IGC-SANTE TEST,OU=0002 187512751,O=ASIP-SANTE,C=FR\"]";
		Mockito.doReturn(listeCA).when(esignWS).getCA();

		ResultActions returned = mockMvc.perform(get("/v1/ca").accept(acceptHeader)).andExpect(status().isOk())
				.andExpect(content().json(body));

		return returned;
		
	}

}
