package fr.istic.sit.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.istic.sit.domain.Sinister;

/**
 * Created by fracma on 3/18/16.
 */
public interface SinisterRepository extends MongoRepository<Sinister, String> {
	Sinister findById(String id);
    List<Sinister> findByCode(String code);
}