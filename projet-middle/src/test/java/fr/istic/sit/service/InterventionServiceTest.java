package fr.istic.sit.service;

import fr.istic.sit.dao.DatabaseUnitTest;
import fr.istic.sit.domain.Intervention;
import fr.istic.sit.domain.Way;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author FireDroneTeam
 */

public class InterventionServiceTest extends DatabaseUnitTest {

    @Autowired
    InterventionService interventionService;

    @Before
    public void setUp() throws Exception {
        importJSON("intervention", "src/test/resources/intervention.json");
    }

    @Test
    public void testGetId() throws Exception {
        Intervention intervention = interventionService.getId("57229521b7603276ca1493ce");
        assertNotNull(intervention);
        assertTrue(intervention.getSinisterCode().equals("FDF"));
    }

    @Test
    public void testGetIdKO() throws Exception {
        Intervention intervention = interventionService.getId("57229521b7603276ca1493");
        assertNull(intervention);
    }

    @Test
    public void testGetInterventions() throws Exception {
        List<Intervention> interventionList = interventionService.getInterventions("IN_PROGRESS");
        assertTrue(interventionList.size() == 2);
    }

    @Test
    public void testInsert() throws Exception {
        List<Intervention> interventionList = interventionService.getInterventions("");
        int sizeInit = interventionList.size();

        Intervention intervention = new Intervention();
        intervention.setStatus("IN_PROGRESS");
        intervention.setLatitude(48.1154336);
        intervention.setLongitude(-1.638722);
        intervention.setDate("01/06/2016 00:56");
        intervention.setAddress("Rennes ISTIC");
        intervention.setWays(new ArrayList<Way>());
        assertNotNull(interventionService.insert(intervention));

        interventionList = interventionService.getInterventions("");
        assertTrue( (sizeInit+1) == interventionList.size());
    }
/*
    @Test
    public void testDelete() throws Exception {
        List<Intervention> interventionList = interventionService.getInterventions("");
        int sizeInit = interventionList.size();
        assertTrue(interventionList.size()>0);
        System.out.println("SIZE111 ----- "+interventionList.size());

        interventionService.delete(interventionList.get(0));
        interventionList = interventionService.getInterventions("");

        System.out.println("SIZE ----- "+interventionList.size());
        //assertTrue( (sizeInit-1) == interventionList.size());
    }*/

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testAddWay() throws Exception {

    }

    @Test
    public void testEditWay() throws Exception {

    }

    @Test
    public void testDeleteWay() throws Exception {

    }

    @Test
    public void testGetCos() throws Exception {

    }

    @Test
    public void testSetCos() throws Exception {

    }

    @Test
    public void testDeleteCos() throws Exception {

    }

    @Test
    public void testAddRessource() throws Exception {

    }

    @Test
    public void testEditRessource() throws Exception {

    }

    @Test
    public void testDeleteRessource() throws Exception {

    }

    @Test
    public void testGetWaysInterventions() throws Exception {

    }

    @Test
    public void testGetDrones() throws Exception {

    }

    @Test
    public void testAddDrone() throws Exception {

    }

    @Test
    public void testEditDrone() throws Exception {

    }

    @Test
    public void testDeleteDrone() throws Exception {

    }

    @Test
    public void testGetWaysAllInterventions() throws Exception {

    }
}