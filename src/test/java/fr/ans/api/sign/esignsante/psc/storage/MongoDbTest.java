package fr.ans.api.sign.esignsante.psc.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.BsonDocument;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.api.sign.esignsante.psc.storage.repository.ProofMongoRepository;
import fr.ans.api.sign.esignsante.psc.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.json.Json;

/*
 * Classe utilisée uniquement pour les tests
 * Configuration et démarrage de Embedded MongoDB
 */


@SpringBootTest 
@Slf4j
public class MongoDbTest {
	   
    
	
        private final static String COLLECTION = "preuves";

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
		   log.info("nom de base utilisée => " + mongoTemplate.getDb().getName());
		  
		   //String sJson = "{\"active\":false}";
		   String sJson = "{\"Secteur_Activite\":\"SA07^1.2.250.1.71.4.2.4\",\"email_verified\":false,\"SubjectOrganization\":\"CABINET M SPECIALISTE0021889\",\"preferred_username\":\"899700218896\",\"given_name\":\"ROBERT\",\"SubjectRefPro\":{\"codeCivilite\":\"M\",\"exercices\":[{\"codeProfession\":\"10\",\"nomDexercice\":\"SPECIALISTE0021889\",\"prenomDexercice\":\"ROBERT\",\"codeTypeSavoirFaire\":\"\"}]},\"SubjectRole\":[\"10^1.2.250.1.213.1.1.5.5\"],\"SubjectNameID\":\"899700218896\",\"family_name\":\"SPECIALISTE0021889\"}";

		   String preuve =  "preuveBase64";
				 			   
		   //la collection doit être vide après le setup
		   assertTrue(repo.findAll().isEmpty());
				   String requestId = Helper.generateRequestId();
		   Date now = new Date() ;
		   ProofStorage prof1 = new ProofStorage(
				   requestId,
				   "CABINET M SPECIALISTE0021889", //SubjectOrganization
				   "899700218896", //preferred_username
				   "ROBERT", //given_name
				   "SPECIALISTE0021889", //family name
				   now, //timestamp
				   preuve 
				   );
	
		   mongoTemplate.save(prof1);
	
		  Set<String> mongoCollections = mongoTemplate.getCollectionNames();
		  assertTrue(mongoCollections.size() >= 1 , "il doit y avoir au moins une collection dans la base");
		  assertTrue(mongoCollections.contains("preuves") , "la collection @Document(collection = \"preuves\" doit exister");
		  List<ProofStorage> results = mongoTemplate.findAll(ProofStorage.class /*, COLLECTION*/);
				  
		  ProofStorage result = repo.findOneById(prof1.get_id());
		  		   assertEquals(result.getRequestId(), prof1.getRequestId());		
		   assertEquals(result.getPreferred_username(), prof1.getPreferred_username());		
		   assertEquals(result.getGiven_name(), prof1.getGiven_name());		
		   assertEquals(result.getFamily_name(), prof1.getFamily_name());	
		   assertEquals(result.getTimestamp().compareTo(prof1.getTimestamp()), 0);
		   assertTrue(result.getTimestamp().compareTo(new Date()) < 0);


		   results = repo.findBySubjectOrganizationWithOutBSON(prof1.getSubjectOrganization());
		   result = results.get(0);
		   assertEquals(result.getRequestId(), prof1.getRequestId());		
		   assertEquals(result.getPreferred_username(), prof1.getPreferred_username());		
		   assertEquals(result.getGiven_name(), prof1.getGiven_name());		
		   assertEquals(result.getFamily_name(), prof1.getFamily_name());	
		   assertEquals(result.getTimestamp().compareTo(prof1.getTimestamp()), 0);
		   assertTrue(result.getTimestamp().compareTo(new Date()) < 0);

		   
		  //TEST retrouver une preuve à partir de l'id généré
		   log.info("Recherche de la preuve stokée id_bdd: " + result.get_id());
		   ProofStorage tmp = repo.findOneById(result.get_id());
		   assertEquals(result.getPreferred_username(), prof1.getPreferred_username());		
		   assertEquals(result.getGiven_name(), prof1.getGiven_name());		
		   assertEquals(result.getFamily_name(), prof1.getFamily_name());	
		   assertEquals(result.getTimestamp().compareTo(prof1.getTimestamp()), 0);
		   assertTrue(result.getTimestamp().compareTo(new Date()) < 0);
		 //TODO BSON  assertEquals(result.getFamily_name(), prof1.getFamily_name());		
		   
		   
		   results = repo.findBySubjectOrganizationWithOutBSON("NotExisting");
		   assertTrue(results.isEmpty());
		   log.info("Test stokage recherche en BDD OK");
	   }
	    
}
