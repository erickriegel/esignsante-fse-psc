package fr.ans.api.sign.esignsante.psc.api.requests;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import lombok.extern.slf4j.Slf4j;


/*
@Slf4j
@Configuration
@Import({MongoProperties.class, EmbeddedMongoProperties.class})
*/

@Slf4j
@TestConfiguration
//@Import({MongoProperties.class, EmbeddedMongoProperties.class})
public class TestEmbeddedMongoDBConfig  {

//	@Value("${spring.profiles.active}")
//	private String profileActive;

	@Autowired
    private MongodStarter mongodStarter;
	
	//@Autowired
	MongodExecutable mongodExecutable;
	
	    
    private String databaseName = "esignsante-psc-archives";
	// private String databaseName = "archives";
    
    private final static String IP = "localhost";
    private final static int PORT = 27020;
//	@Value("${spring.data.mongodb.username}")
//	private String mongoUser;
//    @Value("${spring.data.mongodb.password}")
//    private String mongoUserPassword;


    @Bean
    public MongodStarter mongodStarter() {
        return MongodStarter.getDefaultInstance();
    }

    /*
    @Bean
	public MongoTemplate mongoTemplate() throws IOException {
		ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(IP, PORT, false))
	            .build();
		
		
		log.error("TTTTT   ICI CONFIG pour EMbeddedMongoDB");

//		        MongodStarter starter = MongodStarter.getDefaultInstance();
		        MongodExecutable mongodExecutable = mongodStarter.prepare(mongodConfig);
		        mongodExecutable.start();
		log.error("FFFFFFFFFFFFFFFFFFFFFFFF");
		log.info("connexion parameters to MongoDB used are: :  mongodb://"+ IP + ":" + PORT + "  " + databaseName);
		return new MongoTemplate(MongoClients.create( "mongodb://"+ IP + ":" + PORT ), databaseName);
	}
	*/
    
    @Bean
	public void startEmbeddedMongo() throws IOException {
		ImmutableMongodConfig mongodConfig = MongodConfig
	            .builder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(IP, PORT, false))
	            .build();
		
		
		log.error("TTTTT   ICI CONFIG pour EMbeddedMongoDB");

//		        MongodStarter starter = MongodStarter.getDefaultInstance();
		        mongodExecutable = mongodStarter.prepare(mongodConfig);
		        mongodExecutable.start();
		log.error("FFFFFFFFFFFFFFFFFFFFFFFF");
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
}
