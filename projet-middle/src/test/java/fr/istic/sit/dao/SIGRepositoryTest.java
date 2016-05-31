package fr.istic.sit.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class SIGRepositoryTest  extends DatabaseUnitTest{

    @Autowired
    SIGRepository sigRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void init(){
        importJSON("sig", "src/test/resources/sig.json");
    }


    @Test
    public void testFindById() throws Exception {
        assertNotNull(sigRepository);
        int size = sigRepository.findAll().size();
        assertTrue("OK ", size == 3);
        assertNotNull(mongoTemplate);
    }
}