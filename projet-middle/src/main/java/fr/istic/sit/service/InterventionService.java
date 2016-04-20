package fr.istic.sit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.sit.dao.InterventionRepository;
import fr.istic.sit.domain.Intervention;

@Component
public class InterventionService {

	@Autowired
	private InterventionRepository repository;
	
	public Intervention getId(String id) {
		return repository.findOne(id);
	}

	public List<Intervention> getAll() {
		return repository.findAll();
	}

	public void insert(Intervention intervention) {
		repository.insert(intervention);
	}

	public void delete(Intervention intervention) {
		repository.delete(intervention);
	}

	public void update(Intervention intervention) {
		repository.save(intervention);
	}
}
