package fr.istic.sit.dao;

import fr.istic.sit.domain.Intervention;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by fracma on 3/18/16.
 */
public interface InteventionRepository extends MongoRepository<Intervention, String> {
}
