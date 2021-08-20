package fr.ans.api.sign.esignsante.psc.prosantecall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@EnableAutoConfiguration
@Service
public class ProSanteConnectProperties {
	
	@Value("${psc.url}")
	private String PscUrl;

	@Value("${psc.clientID}")
	private String PscClientID;

	@Value("${psc.clientSecret}")
	private String PscSecret;
}
