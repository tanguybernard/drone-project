package fr.istic.sit.dao;

import fr.istic.sit.TestApplication;
import fr.istic.sit.configuration.MongoTestConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * @author FireDroneTeam
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestApplication.class, MongoTestConfiguration.class})
@WebIntegrationTest
public abstract class DatabaseUnitTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void importJSON(String collection, String file) {
        try {
            mongoTemplate.dropCollection(collection);
            for (Object line : FileUtils.readLines(new File(file), "utf8")) {
                mongoTemplate.save(line, collection);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not import file: " + file, e);
        }
    }
}
