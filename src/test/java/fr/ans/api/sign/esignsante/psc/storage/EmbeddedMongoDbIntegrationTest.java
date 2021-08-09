package fr.ans.api.sign.esignsante.psc.storage;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
//import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
import lombok.extern.slf4j.Slf4j;

//@SpringBootTest 
@Slf4j
//@DataMongoTest
//@OverrideAutoConfiguration( enabled = true)
public class EmbeddedMongoDbIntegrationTest {
	 private static final String CONNECTION_STRING = "mongodb://%s:%d";

	    
	    private MongodExecutable mongodExecutable;
	    private MongoTemplate mongoTemplate;
	    
	    
		    private String databaseName = "esignsante-psc-archives";
	    
	    private final static String IP = "localhost";
        private final static int PORT = 27018;
    
        private final static String COLLECTION = "collection";

     
	    @AfterEach
	    void clean() {
	        mongodExecutable.stop();
	    }

	    @BeforeEach
	    void setup() throws Exception {
	          

	        ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(IP, PORT, false))
	            .build();

	        MongodStarter starter = MongodStarter.getDefaultInstance();
	        mongodExecutable = starter.prepare(mongodConfig);
	        mongodExecutable.start();
	       mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, IP, PORT)), databaseName);
	        
	        log.warn("CCCCCCCCCCCCCCCCCCCCCCCCCCCC");
			log.warn("PORT: "+ PORT);
			
	    }

	    
	 // @Test
	   void premierTestDev() {
		   ArchiveSignature arch1 = new ArchiveSignature( new Date(),"DocumentTransmis1","Medecin1" );
		   ArchiveSignature arch2 = new ArchiveSignature( new Date(),"DocumentTransmis2","Medecin2" );
		   ArchiveSignature arch3 = new ArchiveSignature( new Date(),"DocumentTransmis3","Medecin3" );
		   mongoTemplate.save(arch1, COLLECTION);
		   mongoTemplate.save(arch2, COLLECTION);
		   mongoTemplate.save(arch3, COLLECTION);
		   
		   List<ArchiveSignature> results = mongoTemplate.findAll(ArchiveSignature.class, COLLECTION);
		   log.warn("nb: " + results.size());
		   log.warn(results.toString());
		   
	   }
	    
}
