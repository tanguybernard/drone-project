package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.Sinister;
import fr.istic.sit.domain.Way;
import fr.istic.sit.service.SinisterService;

/**
 * Created by fracma on 3/18/16.
 */

@RestController
@RequestMapping("/sinister")
public class SinisterController {

    @Autowired
    private SinisterService service;
    
    @RequestMapping("/{id}")
    public Sinister searchSinister(@PathVariable String id) {
    	return service.getId(id);
    }
    
    @RequestMapping("")
    public List<Sinister> sinisters() {
    	return service.getAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void insertSinister(@RequestBody Sinister sinister){
    	service.insert(sinister);
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    public void updateSinister(@RequestBody Sinister sinister){
    	service.update(sinister);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void deleteSinister(@PathVariable String id) {
    	service.delete(service.getId(id));
    }
}