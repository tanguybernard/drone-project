package fr.istic.sit.controller;

import fr.istic.sit.dao.DatabaseUnitTest;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author FireDroneTeam
 */

public class AbstractControllerTests extends DatabaseUnitTest{

    @Value("${local.server.port}")
    private int port;

    protected String getBaseUrl() {
        return new StringBuilder("http://localhost:").append(port).toString();
    }
}
