package fr.ans.api.sign.esignsante.psc.storage;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import fr.ans.api.sign.esignsante.psc.storage.entity.ArchiveSignature;

public class EmbeddedMongoDbIntegrationTest {
	 private static final String CONNECTION_STRING = "mongodb://%s:%d";

	    private MongodExecutable mongodExecutable;
	    private MongoTemplate mongoTemplate;
	    private String databaseName = "ArchivesSignatures";

	    @AfterEach
	    void clean() {
	        mongodExecutable.stop();
	    }

	    @BeforeEach
	    void setup() throws Exception {
	        String ip = "localhost";
	        int port = 27018;

	        ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(ip, port, false /*IPV6*/))
	            .build();

	        MongodStarter starter = MongodStarter.getDefaultInstance();
	        mongodExecutable = starter.prepare(mongodConfig);
	        mongodExecutable.start();
	        //MongoClient mongoClient = new MongoClient(net.getServerAddress().getHostName(), net.getPort());
	        //MongoClient mongoClient = new MongoClient(ip,port);
	        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), databaseName);
	    }

	    
	  // @Test
	   void premierTestDev() {
		   ArchiveSignature arch1 = new ArchiveSignature( new Date(),"DocumentTransmis1","Medecin1" );
		   ArchiveSignature arch2 = new ArchiveSignature( new Date(),"DocumentTransmis2","Medecin2" );
		   ArchiveSignature arch3 = new ArchiveSignature( new Date(),"DocumentTransmis3","Medecin3" );
		   mongoTemplate.save(arch1, "collection");
		   mongoTemplate.save(arch2, "collection");
		   mongoTemplate.save(arch3, "collection");
		   
		   List<ArchiveSignature> results = mongoTemplate.findAll(ArchiveSignature.class, "collection");
		   System.err.println("nb: " + results.size());
		   System.err.println(results.toString());
		   
	   }
	    
}
