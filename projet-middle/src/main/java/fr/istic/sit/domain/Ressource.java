package fr.istic.sit.domain;

import fr.istic.sit.util.Validator;

/**
 * @author FireDroneTeam
 */
public class Ressource {
    private String id;
    private String type;
    private Double latitude;
    private Double longitude;

    public Ressource() {
    }

    public Ressource(String id, String type, Double latitude, Double longitude) {
        this.id = id;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void update(Ressource r){
        if( !Validator.isEmpty(r.getLatitude()))
            this.latitude = r.getLatitude();

        if( !Validator.isEmpty(r.getLongitude()))
            this.longitude = r.getLongitude();

        if(!Validator.isEmpty(r.getType()))
            this.type = r.getType();
    }

}
