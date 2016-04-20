package fr.istic.sit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.sit.dao.UserRepository;
import fr.istic.sit.domain.User;

@Component
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public User getId(String id) {
		return repository.findOne(id);
	}

	public List<User> getAll() {
		return repository.findAll();
	}

	public void insert(User user) {
		repository.insert(user);
	}

	public void delete(User user) {
		repository.delete(user);
	}

	public void update(User user) {
		repository.save(user);
	}

	public User getByLogin (String login){ return repository.findByLogin(login);}
}
