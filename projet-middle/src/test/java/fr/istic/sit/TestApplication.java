package fr.istic.sit;

import fr.istic.sit.configuration.MongoConfiguration;
import fr.istic.sit.configuration.SwaggerConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * @author FireDroneTeam
 */

//@Configuration
//@ComponentScan(value = {"fr.istic.sit.service.*"})
@EnableAutoConfiguration(exclude={MongoConfiguration.class, SwaggerConfiguration.class})
//@ComponentScan()
//@Import(MongoTestConfiguration.class)
//@TestPropertySource("aplication-test.properties")
@SpringBootApplication(exclude = {MongoConfiguration.class, SwaggerConfiguration.class,SecurityAutoConfiguration.class})
public class TestApplication {
}

