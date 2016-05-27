package fr.istic.sit.domain;

import java.util.List;

import fr.istic.sit.model.InterventionWay;
import fr.istic.sit.util.Validator;
import org.springframework.data.annotation.Id;

/**
 * @author FireDroneTeam
 */
public class Intervention {
    @Id
    private String id;
    private String sinisterCode;
    private String date;
    private String address;
    private Double latitude;
    private Double longitude;
    private String status;
    private Cos cos;
    private List<Way> ways;
    private List <Ressource> ressources;
    private List <Drone>  drones;

    public Intervention() {}

    public Intervention(String id, String sinisterCode, String date, String address, Double latitude, Double longitude, String status, Cos cos, List<Way> ways, List<Ressource> ressources, List<Drone> drones) {
        this.id = id;
        this.sinisterCode = sinisterCode;
        this.date = date;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.cos = cos;
        this.ways = ways;
        this.ressources = ressources;
        this.drones = drones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSinisterCode() {
		return sinisterCode;
	}

	public void setSinisterCode(String sinisterCode) {
		this.sinisterCode = sinisterCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public Cos getCos() {
        return cos;
    }

    public void setCos(Cos cos) {
        this.cos = cos;
    }

    public List<Way> getWays() {
		return ways;
	}

	public void setWays(List<Way> ways) {
		this.ways = ways;
	}

    public List<Ressource> getRessources() {
        return ressources;
    }

    public void setRessources(List<Ressource> ressources) {
        this.ressources = ressources;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }

    public void update(Intervention newData){
        if (!Validator.isEmpty(newData.getAddress()))
            this.address = newData.getAddress();

        if(!Validator.isEmpty(newData.getDate()))
            this.date = newData.getDate();

        if(!Validator.isEmpty(newData.getLatitude()))
            this.latitude = newData.getLatitude();

        if(!Validator.isEmpty(newData.getLongitude()))
            this.longitude = newData.getLongitude();

        if(!Validator.isEmpty(newData.getSinisterCode()))
            this.sinisterCode = newData.getSinisterCode();

        if(!Validator.isEmpty(newData.getStatus()))
            this.status = newData.getStatus();

        if(!newData.getWays().isEmpty()){
            for(Way way : newData.getWays()){
                //If is a new way
                if(Validator.isEmpty(way.getId())){
                    way.setId(Integer.toString(this.getWays().size()+1));
                    this.getWays().add(way);
                }else{
                    for(Way wayIntervention : this.getWays()){
                        if(wayIntervention.getId().equalsIgnoreCase(way.getId())){
                            wayIntervention.update(way);
                            break;
                        }
                    }
                }

            }
        }
    }

    public InterventionWay toInterventionWay() {
        InterventionWay intWay = new InterventionWay();
        intWay.setId(this.id);
        intWay.setAddress(this.address);
        intWay.setDate(this.date);
        intWay.setSinisterCode(this.sinisterCode);
        intWay.setWays(this.ways);

        return intWay;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "id='" + id + '\'' +
                ", sinisterCode='" + sinisterCode + '\'' +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", status='" + status + '\'' +
                ", cos=" + cos +
                ", ways=" + ways +
                ", ressources=" + ressources +
                ", drones=" + drones +
                '}';
    }
}
