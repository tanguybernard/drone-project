package fr.istic.sit.dao;

import fr.istic.sit.domain.Intervention;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author FireDroneTeam
 */
public interface InterventionRepository extends MongoRepository<Intervention, String> {
	Intervention findById(String id);
    List<Intervention> findByStatus(String status);
}