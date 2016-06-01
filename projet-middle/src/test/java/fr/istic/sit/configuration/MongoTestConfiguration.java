package fr.istic.sit.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import de.flapdoodle.embed.mongo.MongoImportExecutable;
import de.flapdoodle.embed.mongo.MongoImportProcess;
import de.flapdoodle.embed.mongo.MongoImportStarter;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author FireDroneTeam
 */

@Configuration
public class MongoTestConfiguration extends AbstractMongoConfiguration {

  /*  @Autowired
    private Environment env;*/

    /*@Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }*/

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    /*@Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost",37017);
        //return new MongoClient("localhost",27018);
    }*/
    @Bean
    public Mongo mongo() throws IOException {
        System.setProperty("DB.TRACE","true");

        return new EmbeddedMongoBuilder()
                .bindIp("127.0.0.1")
                .port(12345)
                .build();

    }


}