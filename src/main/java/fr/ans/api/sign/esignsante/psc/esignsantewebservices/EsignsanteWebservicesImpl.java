package fr.ans.api.sign.esignsante.psc.esignsantewebservices;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EsignsanteWebservicesImpl implements EsignsanteWebServices {
	
	
	String basePath;
	
	public ResponseEntity<String> getCa() throws URISyntaxException {
	RestTemplate restTemplate = new RestTemplate();
	log.error("basepath du fichier de conf");
	//String url="https://auth.bas.esw.esante.gouv.fr/auth/realms/esante-wallet/protocol/openid-connect/token/introspect";
	String urles="http://localhost:8081/esignsante/v1";
	URI uri = new URI(urles + "/ca");
	log.error("avant appel");
	ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
	log.error("result: " + result);
	return result;
	//URI uri = new URI(urles);
	
	
	
	//HttpHeaders headers = new HttpHeaders();
	//ResponseEntity 
	/*
	 * 
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
	    return builder
	            .setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000))
	         	            .build();
	}
	*/
	
	/*
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
	    int timeout = 5000;
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
	      = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(timeout);
	    return clientHttpRequestFactory;
	}
	*/
	
	
	
	/*WebClient webClient = WebClient.builder()
        .baseUrl(GITHUB_API_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_V3_MIME_TYPE)
        .filter(ExchangeFilterFunctions
                .basicAuthentication(username, token))
        .filter(logRequest())
        .build();*/
}
}
