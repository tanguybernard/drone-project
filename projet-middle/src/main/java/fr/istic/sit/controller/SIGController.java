package fr.istic.sit.controller;

import fr.istic.sit.domain.Sig;
import fr.istic.sit.service.SIGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FireDroneTeam
 */

@RestController
@RequestMapping("/sig")
public class SIGController {

    @Autowired
    private SIGService service;
    
    @RequestMapping(value= "/{id}", method = RequestMethod.GET)
    public Sig searchSIG(@PathVariable String id) {
    	return service.getId(id);
    }
    
    @RequestMapping(value= "", method = RequestMethod.GET)
    public List<Sig> allSIG() {
    	return service.getAll();
    }
    


}