package fr.ans.api.sign.esignsante.psc.prosanteconnect.call;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootTest
public class ProSanteConnectCallTest {

	@Autowired
	ProsanteConnectCalls pscApi;

	@Test
	public void introspectionPSCTestTokenNonActif() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		final String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2Mjg3ODE1NTUsImlhdCI6MTYyODc4MTQ5NSwiYXV0aF90aW1lIjoxNjI4NzgxNDk0LCJqdGkiOiI0MzkyNmNmMS0zZTExLTQ4MTItOWE2ZS1hNjMxYjk0ZjZhZDciLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjE4ODk2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiMDdhOWRmMDYtNjAxMy00YWRhLWE2YWMtOTU0NDRmN2IwOWUxIiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgc2NvcGVfYWxsIiwiYWNyIjoiZWlkYXMzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiI4OTk3MDAyMTg4OTYifQ.Va0NR0lKcyZgRD3tUUb-BpzKYGUCh0gvOFbt-_c9w1ociNkG2hJTo6XxgAMEOvcpM4aVPedyhIYRyKAOaEsICH5VPVP0zvHvbtoatkFRPH_Zro05jkhvsW8X4XTuY06EJdHxUiMRZC_AqclQ1QIR5vc-0XBWFPW9vt5Q-qOqqTPLWrnDxqdVqBfwkVoKlh1Jnlv6vilSqBIsQbs_76dN8T0cChzmtk0kCJcpnXyTy__PLDLpgHITDGnLONwbaP0ofxEjTZ9OgISWQCd5GKWiv5iZrebka0Dvbjkooqt5DhIlGi30l2vLs7-eowrcxsfJGgwzJafppga0nIvBiQ83oA";
		
	
		String result = pscApi.isTokenActive(token);
		assertEquals(result,"{\"active\":false}");
	}

		
		//@Test
		public void introspectionPSCTestTokenActif() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
			//TODO MOCK reponse PSC or bypass?
		//final String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2Mjg3ODE1NTUsImlhdCI6MTYyODc4MTQ5NSwiYXV0aF90aW1lIjoxNjI4NzgxNDk0LCJqdGkiOiI0MzkyNmNmMS0zZTExLTQ4MTItOWE2ZS1hNjMxYjk0ZjZhZDciLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjE4ODk2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiMDdhOWRmMDYtNjAxMy00YWRhLWE2YWMtOTU0NDRmN2IwOWUxIiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgc2NvcGVfYWxsIiwiYWNyIjoiZWlkYXMzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiI4OTk3MDAyMTg4OTYifQ.Va0NR0lKcyZgRD3tUUb-BpzKYGUCh0gvOFbt-_c9w1ociNkG2hJTo6XxgAMEOvcpM4aVPedyhIYRyKAOaEsICH5VPVP0zvHvbtoatkFRPH_Zro05jkhvsW8X4XTuY06EJdHxUiMRZC_AqclQ1QIR5vc-0XBWFPW9vt5Q-qOqqTPLWrnDxqdVqBfwkVoKlh1Jnlv6vilSqBIsQbs_76dN8T0cChzmtk0kCJcpnXyTy__PLDLpgHITDGnLONwbaP0ofxEjTZ9OgISWQCd5GKWiv5iZrebka0Dvbjkooqt5DhIlGi30l2vLs7-eowrcxsfJGgwzJafppga0nIvBiQ83oA";
			//String result = pscApi.isTokenActive(token);
			//String result = 
			//assertEquals(result,"{\"active\"}");
		
		}

		
