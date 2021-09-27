/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultApiIntegrationTest {

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

	/**
	 * Vérification de la liste des services disponibles.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@DisplayName("Cas passant. Vérification de la liste des services disponibles.")
	public void rootGetTest() throws Exception {
		
		final String body= "[{\"path\":\"/ca\",\"description\":\"Opération qui permet au client de prendre connaissance des Autorités de Certification de confiance utilisées par la plateforme.\",\"payload\":\"\",\"requiredHeaders\":\"accept:application/json\"},{\"path\":\"/checksignature/xades\",\"description\":\"Opération qui permet de vérifier un document signé au format PADES Baseline-B.\",\"payload\":\"file:sigendPadesDoc\",\"requiredHeaders\":\"accept:application/json\"},{\"path\":\"/asksignature/xades\",\"description\":\"Opération qui permet au client de demander de signer un document au format XADES Baseline-B.\",\"payload\":\"[\\\"file: docXMLToSign\\\", \\\"userToken:userToken\\\"]\",\"requiredHeaders\":\"[\\\"accept:application/json\\\", \\\"accept:application/json\\\", \\\"access_token: PSCVAlidAccessToken\\\"]\"},{\"path\":\"/checksignature/xades\",\"description\":\"Opération qui permet de vérifier un document signé au format XADES Baseline-B.\",\"payload\":\"file:sigendXadesDoc\",\"requiredHeaders\":\"accept:application/json\"},{\"path\":\"/asksignature/pades\",\"description\":\"Opération qui permet au client de demander de signer un document au format PADES Baseline-B.\",\"payload\":\"[\\\"file: docXMLToSign\\\", \\\"userToken:userToken\\\"]\",\"requiredHeaders\":\"[\\\"accept:application/json\\\", \\\"accept:application/json\\\", \\\"access_token: PSCVAlidAccessToken\\\"]\"},{\"path\":\"/\",\"description\":\"Liste des opérations disponibles\",\"payload\":\"\",\"requiredHeaders\":\"accept:application/json\"}]";
		ResultActions returned = mockMvc.perform(get("/v1/").accept("application/json")).andExpect(status().isOk());
		returned.andDo(print());
		returned.andExpect(content().json(body));

		returned.andDo(print()); // pour debug console
		returned.andDo(document("Liste_des_services/OK"));
	}

	@Test
	@DisplayName("/: cas non passant. Header 'accept' non valide")
	public void rootGetTestBadAcceptHeader() throws Exception {

		ResultActions returned = mockMvc.perform(get("/v1/").accept(MediaType.APPLICATION_XML))
				.andExpect(status().isNotAcceptable());

		returned.andDo(document("Liste_des_services/KO_NotAcceptable"));

		MvcResult res = mockMvc.perform(get("/v1/").accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(1 , res.getResponse().getHeaderValues("Content-Type").size());
		System.out.println("MediaType.APPLICATION_JSON.getType(): " + MediaType.APPLICATION_JSON.getType());
		System.out.println("getResponse().getHeaderValues" + res.getResponse().getHeaderValues("Content-Type").get(0));
		assertEquals("application/json", res.getResponse().getHeaderValues("Content-Type").get(0));

	}

	@Test
	@DisplayName("Multiple Headers 'accept'")
	public void rootGetTestMultipledAcceptHeader() throws Exception {

		ResultActions returned = mockMvc.perform(get("/v1/").accept("application/xml").accept("application/json"))
				.andExpect(status().isOk());
		returned.andDo(document("Liste_des_services/OK_BIS"));
	}

}
