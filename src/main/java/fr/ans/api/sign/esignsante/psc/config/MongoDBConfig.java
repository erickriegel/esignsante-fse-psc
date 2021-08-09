package fr.ans.api.sign.esignsante.psc.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Profile(value = { "production" })
//@Profile("!dev")
//@Configuration
//@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
public class MongoDBConfig {

//	@Value("${spring.profiles.active}")
//	private String profileActive;

	@Value("${spring.data.mongodb.host}")
	private String mongoHost;

	@Value("${spring.data.mongodb.port}")
	private String mongoPort;

	@Value("${spring.data.mongodb.database}")
	private String mongoDatabase;

//	@Value("${spring.data.mongodb.username}")
//	private String mongoUser;
//    @Value("${spring.data.mongodb.password}")
//    private String mongoUserPassword;

	
	@Bean
	public MongoTemplate mongoTemplate() throws IOException {
		log.error("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		log.info("connexion parameters to MongoDB used are: :  mongodb://"+ mongoHost + ":" + mongoPort + "  " + mongoDatabase);
		return new MongoTemplate(MongoClients.create( "mongodb://"+ mongoHost + ":" + mongoPort ), mongoDatabase);
	}
	
	
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
