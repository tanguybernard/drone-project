package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

/**
 * @author FireDroneTeam
 * Geographical Information System
 */

public class Sig {

    //** **//
    private static final String WATER="WATER";
    private static final String HYDRANT="HYDRANT";
    private static final String CHEMICALS="CHEMICALS";

    @Id
    private String id;
    private String type;
    private String latitude;
    private String longitude;
    private String name;

    public Sig() {}

    public Sig(String id, String type, String latitude, String longitude) {
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sig{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
