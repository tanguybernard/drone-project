package fr.istic.sit.service;

import fr.istic.sit.dao.InterventionRepository;
import fr.istic.sit.dao.UserRepository;
import fr.istic.sit.domain.*;
import fr.istic.sit.exception.CustomException;
import fr.istic.sit.model.InterventionWay;
import fr.istic.sit.util.Validator;
import fr.istic.sit.util.WayStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FireDroneTeam
 */

@Component
public class InterventionService {

    private final Logger log = LoggerFactory.getLogger(InterventionService.class);


    @Autowired
    private NotificationSenderService sender;

    @Autowired
    private InterventionRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    DroneService droneService;


    private Integer nextDroneID = 0;
    private String DEFAULT_IP_DRONE="127.0.0.1";
    private Integer DEFAULT_PORT_DRONE_MAV_PROXY = 14550;

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

        //On cree l'id du moyen.
        Date now = new Date();
        Long idWay = now.getTime();
        way.setId(idWay.toString());
		/*if(intervention.getWays().isEmpty()){
			way.setId((new Date()).toString());
		}else {
			way.setId(Integer.toString(intervention.getWays().size() + 1));
		}*/

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
        if(intervention.getWays() != null && ! intervention.getWays().isEmpty()){
            intervention.getWays().removeIf(w -> w.getId().equalsIgnoreCase(idWay));
            return repository.save(intervention);
        }
        return intervention;
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
        Intervention saveIntervention = repository.save(intervention);

        //Send notification
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("idIntervention", id);
        if (cos != null) {
            payload.put("cosIam", cos.getLogin());
        }
        try {
            sender.sendNotification(payload);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error send notification "+e.getMessage());
        }

