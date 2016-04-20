package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.Way;
import fr.istic.sit.service.WayService;

/**
 * Created by fracma on 3/18/16.
 */

@RestController
@RequestMapping("/way")
public class WayController {

    @Autowired
    private WayService service;
    
    @RequestMapping("/{id}")
    public Way searchWay(@PathVariable String id) {
    	return service.getId(id);
    }
    
    @RequestMapping("")
    public List<Way> ways() {
    	return service.getAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void insertWay(@RequestBody Way way){
    	service.insert(way);
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    public void updateWay(@RequestBody Way way){
    	service.update(way);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void deleteWay(@PathVariable String id) {
    	service.delete(service.getId(id));
    }
}