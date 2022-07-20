/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.prosantedatas;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import static fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException.throwExceptionRequestError;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.ans.api.sign.esignsante.psc.utils.Helper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(name = "with.gravitee", havingValue="false", matchIfMissing=true)
public class PSCDataFromPSCCallsImpl implements PSCData{

	@Value("${psc.url.introspection}")
	public String pscUrlIntrospection;

	@Value("${psc.url.userinfo}")
	public String pscUrlUserInfo;

	
	@Value("${psc.clientID}")
	public String pscClientID;

	@Value("${psc.clientSecret}")
	public String pscSecret;


	/*
	 * Set headers for PSC calls
	 */
	private HttpHeaders getHeadersIntrospection() throws UnsupportedEncodingException {
		String adminuserCredentials = pscClientID + ":" + pscSecret;
		String encodedCredentials = Helper.encodeBase64(adminuserCredentials);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Basic " + encodedCredentials);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return httpHeaders;
	}
	
	private HttpHeaders getHeadersUserInfo(String accessToken) throws UnsupportedEncodingException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + accessToken);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return httpHeaders;
	}

	public String getReponseIntrospection(String accessToken)
			throws URISyntaxException, UnsupportedEncodingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		log.debug("uri connection à ProSanteConnect: {}", pscUrlIntrospection);
		log.debug("ClientID pour proSanteConnect: {}", pscClientID);
		URI uri = new URI(pscUrlIntrospection);

		// payload
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, getHeadersIntrospection());
		ResponseEntity<String> result = restTemplate.postForEntity(uri, requestEntity, String.class);

		log.debug("Appel ProSanteConnect Reponse: StatusCode {} \n body: {}", result.getStatusCode(),
				result.getBody());
		return result.getBody();
	}

	@Override
	public String getIntrospectionResult(String accessToken) {
		String result = null;
		try {
			result =  getReponseIntrospection(accessToken);
		} catch (JsonProcessingException | UnsupportedEncodingException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throwExceptionRequestError("TODO throw a revoir...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}

	@Override
	public String getUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		log.debug("uri connection à ProSanteConnect: {}", pscUrlUserInfo);
		log.debug("ClientID pour proSanteConnect: {}", pscClientID);
		URI uri = null;
		try {
			uri = new URI(pscUrlUserInfo);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throwExceptionRequestError("Erreur lors de l'appel à PSC (URI)", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// payload
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken);
		HttpEntity<MultiValueMap<String, String>> requestEntity = null;
		try {
			requestEntity = new HttpEntity<>(getHeadersUserInfo(accessToken));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throwExceptionRequestError("Erreur sur la construction de la  requête getUserInfo (PSC)", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		log.debug("Appel ProSanteConnect pour getUserInfo. Reponse: StatusCode {} \n body: {}", result.getStatusCode(),
				result.getBody());
		return result.getBody();
	}

	@Override
	public void isPSCvalidateToken(String introspectionResult) {
		Helper.parsePSCresponse(introspectionResult);
	}

}
