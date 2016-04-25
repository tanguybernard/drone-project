package fr.istic.sit.dao;

import fr.istic.sit.domain.Sig;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * @author FireDroneTeam
 */
public interface SIGRepository extends MongoRepository<Sig, String> {
	Sig findById(String id);
}