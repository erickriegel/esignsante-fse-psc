package fr.ans.sign.esignsantepsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"fr.ans.api.sign.esignsante.psc.api", "fr.ans.api.sign.esignsante.psc.api"})

public class EsignsantePscApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsignsantePscApplication.class, args);
	}

}
