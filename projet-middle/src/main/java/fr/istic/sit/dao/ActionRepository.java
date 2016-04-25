package fr.istic.sit.dao;

import fr.istic.sit.domain.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author FireDroneTeam
 */
public interface ActionRepository extends MongoRepository<Action, String> {
}
