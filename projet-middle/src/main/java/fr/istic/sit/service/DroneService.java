package fr.istic.sit.service;

import fr.istic.sit.util.ExecuteShellComand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fracma on 5/30/16.
 */

@Component
public class DroneService {
    private final Logger log = LoggerFactory.getLogger(DroneService.class);


    @Autowired
    private ExecuteShellComand executeShellComand;

    public void startDrone(){
        log.debug("Starting drone....");
        String response = executeShellComand.executeCommand("./create-drone.sh 1 48.115127 -1.637972");
        log.info("Response "+ response);
    }
}
