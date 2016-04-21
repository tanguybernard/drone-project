package fr.istic.sit.dao;

import fr.istic.sit.domain.SIG;
import org.springframework.data.mongodb.repository.MongoRepository;


/** * * * * * * * * * * * * *
 * Created by FireDroneTeam *
 * * * * * * * * * * * * * **/
public interface SIGRepository extends MongoRepository<SIG, String> {
	SIG findById(String id);
}