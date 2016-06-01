package fr.istic.sit.service;

import fr.istic.sit.dao.DatabaseUnitTest;
import fr.istic.sit.domain.Sig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author FireDroneTeam
 */
public class SIGServiceTest extends DatabaseUnitTest {

    private int SIZE = 3;

    @Autowired
    SIGService sigService;

    @Before
    public void init(){
        importJSON("sig", "src/test/resources/sig.json");
    }

    @Test
    public void testGetId() throws Exception {
        assertNotNull(sigService);
        Sig sigResponse = sigService.getId("1");
        assertNotNull(sigResponse);
        assertTrue(sigResponse.getType().equals("WATER"));
    }

    @Test
    public void testGetAll() throws Exception {
        assertTrue("testGetAll SIGServiceTest OK", sigService.getAll().size()==SIZE);
    }
}