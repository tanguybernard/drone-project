package fr.istic.sit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.sit.dao.SinisterRepository;
import fr.istic.sit.domain.Sinister;

/**
 * @author FireDroneTeam
 */

@Component
public class SinisterService {

	@Autowired
	private SinisterRepository repository;
	
	public Sinister getId(String id) {
		return repository.findOne(id);
	}

	public List<Sinister> getAll() {
		return repository.findAll();
	}

	public void insert(Sinister sinister) {
		repository.insert(sinister);
	}

	public void delete(Sinister sinister) {
		repository.delete(sinister);
	}

	public void update(Sinister sinister) {
		repository.save(sinister);
	}
}
