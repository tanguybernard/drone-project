package fr.istic.sit.dao;

import fr.istic.sit.domain.Intervention;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author FireDroneTeam
 */
public class InterventionRepositoryTest extends DatabaseUnitTest {

    @Autowired
    InterventionRepository interventionRepository;

    @Before
    public void setUp() throws Exception {
        importJSON("intervention", "src/test/resources/intervention.json");
    }

    @Test
    public void testFindByStatusKO() throws Exception {
        List<Intervention> interventionList = interventionRepository.findByStatus("NOT");
        assertTrue(interventionList.size() == 0);
    }

    @Test
    public void testFindByStatus() throws Exception {
        List<Intervention> interventionList = interventionRepository.findByStatus("IN_PROGRESS");
        assertTrue(interventionList.size() == 2);
    }
}