package fr.istic.sit.domain;

import fr.istic.sit.util.Validator;

/**
 * @author FireDroneTeam
 */
public class Drone {
    private String id;
    private String idUser;
    private String name;
    private String longitude;
    private String latitude;
    private Double battery;
    private String ip;
    private String port;

    public Drone() {
    }

    public Drone(String id, String idUser, String name, String longitude, String latitude, Double battery, String ip, String port) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.battery = battery;
        this.ip = ip;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getBattery() {
        return battery;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void update(Drone newDrone){
        if( !Validator.isEmpty(newDrone.getLongitude()))
            this.longitude = newDrone.getLongitude();
        if( !Validator.isEmpty(newDrone.getBattery()))
            this.battery = newDrone.getBattery();
        if( !Validator.isEmpty(newDrone.getIdUser()))
            this.idUser = newDrone.getIdUser();
        if( !Validator.isEmpty(newDrone.getLatitude()))
            this.latitude = newDrone.getLatitude();
        if( !Validator.isEmpty(newDrone.getIp()))
            this.ip = newDrone.getIp();
        if( !Validator.isEmpty(newDrone.getPort()))
            this.port = newDrone.getPort();
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", battery=" + battery +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
