package fr.istic.sit.controller;

import fr.istic.sit.annotation.Notification;
import fr.istic.sit.domain.Intervention;
import fr.istic.sit.model.ActionMissionDrone;
import fr.istic.sit.service.DroneService;
import fr.istic.sit.service.InterventionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Group A
 */
@RestController
@RequestMapping("/drone")
@Api(value = "drone", description = "Drone API", produces = "application/json")
public class DroneController {

    @Autowired
    private DroneService service;

    // -------------------------------------  PATCH  ----------------------------------//
    //@Notification
    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "Action on Drone", nickname = "Action Drone")
    public void actionDrone(OAuth2Authentication authentication, @RequestBody ActionMissionDrone actionMissionDrone) {
        service.actionNO(actionMissionDrone);
    }

}
