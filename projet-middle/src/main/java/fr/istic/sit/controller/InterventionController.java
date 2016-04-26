package fr.istic.sit.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import fr.istic.sit.domain.Intervention;
import fr.istic.sit.domain.Way;
import fr.istic.sit.service.InterventionService;

/**
 * @author FireDroneTeam
 */

@RestController
@RequestMapping("/intervention")
@Api(value = "Intervention", description = "Intervention API", produces = "application/json")
public class InterventionController {

    @Autowired
    private InterventionService service;

    @RequestMapping(value= "/{id}/way" , method = RequestMethod.GET)
    @ApiOperation(value = "Search ways of an intervention", nickname = "WaysOfIntervention", response = Way.class )
    public List<Way> searchWaysOfIntervention(OAuth2Authentication authentication, @PathVariable String id) {
        return service.getId(id).getWays();
    }

    @RequestMapping(value="/{id}" , method = RequestMethod.GET)
    @ApiOperation(value = "Search an intervention", nickname = "Intervention", response = Intervention.class )
    public Intervention searchIntervention(OAuth2Authentication authentication, @PathVariable String id) {
    	return service.getId(id);
    }

    @RequestMapping(value="" , method = RequestMethod.GET)
    @ApiOperation(value = "List of intervention. Optional parameter: status", nickname = "Interventions", response = Intervention.class )
    public List<Intervention> interventions(OAuth2Authentication authentication, @RequestParam (value = "status" , required = false) String status) {
        return service.getInterventions(status);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Insert intervention", nickname = "New intervention", response = Intervention.class )
    public Intervention insertIntervention(OAuth2Authentication authentication, @RequestBody Intervention intervention){
    	return service.insert(intervention);
    }

    @RequestMapping(value= "/{id}/way" , method = RequestMethod.POST)
    @ApiOperation(value = "Add a way of an intervention", nickname = "AddWayOfIntervention", response = Way.class )
    public List<Way> addWayToIntervention(OAuth2Authentication authentication, @PathVariable String id, @RequestBody Way way) {
        return service.addWay(id,way).getWays();
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit intervention", nickname = "Edit intervention")
    public void updateIntervention(OAuth2Authentication authentication, @RequestBody Intervention intervention){
    	service.update(intervention);
    }

    @RequestMapping(value= "/{id}/way" , method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit a way of an intervention", nickname = "AddWayOfIntervention", response = Way.class )
    public List<Way> updateWayToIntervention(OAuth2Authentication authentication, @PathVariable String id, @RequestBody Way way) {
        return service.editWay(id,way).getWays();
    }

    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    @ApiOperation(value = "Delete an intervention", nickname = "DeleteIntervention" )
    public void deleteIntervention(OAuth2Authentication authentication, @PathVariable String id) {
    	service.delete(service.getId(id));
    }

    @RequestMapping(method=RequestMethod.DELETE, value="{id}/way/{idWay}")
    @ApiOperation(value = "Delete a way of an intervention", nickname = "DeleteWayIntervention" )
    public void deleteWayIntervention(OAuth2Authentication authentication, @PathVariable String id, @PathVariable String idWay) {
        service.deleteWay(id, idWay);
    }
}