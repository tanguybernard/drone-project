package fr.istic.sit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.sit.dao.WayRepository;
import fr.istic.sit.domain.Way;

@Component
public class WayService {

	@Autowired
	private WayRepository repository;
	
	public Way getId(String id) {
		return repository.findOne(id);
	}

	public List<Way> getAll() {
		return repository.findAll();
	}

	public void insert(Way way) {
		repository.insert(way);
	}

	public void delete(Way way) {
		repository.delete(way);
	}

	public void update(Way way) {
		repository.save(way);
	}
}
