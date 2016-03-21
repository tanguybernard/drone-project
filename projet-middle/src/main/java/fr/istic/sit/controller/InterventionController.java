package fr.istic.sit.controller;

import fr.istic.sit.dao.InteventionRepository;
import fr.istic.sit.domain.Intervention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by fracma on 3/18/16.
 */

@RestController
@RequestMapping("/intervention")
public class InterventionController {

    @Autowired
    InteventionRepository inteventionRepository;

    @RequestMapping("/{idIntervention}")
    public Intervention getIntervention(@PathVariable String idIntervention) {
        System.out.println("getIntervention... ");
        return inteventionRepository.findOne(idIntervention);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Intervention getIntervention(@RequestBody Intervention intervention) {
        System.out.println("setIntervention... "+intervention);
        return inteventionRepository.save(intervention);
    }

}
