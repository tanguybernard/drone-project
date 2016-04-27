package fr.istic.sit.dao;

import fr.istic.sit.domain.WayType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author FireDroneTeam
 */
public interface WayRepository  extends MongoRepository<WayType, String> {
}
