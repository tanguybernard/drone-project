package fr.istic.sit.service;

import fr.istic.sit.dao.WayRepository;
import fr.istic.sit.domain.Way;
import fr.istic.sit.domain.WayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FireDroneTeam
 */

@Component
public class WayService {
    @Autowired
    WayRepository wayRepository;

    public List<WayType> getAll(){
        return wayRepository.findAll();
    }
}
