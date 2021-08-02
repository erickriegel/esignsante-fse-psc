package fr.ans.sign.esignsantepsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication (exclude= {EmbeddedMongoAutoConfiguration.class})
@ComponentScan(basePackages = {"fr.ans.api.sign.esignsante.psc.api", "fr.ans.api.sign.esignsante.psc.config"})

public class EsignsantePscApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsignsantePscApplication.class, args);
	}
}
