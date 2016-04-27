package fr.istic.sit.service;

import java.util.ArrayList;
import java.util.List;

import fr.istic.sit.dao.UserRepository;
import fr.istic.sit.domain.Cos;
import fr.istic.sit.domain.User;
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

	@Autowired
	private UserRepository userRepository;
	
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
		if(intervention.getWays() == null)
			intervention.setWays(new ArrayList<Way>());

		if(intervention.getWays().isEmpty()){
			way.setId("1");
		}else {
			way.setId(Integer.toString(intervention.getWays().size() + 1));
		}

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

	public Cos getCos(String id){
		Cos cos = repository.findById(id).getCos();
		if(cos == null)
			return new Cos();
		else return cos;
	}

	public Intervention setCos(String id, String login){
		Intervention intervention = repository.findById(id);
		Cos cosObj = new Cos();
		//e
		//if (intervention.getCos()!=null)
		User cos = userRepository.findByLogin(login);
		if(cos != null){
			cosObj.setId(cos.getId());
			cosObj.setEmail(cos.getEmail());
			cosObj.setFirstname(cos.getFirstname());
			cosObj.setLastname(cos.getLastname());
			cosObj.setPhone(cos.getPhone());
			cosObj.setLogin(cos.getLogin());
		}

		intervention.setCos(cosObj);
		return repository.save(intervention);

	}

	public Intervention deleteCos(String id){
		Intervention intervention = repository.findById(id);
		intervention.setCos(null);
		return repository.save(intervention);
	}
}
