package fr.istic.sit.dao;

import fr.istic.sit.TestApplication;
import fr.istic.sit.configuration.MongoTestConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by fracma on 5/31/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestApplication.class, MongoTestConfiguration.class})
@WebAppConfiguration
//@IntegrationTest
public abstract class DatabaseUnitTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    protected void importJSON(String collection, String file) {
        try {
            for (Object line : FileUtils.readLines(new File(file), "utf8")) {
                mongoTemplate.save(line, collection);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not import file: " + file, e);
        }
    }
}