//		@Test
//		public void introspectionPSCFormatFormatReponseNonConforme() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
//			//TODO MOCK reponse PSC or bypass?
//		//final String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2Mjg3ODE1NTUsImlhdCI6MTYyODc4MTQ5NSwiYXV0aF90aW1lIjoxNjI4NzgxNDk0LCJqdGkiOiI0MzkyNmNmMS0zZTExLTQ4MTItOWE2ZS1hNjMxYjk0ZjZhZDciLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjE4ODk2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiMDdhOWRmMDYtNjAxMy00YWRhLWE2YWMtOTU0NDRmN2IwOWUxIiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgc2NvcGVfYWxsIiwiYWNyIjoiZWlkYXMzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiI4OTk3MDAyMTg4OTYifQ.Va0NR0lKcyZgRD3tUUb-BpzKYGUCh0gvOFbt-_c9w1ociNkG2hJTo6XxgAMEOvcpM4aVPedyhIYRyKAOaEsICH5VPVP0zvHvbtoatkFRPH_Zro05jkhvsW8X4XTuY06EJdHxUiMRZC_AqclQ1QIR5vc-0XBWFPW9vt5Q-qOqqTPLWrnDxqdVqBfwkVoKlh1Jnlv6vilSqBIsQbs_76dN8T0cChzmtk0kCJcpnXyTy__PLDLpgHITDGnLONwbaP0ofxEjTZ9OgISWQCd5GKWiv5iZrebka0Dvbjkooqt5DhIlGi30l2vLs7-eowrcxsfJGgwzJafppga0nIvBiQ83oA";
//			//String result = pscApi.isTokenActive(token);
//			String result = "nonjson";
//			
//			result = {"exp":1628250495,"iat":1628250435,"auth_time":1628250434,"jti":"0cc00f99-3f1b-4799-9222-a944ca82c310","iss":"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet","sub":"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896","typ":"Bearer","azp":"ans-poc-bas-psc","nonce":"","session_state":"e0e82435-fef5-430b-9b31-187fe3b0ffe6","preferred_username":"899700218896","email_verified":false,"acr":"eidas3","scope":"openid profile email scope_all","client_id":"ans-poc-bas-psc","username":"899700218896","active":true}
//			//assertEquals(result,"{\"active\"}");
//		
//		}

//		@Test
//		public void introspectionPSCFormatFormatReponseConforme() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
//			//TODO MOCK reponse PSC or bypass?
//		//final String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjaDNLZkFqOXZhX2tUZ2xGY01tTWlQVXZaSkNyU2l0NXZyeGVfZVgzbWpNIn0.eyJleHAiOjE2Mjg3ODE1NTUsImlhdCI6MTYyODc4MTQ5NSwiYXV0aF90aW1lIjoxNjI4NzgxNDk0LCJqdGkiOiI0MzkyNmNmMS0zZTExLTQ4MTItOWE2ZS1hNjMxYjk0ZjZhZDciLCJpc3MiOiJodHRwczovL2F1dGguYmFzLmVzdy5lc2FudGUuZ291di5mci9hdXRoL3JlYWxtcy9lc2FudGUtd2FsbGV0Iiwic3ViIjoiZjo1NTBkYzFjOC1kOTdiLTRiMWUtYWM4Yy04ZWI0NDcxY2Y5ZGQ6ODk5NzAwMjE4ODk2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYW5zLXBvYy1iYXMtcHNjIiwibm9uY2UiOiIiLCJzZXNzaW9uX3N0YXRlIjoiMDdhOWRmMDYtNjAxMy00YWRhLWE2YWMtOTU0NDRmN2IwOWUxIiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgc2NvcGVfYWxsIiwiYWNyIjoiZWlkYXMzIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiI4OTk3MDAyMTg4OTYifQ.Va0NR0lKcyZgRD3tUUb-BpzKYGUCh0gvOFbt-_c9w1ociNkG2hJTo6XxgAMEOvcpM4aVPedyhIYRyKAOaEsICH5VPVP0zvHvbtoatkFRPH_Zro05jkhvsW8X4XTuY06EJdHxUiMRZC_AqclQ1QIR5vc-0XBWFPW9vt5Q-qOqqTPLWrnDxqdVqBfwkVoKlh1Jnlv6vilSqBIsQbs_76dN8T0cChzmtk0kCJcpnXyTy__PLDLpgHITDGnLONwbaP0ofxEjTZ9OgISWQCd5GKWiv5iZrebka0Dvbjkooqt5DhIlGi30l2vLs7-eowrcxsfJGgwzJafppga0nIvBiQ83oA";
//			//String result = pscApi.isTokenActive(token);
//			String result = "{\"exp\":1628250495,\"iat\":1628250435,\"auth_time\":1628250434,\"jti\":\"0cc00f99-3f1b-4799-9222-a944ca82c310\",\"iss\":\"https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet\",\"sub\":\"f:550dc1c8-d97b-4b1e-ac8c-8eb4471cf9dd:899700218896\",\"typ\":\"Bearer\",\"azp\":\"ans-poc-bas-psc\",\"nonce\":\"\",\"session_state\":\"e0e82435-fef5-430b-9b31-187fe3b0ffe6\",\"preferred_username\":\"899700218896\",\"email_verified\":false,\"acr\":\"eidas3\",\"scope\":\"openid profile email scope_all\",\"client_id\":\"ans-poc-bas-psc\",\"username\":\"899700218896\",\"active\":true}";
//			//assertEquals(result,"{\"active\"}");
//		
//		}
		
			//@Test
			//public void introspectionPSCURLNonvalide() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
				
			//}

		
		//@Test
		//public void introspectionPSCServiceInaccessible() throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
			
		//}
	}

