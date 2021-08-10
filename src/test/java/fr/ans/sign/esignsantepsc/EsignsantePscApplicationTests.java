package fr.ans.sign.esignsantepsc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import fr.ans.api.sign.esignsante.psc.storage.TestEmbeddedMongoDBConfig;

@SpringBootTest
@ContextConfiguration
@ExtendWith(TestEmbeddedMongoDBConfig.class)
class EsignsantePscApplicationTests {

	@Test
	void contextLoads() {
	}

}