        return saveIntervention;
    }

    public Intervention deleteCos(String id){
        Intervention intervention = repository.findById(id);

        //Send notification
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("idIntervention", id);
        payload.put("cosFree", intervention.getCos().getLogin());
        try {
            sender.sendNotification(payload);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error send notification "+e.getMessage());
        }

        intervention.setCos(null);
        return repository.save(intervention);
    }

    public Intervention addRessource(String id, Ressource ressource){
        Intervention intervention = repository.findById(id);
        if(intervention.getRessources() == null)
            intervention.setRessources(new ArrayList<>());

        if(intervention.getRessources().isEmpty()){
            ressource.setId("1");
        }else {
            ressource.setId(Integer.toString(intervention.getRessources().size() + 1));
        }

        intervention.getRessources().add(ressource);
        return repository.save(intervention);
    }

    public Intervention editRessource(String id, Ressource ressource){
        Intervention intervention = repository.findById(id);

        if(intervention.getRessources()!= null) {
            intervention.getRessources()
                    .stream()
                    .filter(r -> r.getId().equalsIgnoreCase(ressource.getId()))
                    .forEach(r -> r.update(ressource));

            return repository.save(intervention);
        }
        return intervention;
    }

    public Intervention deleteRessource(String id, String idRessource){
        Intervention intervention = repository.findById(id);
        if(intervention.getRessources() != null && ! intervention.getRessources().isEmpty()){
            intervention.getRessources().removeIf(r -> r.getId().equalsIgnoreCase(idRessource));
            return repository.save(intervention);
        }
        return intervention;
    }


    public List<Way> getWaysInterventions(String id, String status) {
        Intervention intervention = repository.findById(id);
        List<Way> waysFilter = new ArrayList<>();
        try {
            if (!Validator.isEmpty(status)) {
                //Si on a une valeur pour le status on filtre les moyens par cet donnée
                log.info("getWaysInterventions "+ status);
                waysFilter =intervention.getWays().stream()
                        .filter(w -> w.getStatus().equalsIgnoreCase(status))
                        .collect(Collectors.toList())
                ;
            } else{
                waysFilter = intervention.getWays();
            }
            log.debug("Ways filter-->" + waysFilter.size());

            //avant renvoyer le résultat, on reemplace le status par son définition
            waysFilter.forEach(wf -> wf.setStatus(WayStatus.getDescription(wf.getStatus())));
            return waysFilter;
        }catch (Exception e){
            log.error("ERROR getWaysInterventions"+e.getMessage());
            e.printStackTrace();
        }
        return  null;
    }

    public List<Drone> getDrones(String id){
        return repository.findById(id).getDrones();
    }

    /**
     *
     * @param id
     * @param drone
     * @return new Drone
     */
    public Drone addDrone(String id, Drone drone){

        Intervention intervention = repository.findById(id);
        //Date now = new Date();
        //Long idDrone = now.getTime();


        //On remplace le nom de l'utilisateur par son ID
        drone.setIdUser(userRepository.findByLogin(drone.getIdUser()).getId());
        try {
            if (!userHasDrone(intervention.getDrones(), drone)) {
                //**  Initialize the Drone  **/
                drone.setId(getNextIDDRONE().toString());
                drone.setIp(DEFAULT_IP_DRONE);
                final Integer port = Integer.parseInt(drone.getId()) * 1 + DEFAULT_PORT_DRONE_MAV_PROXY;
                drone.setPort(port.toString());
                drone.setLatitude(intervention.getLatitude().toString());
                drone.setLongitude(intervention.getLongitude().toString());
                drone.setName("Drone d" + drone.getId() + "-" + drone.getPort());

                if (intervention.getDrones() == null)
                    intervention.setDrones(new ArrayList<>());

                intervention.getDrones().add(drone);
                repository.save(intervention);

                droneService.createDrone(drone.getId(), drone.getLatitude(), drone.getLongitude());

                return drone;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        throw new CustomException("D-0001","Impossible d'associer plus d'un drone à un utilisateur");
    }

    public Drone editDrone(String id, Drone drone){
        Intervention intervention = repository.findById(id);
        //Intervention newIntervention  = null;
        //if(!userHasDrone(intervention.getDrones(), drone)) {
        intervention.getDrones()
                .stream()
                .filter(d -> d.getId().equalsIgnoreCase(drone.getId()))
                .forEach(df -> df.update(drone));

        repository.save(intervention);

        if(drone.getBattery()<= 20){
            sendBatteryNotification(intervention.getId(),drone.getId(),drone.getBattery());
        }

        return drone;

        //}
        //throw new CustomException("D-0001","Impossible d'associer plus d'un drone à un utilisateur");
    }

    public Intervention deleteDrone(String id, String idDrone){
        Intervention intervention = repository.findById(id);
        if(intervention.getDrones() != null && ! intervention.getDrones().isEmpty()){
            intervention.getDrones().removeIf(d -> d.getId().equalsIgnoreCase(idDrone));
            return repository.save(intervention);
        }
        return intervention;
    }

    private boolean userHasDrone(List<Drone> drones, Drone drone){
        if(drones.isEmpty())
            return false;

        long count = drones.stream()
                .filter(d -> d.getIdUser().equalsIgnoreCase(drone.getIdUser()))
                .count();

        if(count > 0)
            return true;

        return false;
    }

    private void sendBatteryNotification(String id, String idDrone, Double battery){
        //Send notification
        Map<String, String> payload = new HashMap<>();
        payload.put("idIntervention", id);
        payload.put("message", "Batterie faible Drone: "+idDrone+" % batterie"+battery);

        try {
            sender.sendNotification(payload);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error sendBatteryNotification "+e.getMessage());
        }

    }

    public List<InterventionWay> getWaysAllInterventions(String statusWay) {
        List<Intervention> allInte = repository.findAll();
        List<InterventionWay> intervResponse = new ArrayList<>();
        try {

            if(!Validator.isEmpty(statusWay)) {
                allInte.removeIf(
                        i -> i.getWays()
                                .stream()
                                .filter(w -> w.getStatus().equalsIgnoreCase(statusWay))
                                .count() <= 0
                );

                allInte.forEach(
                        i -> i.getWays()
                                .removeIf(
                                        w -> !w.getStatus().equalsIgnoreCase(statusWay)
                                )
                );
            }




        }catch (Exception e){
            e.printStackTrace();
        }

        allInte.forEach(i -> {
                    //i.setStatus(WayStatus.getDescription(i.getStatus()));
                    i.getWays().forEach(w -> w.setStatus(WayStatus.getDescription(w.getStatus())));
                    intervResponse.add(i.toInterventionWay());
                }
        );

        return intervResponse;
    }


    /**
     * Return an ID for the DRONE
     * Count every Drone in each Intervention in order to know which ID the next Drone will have
     * @return ID DRONE
     */
    public Integer getNextIDDRONE() {
        List<Intervention> allInterventions = repository.findAll();
        allInterventions.stream()
                .filter(i -> i.getDrones() != null)
                .forEach(intervention -> {
                    intervention.getDrones().forEach(drone -> nextDroneID++);
                }
        );

        return nextDroneID;
    }



}
