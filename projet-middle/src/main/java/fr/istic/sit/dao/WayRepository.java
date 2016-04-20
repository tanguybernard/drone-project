package fr.istic.sit.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.istic.sit.domain.Way;

/**
 * Created by fracma on 3/18/16.
 */
public interface WayRepository extends MongoRepository<Way, String> {
	Way findById(String id);
    List<Way> findByCode(String code);
    List<Way> findByName(String name);
}