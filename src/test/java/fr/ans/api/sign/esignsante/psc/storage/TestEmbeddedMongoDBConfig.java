package fr.ans.api.sign.esignsante.psc.storage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j

//@Import({MongoProperties.class, EmbeddedMongoProperties.class})
public class TestEmbeddedMongoDBConfig /*extends EmbeddedMongoAutoConfiguration*/ {

//	@Value("${spring.profiles.active}")
//	private String profileActive;

//	public TestEmbeddedMongoDBConfig(MongoProperties properties) {
//		super(properties);
//		// TODO Auto-generated constructor stub
//	}

	
//	@Autowired
//    private MongodStarter mongodStarter;
	
	
	MongodExecutable mongodExecutable;
	
	    
 //   private String databaseName = "esignsante-psc-archives";
	// private String databaseName = "archives";
    
    private final static String IP = "localhost";
    private final static int PORT = 27022;
    
    private String mongoDatabase = "esignsante-psc-archives";


//    @Bean
//    public MongodStarter mongodStarter() {
//        return MongodStarter.getDefaultInstance();
//    }

      
  
	private void startEmbeddedMongo() throws IOException {
		ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(IP, PORT, false))
	            .build();
		
		
		log.error("TTTTT   ICI CONFIG pour EMbeddedMongoDB");

		        MongodStarter starter = MongodStarter.getDefaultInstance();
		        mongodExecutable = starter.prepare(mongodConfig);
		        mongodExecutable.start();
		log.error("FFFFFFFFFFFFFFFFFFFFFFFF");
	}
	
//		@Bean
		public MongoTemplate mongoTemplate() throws IOException {
			
			log.error("LALALALALALA");
			startEmbeddedMongo();
			log.info("connexion parameters to MongoDB used are: :  mongodb://"+ IP + ":" + PORT + "  " + mongoDatabase);
			return new MongoTemplate(MongoClients.create( "mongodb://"+ IP + ":" + PORT ), mongoDatabase);
		}
		
		
	}
    
    
    
    
    
	/*
	@Bean(destroyMethod = "stop")
    public MongodProcess mongodProcess(MongodExecutable mongodExecutable) throws IOException {
        return mongodExecutable.start();
    }

    @Bean(destroyMethod = "stop")
    public MongodExecutable mongodExecutable(MongodStarter mongodStarter, IMongodConfig iMongodConfig) throws IOException {
        return mongodStarter.prepare(iMongodConfig);
    }

    @Bean
    public IMongodConfig mongodConfig() throws IOException {
        return new MongodConfigBuilder().version(Version.Main.PRODUCTION).build();
    }

    @Bean
    public MongodStarter mongodStarter() {
        return MongodStarter.getDefaultInstance();
    }
	*/
	
//	ImmutableMongodConfig mongodConfig = MongodConfig
//            .builder()
//            .version(Version.Main.PRODUCTION)
//            .net(new Net("localhost", "27018", false))
//            .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongodExecutable.start();

