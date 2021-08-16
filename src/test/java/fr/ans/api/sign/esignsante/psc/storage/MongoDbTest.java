package fr.ans.api.sign.esignsante.psc.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.ProofMongoRepository;
import lombok.extern.slf4j.Slf4j;

/*
 * Classe utilisée uniquement pour les tests
 * Configuration et démarrage de Embedded MongoDB
 */


@SpringBootTest 
@Slf4j
public class MongoDbTest {
	   
    
	
        private final static String COLLECTION = "collectionxx";

        @Autowired
    	MongoTemplate mongoTemplate;
        
        @Autowired
        ProofMongoRepository repo;  
    
        @BeforeEach
    	public void setup() {
        	repo.deleteAll();
        }
    	  
        
	  @Test
	   public void sauvegardeEmbeddeMongoDB() {
		   log.info("*** Test d'écriture ProofStorage dans MongoDB");
		   log.error("nom de la base utilisée=> " + mongoTemplate.getDb().getName());
		 			   
		   //la collection doit être vide après le setup
		   assertTrue(repo.findAll().isEmpty());
		   
		   Date now = new Date() ;
		   ProofStorage prof1 = new ProofStorage(
				   "CABINET M SPECIALISTE0021889", //SubjectOrganization
				   "899700218896", //preferred_username
				   "ROBERT", //given_name
				   "SPECIALISTE0021889", //family name
				   now, //timestamp
				   null // TODO
				   );
	
		   mongoTemplate.save(prof1);
	
		   
		  Set<String> mongoCollections = mongoTemplate.getCollectionNames();
		  assertTrue(mongoCollections.size() >= 1 , "il doit y avoir au moins une collection dans la base");
		  assertTrue(mongoCollections.contains("archivesPreuves") , "la collection @Document(collection = \"archivesPreuves\" doit exister");
		   
		   List<ProofStorage> results = mongoTemplate.findAll(ProofStorage.class, COLLECTION);
		   log.info("nb elements trouvés: " + results.size());
		   
		   
		   results = repo.findBySubjectOrganizationWithOutBSON(prof1.getSubjectOrganization());
		   log.info("nb elements trouvés: " + results.size());
		   ProofStorage result = results.get(0);
		   assertEquals(result.getPreferred_username(), prof1.getPreferred_username());		
		   assertEquals(result.getGiven_name(), prof1.getGiven_name());		
		   assertEquals(result.getFamily_name(), prof1.getFamily_name());	
		   assertEquals(result.getTimestamp().compareTo(prof1.getTimestamp()), 0);
		   assertTrue(result.getTimestamp().compareTo(new Date()) < 0);
		 //TODO BSON  assertEquals(result.getFamily_name(), prof1.getFamily_name());		
		   
		   
		   results = repo.findBySubjectOrganizationWithOutBSON("NotExisting");
		   assertTrue(results.isEmpty());
	   }
	    
}
