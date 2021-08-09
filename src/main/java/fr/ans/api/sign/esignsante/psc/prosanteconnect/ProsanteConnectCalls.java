package fr.ans.api.sign.esignsante.psc.prosanteconnect;

//import java.net.http.HttpClient;
//import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

import com.google.common.net.HttpHeaders;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.EsignsanteConfigurationProperties;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Import({EsignsanteConfigurationProperties.class})
public class ProsanteConnectCalls {
/*
	HttpClient httpClient = HttpClient.create()
			  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			  .responseTimeout(Duration.ofMillis(5000))
			  .doOnConnected(conn -> 
			    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
			      .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
	
	WebClient client = WebClient.builder()
			  .baseUrl("http://localhost:8080")
			  .defaultCookie("cookieKey", "cookieValue")
			  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
			  .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
			  .clientConnector(new ReactorClientHttpConnector(httpClient))
			  .build();
	*/
	
	/*  Avec RestTemplateBuilder 
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	 
	    return builder
	            .setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000))
	         	            .build();
	}
	*/
	
	/*
	@Bean
	public RestTemplate restTemplate() 
	DefaultUriTemplateHandler defaultUriTemplateHandler = new DefaultUriTemplateHandler();
	defaultUriTemplateHandler.setBaseUrl(url);
	*/
	
	/* avec Apache httpClient 
	@Bean
	public RestTemplate restTemplate() {
	    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
	    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(apiHost));
	    return restTemplate;
	}
	 
	@Bean
	@ConditionalOnMissingBean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory 
	                        = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setHttpClient(httpClient);
	    return clientHttpRequestFactory;
	}
	*/
	
	/*
	// HttpHeaders
    HttpHeaders headers = new HttpHeaders();

    headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
    // Request to return JSON format
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("my_other_key", "my_other_value");

    // HttpEntity<String>: To get result as String.
    HttpEntity<String> entity = new HttpEntity<String>(headers);

    // RestTemplate
    RestTemplate restTemplate = new RestTemplate();

    // Send request with GET method, and Headers.
    ResponseEntity<String> response = restTemplate.exchange(URL_EMPLOYEES, //
            HttpMethod.GET, entity, String.class);

    String result = response.getBody();

    System.out.println(result);
	*/
}
