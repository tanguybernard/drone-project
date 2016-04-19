package fr.istic.sit.dao;

import fr.istic.sit.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fracma on 3/14/16.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByFirstname(String firstName);
    List<User> findByLastname(String lastName);
    User findByLogin(String login);
}
