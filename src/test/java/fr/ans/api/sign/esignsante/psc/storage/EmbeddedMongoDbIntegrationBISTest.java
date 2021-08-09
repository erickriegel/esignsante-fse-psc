package fr.ans.api.sign.esignsante.psc.storage;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import fr.ans.api.sign.esignsante.psc.api.requests.TestEmbeddedMongoDBConfig;
import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
//import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest 
@Slf4j
//@DataMongoTest
//@ExtendWith(SpringExtension.class)
//@Import(EmbeddedMongoAutoConfiguration.class)
//@ContextConfiguration


//@Import(TestEmbeddedMongoDBConfig.class)

public class EmbeddedMongoDbIntegrationBISTest {
	
 	 
//	    @Autowired
	    private MongoTemplate mongoTemplate;
	    
	    
	//  private String databaseName = "esignsante-psc-archives";
	    
    
        private final static String COLLECTION = "collection";

//       private final static String IP = "localhost";
//       private final static int PORT = 27021;

        /*
        @BeforeEach
	    void setup() throws Exception {
	          

	        ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .net(new Net(IP, PORT, false))
	            .build();
        }
        */
	    
//	  @Test
	   void toto() {
		   ArchiveSignature arch1 = new ArchiveSignature( new Date(),"Transmis1","Med1" );
		   ArchiveSignature arch2 = new ArchiveSignature( new Date(),"Transmis2","Med2" );
		   mongoTemplate.save(arch1, COLLECTION);
		   mongoTemplate.save(arch2, COLLECTION);
		   
		   List<ArchiveSignature> results = mongoTemplate.findAll(ArchiveSignature.class, COLLECTION);
		   log.error("nb: " + results.size());
		   log.error(results.toString());
		   log.error("DDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		   log.error(mongoTemplate.getDb().getName());
	//		log.error("PORT: "+ PORT);
		   
	   }
	    
}
