package fr.istic.sit.service;

import fr.istic.sit.dao.SIGRepository;
import fr.istic.sit.domain.SIG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/** * * * * * * * * * * * * *
 * Created by FireDroneTeam *
 * * * * * * * * * * * * * **/
@Component
public class SIGService {

	@Autowired
	private SIGRepository repository;

	public SIG getId(String id) {
		return repository.findOne(id);
	}

	public List<SIG> getAll() {
		return repository.findAll();
	}
}
