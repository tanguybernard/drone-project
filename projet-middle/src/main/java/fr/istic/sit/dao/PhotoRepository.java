package fr.istic.sit.dao;


import fr.istic.sit.domain.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PhotoRepository extends MongoRepository<Photo, String> {
    List<Photo> findByIdIntervention(String idIntervention);

    List<Photo> findByIdInterventionAndLatitudeAndLongitude(String idIntervention, Double latitude, Double longitude);
}
