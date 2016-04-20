package fr.istic.sit.dao;

import fr.istic.sit.domain.Intervention;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by fracma on 3/18/16.
 */
public interface InterventionRepository extends MongoRepository<Intervention, String> {
	Intervention findById(String id);
    List<Intervention> findByCode(String code);
}