package fr.istic.sit.configuration;

import com.mongodb.Mongo;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.io.IOException;

/**
 * @author FireDroneTeam
 */

@Configuration
public class MongoTestConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Bean
    public Mongo mongo() throws IOException {
        System.setProperty("DB.TRACE","true");

        return new EmbeddedMongoBuilder()
                .bindIp("127.0.0.1")
                .port(12345)
                .build();

    }


}