package fr.istic.sit.controller;

import fr.istic.sit.annotation.Notification;
import fr.istic.sit.annotation.ParamNotification;
import fr.istic.sit.domain.*;
import fr.istic.sit.model.InterventionWay;
import fr.istic.sit.service.InterventionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FireDroneTeam
 */

@RestController
@RequestMapping("/intervention")
@Api(value = "Intervention", description = "Intervention API", produces = "application/json")
public class InterventionController {

    @Autowired
    private InterventionService service;


    // -------------------------------------  GET  ----------------------------------//

    @RequestMapping(value= "/{id}/ressource" , method = RequestMethod.GET)
    @ApiOperation(value = "Search ressource of an intervention", nickname = "RessourcesOfIntervention", response = Ressource.class )
    public List<Ressource> searchRessourcesOfIntervention(OAuth2Authentication authentication, @PathVariable String id) {
        return service.getId(id).getRessources();
    }

    @RequestMapping(value= "/{id}/way" , method = RequestMethod.GET)
    @ApiOperation(value = "Search ways of an intervention", nickname = "WaysOfIntervention", response = Way.class )
    public List<Way> searchWaysOfIntervention(OAuth2Authentication authentication, @PathVariable String id, @RequestParam (value = "status" , required = false) String status) {
        return service.getWaysInterventions(id, status);
    }

    @RequestMapping(value= "/{id}/drone" , method = RequestMethod.GET)
    @ApiOperation(value = "Search drones of an intervention", nickname = "DronesOfIntervention", response = Drone.class )
    public List<Drone> searchDronesOfIntervention(OAuth2Authentication authentication, @PathVariable String id) {
        return service.getDrones(id);
    }

    @RequestMapping(value="/{id}" , method = RequestMethod.GET)
    @ApiOperation(value = "Search an intervention", nickname = "Intervention", response = Intervention.class )
    public Intervention searchIntervention(OAuth2Authentication authentication, @PathVariable String id) {
    	return service.getId(id);
    }

    @RequestMapping(value="/way" , method = RequestMethod.GET)
    @ApiOperation(value = "List of ways of all interventions. Optional parameter: statusWay", nickname = "WaysALLIntervention", response = Intervention.class )
    public List<InterventionWay> searchWaysAllIntervention(OAuth2Authentication authentication, @RequestParam (value = "statusWay" , required = false) String statusWay) {
        return service.getWaysAllInterventions(statusWay);
    }

    @RequestMapping(value= "/{id}/cos" , method = RequestMethod.GET)
    @ApiOperation(value = "Get COS of an intervention", nickname = "GetCOSOfIntervention", response = Intervention.class )
    public Cos getCos(OAuth2Authentication authentication, @PathVariable String id) {
        return service.getCos(id);
    }


    @RequestMapping(value="" , method = RequestMethod.GET)
    @ApiOperation(value = "List of intervention. Optional parameter: status", nickname = "Interventions", response = Intervention.class )
    public List<Intervention> interventions(OAuth2Authentication authentication, @RequestParam (value = "status" , required = false) String status) {
        return service.getInterventions(status);
    }

    // -------------------------------------  POST  ----------------------------------//

    //@RolesAllowed({"ROLE_CODIS"})
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Insert intervention", nickname = "New intervention", response = Intervention.class )
    public Intervention insertIntervention(OAuth2Authentication authentication, @RequestBody Intervention intervention){
    	return service.insert(intervention);
    }

    @Notification
    @RequestMapping(value= "/{id}/way" , method = RequestMethod.POST)
    @ApiOperation(value = "Add a way of an intervention", nickname = "AddWayOfIntervention", response = Way.class )
    public List<Way> addWayToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Way way) {
        return service.addWay(id,way).getWays();
    }

    @Notification
    @RequestMapping(value= "/{id}/ressource" , method = RequestMethod.POST)
    @ApiOperation(value = "Add a ressource of an intervention", nickname = "AddRessourceOfIntervention", response = Ressource.class )
    public List<Ressource> addRessourceToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Ressource ressource) {
        return service.addRessource(id,ressource).getRessources();
    }

    @Notification
    @RequestMapping(value= "/{id}/drone" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a drone of an intervention", nickname = "AddDroneOfIntervention", response = Drone.class )
    public Drone addDroneToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Drone drone) {
        if(authentication != null)
            drone.setIdUser(((User)authentication.getPrincipal()).getUsername());
        return service.addDrone(id, drone);
    }

    @RequestMapping(value= "/{id}/cos" , method = RequestMethod.POST)
    @ApiOperation(value = "Set current user as COS of an intervention", nickname = "SetCOSOfIntervention", response = Intervention.class )
    public Intervention setCos(OAuth2Authentication authentication, @PathVariable String id) {
        if(authentication != null)
            return  service.setCos(id, ((User)authentication.getPrincipal()).getUsername());
        else
            return service.getId(id);
    }

    // -------------------------------------  PATCH  ----------------------------------//

    @Notification
    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit intervention", nickname = "Edit intervention")
    public void updateIntervention(OAuth2Authentication authentication, @RequestBody Intervention intervention){
    	service.update(intervention);
    }

    @Notification
    @RequestMapping(value= "/{id}/way" , method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit a way of an intervention", nickname = "EditWayOfIntervention", response = Way.class )
    public List<Way> updateWayToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Way way) {
        return service.editWay(id,way).getWays();
    }

    @Notification
    @RequestMapping(value= "/{id}/ressource" , method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit a ressource of an intervention", nickname = "EditWayOfIntervention", response = Ressource.class )
    public List<Ressource> updateRessourceToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Ressource ressource) {
        return service.editRessource(id,ressource).getRessources();
    }

    @Notification
    @RequestMapping(value= "/{id}/drone" , method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit a drone of an intervention", nickname = "EditDroneOfIntervention", response = Drone.class )
    public Drone updateDroneToIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @RequestBody Drone drone) {
        return service.editDrone(id, drone);
    }

    // -------------------------------------  DELETE  ----------------------------------//

    @Notification
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    @ApiOperation(value = "Delete an intervention", nickname = "DeleteIntervention" )
    public void deleteIntervention(OAuth2Authentication authentication,  @ParamNotification(value ="message") @PathVariable String id) {
    	service.delete(service.getId(id));
    }

    @Notification
    @RequestMapping(method=RequestMethod.DELETE, value="{id}/way/{idWay}")
    @ApiOperation(value = "Delete a way of an intervention", nickname = "DeleteWayIntervention" )
    public void deleteWayIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @PathVariable String idWay) {
        service.deleteWay(id, idWay);
    }

    @Notification
    @RequestMapping(method=RequestMethod.DELETE, value="{id}/ressource/{idRessource}")
    @ApiOperation(value = "Delete a ressource of an intervention", nickname = "DeleteRessourceIntervention" )
    public void deleteRessourceIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @PathVariable String idRessource) {
        service.deleteRessource(id, idRessource);
    }

    @Notification
    @RequestMapping(method=RequestMethod.DELETE, value="{id}/drone/{idDrone}")
    @ApiOperation(value = "Delete a drone of an intervention", nickname = "DeleteDroneIntervention" )
    public void deleteDroneIntervention(OAuth2Authentication authentication, @ParamNotification @PathVariable String id, @PathVariable String idDrone) {
        service.deleteDrone(id, idDrone);
    }

    @RequestMapping(value= "/{id}/cos" , method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete current user as COS of an intervention", nickname = "DeleteCOSOfIntervention", response = Intervention.class )
    public Intervention deleteCos(OAuth2Authentication authentication, @PathVariable String id) {
        return service.deleteCos(id);
    }

}