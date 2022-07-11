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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.HeaderAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.ans.api.sign.esignsante.psc.api.delegate.AbstractApiDelegate;
import fr.ans.api.sign.esignsante.psc.utils.Helper;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) // pour restdocs
@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test-withgravitee")
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
	final String accessToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2NTcyNjg2NDYsImlhdCI6MTY1NzI2ODU4NiwiYXV0aF90aW1lIjoxNjU3MjY4NTg2LCJqdGkiOiI2ZmY0OWMwNi1kNWM4LTQxYTgtYTZjZS03MjZmMWJiNGQzY2IiLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjQ1NjY3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiODcxNmQ1OTktMWJjYS00ODU4LThkMjAtZGQ2YzhhMzNmYTYzIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBzY29wZV9hbGwiLCJzaWQiOiI4NzE2ZDU5OS0xYmNhLTQ4NTgtOGQyMC1kZDZjOGEzM2ZhNjMiLCJhY3IiOiJlaWRhczMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6Ijg5OTcwMDI0NTY2NyJ9.izsTSHGd_jkrRtY2VplyiyZwlUQ8E4WRS1mjcaDJPXfRa_UB36ekgUcz2-5a69lOXRI1Y3Xre3KfZ-G2rgYlA2UN0NOOzR0mAfELwzJToi2f0hEo_2WkqALxu9fceDHGPfdWypUIBjnY0v8NlGi4goCTFtWFXKFyarec9-qgPOwi_D9GcqP_GQylehH1bCiBNL0TImR7-73nlbMrPEtMUtqzr58S5G2eTirk9NBcziCepDovprrmIQ8ktuJW73DlHoq90ocRkBYXCtmTkdFlOjJ_GLwYhC9UONShB5iiIPCMK2ZrZ1RSD0ooEOuP4HPlsBC6rJ_oHJs4c2JoYXEY8A";
	
	@Test
	@DisplayName("Ask signature XADES Cas non passant esignWS OFF")
	public void askSignXADES_esignWS_OFFTest() throws Exception {

//		String reponsePSCActif = Files.readString(
//				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());		
		
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
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
    	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_USERINFO,userInfobase64 );
       	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_TOKEN_VALIDATIONRESPONSE, "forwardedtokenValityResponse" );
		ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/xades")				
				.file(fileXML)
  			//	.file("userinfo", userInfobase64.getBytes())
  				.headers(httpHeaders))
				.andExpect(status().isServiceUnavailable()).andExpect(content().json(body));

		assertEquals("application/json",returned.andReturn().getResponse().getContentType());	
		returned.andDo(document("signXADES/esginWS_OFF")); // RestDcos
	}

	
	@Test
	@DisplayName("Ask signature PADES Cas non passant esignWS OFF")
	public void askSignPADES_esignWS_OFFTest() throws Exception {

//		String reponsePSCActif = Files.readString(
//				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
		
		MockMultipartFile filePDF = new MockMultipartFile(
    			"file",
    			"ANS.pdf",
    			null,
				Files.readAllBytes(
						new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));

		final String userInfobase64 = Files.readString(
				new ClassPathResource("PSC/UserInfo_medecin_899700218896_UTF8_Base64.txt").getFile().toPath());
    			
		
   	 HttpHeaders httpHeaders = new HttpHeaders();
   	 List<MediaType> acceptedMedia = new ArrayList<MediaType>();
   	 acceptedMedia.add(MediaType.APPLICATION_JSON);
   	 acceptedMedia.add(MediaType.APPLICATION_PDF);
   	 httpHeaders.setAccept(acceptedMedia);
   	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_AUTHORIZATION, accessToken);
   	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_USERINFO,userInfobase64 );
   	 httpHeaders.add(AbstractApiDelegate.HEADER_NAME_TOKEN_VALIDATIONRESPONSE, "forwardedtokenValityResponse" );
		ResultActions returned = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/asksignature/pades")				
				.file(filePDF)
  			//	.file("userinfo", userInfobase64.getBytes())
  				.headers(httpHeaders))
				.andExpect(status().isServiceUnavailable());
				
		assertEquals("application/json",returned.andReturn().getResponse().getContentType());	
		
				returned.andExpect(content().json(body));
				
String result = returned.andReturn().getResponse().getContentAsString();
 
		returned.andDo(document("signPADES/esginWS_OFF")); 
	}
}
