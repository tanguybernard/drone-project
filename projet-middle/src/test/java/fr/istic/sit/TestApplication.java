package fr.istic.sit;

import fr.istic.sit.configuration.MongoConfiguration;
import fr.istic.sit.configuration.SwaggerConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * @author FireDroneTeam
 */

@SpringBootApplication(exclude = {MongoConfiguration.class, SwaggerConfiguration.class,SecurityAutoConfiguration.class})
public class TestApplication {
}

