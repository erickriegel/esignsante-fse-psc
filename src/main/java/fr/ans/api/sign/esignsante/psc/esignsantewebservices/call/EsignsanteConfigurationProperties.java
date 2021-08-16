package fr.ans.api.sign.esignsante.psc.esignsantewebservices.call;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Scope;
import lombok.Getter;

/*
 * Classe EsignsanteConfigurationProperties
 * Configuration d'esignsante webservies( url - config.json)
 *  utilisée par esignsante-psc 
 * 
 */
@Getter
@Configuration

public class EsignsanteConfigurationProperties {

	@Value("${esignsante.webservices.signature.confId}")
	private int signatureConfId;

	@Value("${esignsante.webservices.signature.secret}")
	private String secret;

	@Value("${esignsante.webservices.proof.confId}")
	private int proofConfId;
	
	@Value("${esignsante.webservices.checksignature}")
	private int checkSignatureConfId;
	
	@Value("${esignsante.webservices.basepath")
	private String basePath;
	
	@Value("${esignsante.webservices.appliantId}")
	private String appliantId;
	
	@Value("${esignsante.webservices.proofTag}")
	private String proofTag;

}
