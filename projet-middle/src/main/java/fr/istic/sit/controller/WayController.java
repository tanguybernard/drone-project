package fr.istic.sit.controller;

import fr.istic.sit.domain.WayType;
import fr.istic.sit.service.WayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @author FireDroneTeam
 */
@RestController
@RequestMapping("/way")
@Api(value = "Way", description = "Way API", produces = "application/json")
public class WayController {

    @Autowired
    private WayService service;

    @RequestMapping(value="" , method = RequestMethod.GET)
    public List<WayType> ways() {
        return service.getAll();
    }
}
