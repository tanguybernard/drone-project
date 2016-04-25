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
    
    @RequestMapping("/{id}")
    public Sig searchSIG(@PathVariable String id) {
    	return service.getId(id);
    }
    
    @RequestMapping("")
    public List<Sig> allSIG() {
    	return service.getAll();
    }
    


}