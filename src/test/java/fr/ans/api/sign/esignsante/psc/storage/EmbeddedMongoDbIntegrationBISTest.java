package fr.ans.api.sign.esignsante.psc.storage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
//import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
import lombok.extern.slf4j.Slf4j;

/*
 * Classe utilisée uniquement pour les tests
 * Configuration et démarrage de Embedded MongoDB
 */


@SpringBootTest 
@Slf4j
@ExtendWith(TestEmbeddedMongoDBConfig.class)
public class EmbeddedMongoDbIntegrationBISTest {
	   
    
        private final static String COLLECTION = "collectionxx";

        @Autowired
    	MongoTemplate mongoTemplate;
        
	  @Test
	   public void essaiEcritureEmbeddeMongoDB() {
//		  TestEmbeddedMongoDBConfig mongo = new TestEmbeddedMongoDBConfig();
//		   MongoTemplate mongoTemplate = null;
//		log.debug("recup mopngoDBTemplate de test");
//		mongoTemplate = mongo.mongoTemplate();
//		
		// A supprimer: pour mise au point
		   ArchiveSignature arch1 = new ArchiveSignature( new Date(),"Transmis1","Med1" );
		   ArchiveSignature arch2 = new ArchiveSignature( new Date(),"Transmis2","Med2" );

		   mongoTemplate.save(arch1, COLLECTION);
		   mongoTemplate.save(arch2, COLLECTION);
		   
		   List<ArchiveSignature> results = mongoTemplate.findAll(ArchiveSignature.class, COLLECTION);
		   log.error("nb: " + results.size());
		   log.error(results.toString());
		   log.error("DDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		   log.error("nom de la base utilisée=> " + mongoTemplate.getDb().getName());
		   
		   //fin A supprimer: pour mise au point
		   
	   }
	    
}
