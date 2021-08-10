package fr.ans.api.sign.esignsante.psc.storage;

import java.awt.desktop.ScreenSleepEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import fr.ans.api.sign.esignsante.psc.config.MongoDBConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j

//@Import({MongoProperties.class, MongoDBConfig.class})
public class TestEmbeddedMongoDBConfig implements BeforeAllCallback, AfterAllCallback {

	MongodExecutable mongodExecutable = null;
	MongodProcess mongod = null;

//	private final static String IP = "localhost";
//	private final static int PORT = 27022;
	
//	@Value("${spring.data.mongodb.host}")
//	private static  String mongoHost;
//
//	@Value("${spring.data.mongodb.port}")
//	private int mongoPort;



	private static /*static*/ String mongoHost = "localhost" ;

	private static String mongoPort = "27017"; /* 0 // premier port libre;*/
	private static int port = 27017; // eviter le try-catch 

	
	private static /*static*/ String mongoDatabase = "esignsante-psc-archives";

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
//		log.info("beforeAll");
//		if (mongodExecutable == null) {
//		startEmbeddedMongo();
//		}
	}


	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		log.info("afterAll");

//		if (mongod != null)
//		{
//			log.info("stop mongo process");
//			mongod.stop();
//		}
//
//		if (mongodExecutable != null) {
//		
//			mongodExecutable.stop();
//			if (mongod != null)
//			{
//				mongod.stop();
//			}
//		}
		/*
		try {
			//TimeUnit.SECONDS.sleep(1);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	
//	public  MongoTemplate mongoTemplate()  {
//		log.error("LALALALALALA");
//		
//			log.info("connexion parameters to MongoDB used are: :  mongodb://" + mongoHost + ":" + mongoPort + "  " + mongoDatabase);
//		return new MongoTemplate(MongoClients.create("mongodb://" + mongoHost + ":" + mongoPort), mongoDatabase);
//	}

	private void startEmbeddedMongo() throws IOException {
		log.info("testConf mongoHost: "  + mongoHost);
		log.info("testConf mongoPort: "  + mongoPort);
		
		ImmutableMongodConfig mongodConfig = MongodConfig.builder().version(Version.Main.PRODUCTION)
				.net(new Net(mongoHost, port, false)).build();

		log.error("TTTTT   ICI CONFIG pour EMbeddedMongoDB");

		MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
		mongod =mongodExecutable.start();
		log.error("FFFFFFFFFFFFFFFFFFFFFFFF");
		/*try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}

/*
 * @Bean(destroyMethod = "stop") public MongodProcess
 * mongodProcess(MongodExecutable mongodExecutable) throws IOException { return
 * mongodExecutable.start(); }
 * 
 * @Bean(destroyMethod = "stop") public MongodExecutable
 * mongodExecutable(MongodStarter mongodStarter, IMongodConfig iMongodConfig)
 * throws IOException { return mongodStarter.prepare(iMongodConfig); }
 * 
 * @Bean public IMongodConfig mongodConfig() throws IOException { return new
 * MongodConfigBuilder().version(Version.Main.PRODUCTION).build(); }
 * 
 * @Bean public MongodStarter mongodStarter() { return
 * MongodStarter.getDefaultInstance(); }
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
