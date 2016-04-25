package fr.istic.sit.service;

import fr.istic.sit.dao.SIGRepository;
import fr.istic.sit.domain.Sig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FireDroneTeam
 */

@Component
public class SIGService {

	@Autowired
	private SIGRepository repository;

	public Sig getId(String id) {
		return repository.findOne(id);
	}

	public List<Sig> getAll() {
		return repository.findAll();
	}
}
