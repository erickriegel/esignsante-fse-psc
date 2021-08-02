package fr.ans.api.sign.esignsante.psc.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

@Configuration
@Profile(value = { "production" })
public class MongoDBConfig {

//	@Value("${spring.profiles.active}")
//	private String profileActive;

	@Value("${spring.data.mongodb.host}")
	private String mongoHost;

	@Value("${spring.data.mongodb.port}")
	private String mongoPort;

	@Value("${spring.data.mongodb.database}")
	private String mongoDatabase;

	@Value("${spring.data.mongodb.username}")
	private String mongoUser;

//    @Value("${spring.data.mongodb.password}")
//    private String mongoUserPassword;

	@Bean
	public MongoTemplate mongoTemplate() throws IOException {
		return new MongoTemplate(MongoClients.create( "mongodb://"+ mongoHost + ":" + mongoPort ), mongoDatabase);

	}
}
