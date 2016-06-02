package fr.istic.sit.controller;

import fr.istic.sit.domain.Sig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author FireDroneTeam
 */
public class SIGControllerTest extends AbstractControllerTests{
    private int SIZE = 3;
    private RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() throws Exception {
        importJSON("sig", "src/test/resources/sig.json");
    }


    @Test
    public void testSearchSIG() throws Exception {
        assertNotNull(restTemplate);
        ResponseEntity<Sig> response =	restTemplate.getForEntity(getBaseUrl()+"/sig/{id}" , Sig.class, "1");
        assertEquals( response.getStatusCode(), HttpStatus.OK);

        final Sig sig= response.getBody();
        assertTrue(sig.getType().equals("WATER"));
    }

    @Test
    public void testAllSIG() throws Exception {
        ResponseEntity<List> response =	restTemplate.getForEntity(getBaseUrl()+"/sig" , List.class);
        assertEquals( response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody().size()==SIZE);
    }

}