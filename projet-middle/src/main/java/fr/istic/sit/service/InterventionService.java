package fr.istic.sit.service;

import java.util.List;

import fr.istic.sit.domain.Way;
import fr.istic.sit.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.sit.dao.InterventionRepository;
import fr.istic.sit.domain.Intervention;

/**
 * @author FireDroneTeam
 */

@Component
public class InterventionService {

	@Autowired
	private InterventionRepository repository;
	
	public Intervention getId(String id) {
		return repository.findOne(id);
	}

	public List<Intervention> getInterventions(String status) {
		try {
			if (!Validator.isEmpty(status))
				return repository.findByStatus(status);

			return repository.findAll();
		}catch (Exception e){
			e.printStackTrace();
		}

		return  null;
	}

	public Intervention insert(Intervention intervention) {
		//Ajouter les id's des moyens
		for(int i=0; i<intervention.getWays().size(); i++){
			intervention.getWays().get(i).setId(Integer.toString(i+1));
		}
		return repository.insert(intervention);
	}

	public void delete(Intervention intervention) {
		repository.delete(intervention);
	}

	public void update(Intervention intervention) {
		repository.save(intervention);
	}

	public Intervention addWay(String id, Way way){
		Intervention intervention = repository.findById(id);
		way.setId(Integer.toString(intervention.getWays().size()+1));
		intervention.getWays().add(way);
		return repository.save(intervention);
	}

	public Intervention editWay(String id, Way way){
		Intervention intervention = repository.findById(id);

		intervention.getWays()
				.stream()
				.filter(w -> w.getId().equalsIgnoreCase(way.getId()) )
				.forEach(w ->  w.update(way) );

		return repository.save(intervention);
	}

	public Intervention deleteWay(String id, String idWay){
		Intervention intervention = repository.findById(id);
		intervention.getWays().removeIf(w -> w.getId().equalsIgnoreCase(idWay));
		return repository.save(intervention);
	}
}
