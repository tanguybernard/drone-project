package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.Intervention;
import fr.istic.sit.domain.Way;
import fr.istic.sit.service.InterventionService;

/**
 * Created by fracma on 3/18/16.
 */

@RestController
@RequestMapping("/intervention")
public class InterventionController {

    @Autowired
    private InterventionService service;
    
    @RequestMapping("/{id}")
    public Intervention searchIntervention(@PathVariable String id) {
    	return service.getId(id);
    }
    
    @RequestMapping("/{id}/way")
    public List<Way> searchWaysOfIntervention(@PathVariable String id) {
    	return service.getId(id).getWays();
    }
    
    @RequestMapping("")
    public List<Intervention> interventions() {
    	return service.getAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void insertIntervention(@RequestBody Intervention intervention){
    	service.insert(intervention);
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    public void updateIntervention(@RequestBody Intervention intervention){
    	service.update(intervention);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void deleteIntervention(@PathVariable String id) {
    	service.delete(service.getId(id));
    }
}