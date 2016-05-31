package projet.istic.fr.firedrone.model;


import com.google.gson.annotations.SerializedName;


/**
 * @author Group A
 */
public class Drone {

    @SerializedName("id")
    private String id = "";
    @SerializedName("idUser")
    private String idUser = "";
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("ip")
    private String ip;
    @SerializedName("port")
    private String port;
    @SerializedName("battery")
    private int battery;
    @SerializedName("name")
    private String name;



    //**  -  -  -   Constructor   -  -  -  **//

    public Drone() {
        this.id = "";
        this.idUser = "";
        this.latitude = "";
        this.longitude = "";
        this.ip = "";
        this.port = "";
        this.battery = 0;
        this.name = "";

    }

    public Drone(String id, String idUser, String longitude, String latitude, String ip, String port, int battery, String name) {
        this.id = id;
        this.idUser = idUser;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ip = ip;
        this.port = port;
        this.battery = battery;
        this.name = name;
    }

    //**  -  -  -   Getter & Setters   -  -  -  **//

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

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //**  -  -  -   To String   -  -  -  **//

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", idUser='" + idUser + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", battery=" + battery +
                ", name='" + name + '\'' +
                '}';
    }


    //**  -  -  -   Equals   -  -  -  **//

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drone drone = (Drone) o;

        if (battery != drone.battery) return false;
        if (!id.equals(drone.id)) return false;
        if (!idUser.equals(drone.idUser)) return false;
        if (!longitude.equals(drone.longitude)) return false;
        if (!latitude.equals(drone.latitude)) return false;
        if (!ip.equals(drone.ip)) return false;
        if (!port.equals(drone.port)) return false;
        return name.equals(drone.name);

    }



    //**  -  -  -   HashCode   -  -  -  **//


    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + longitude.hashCode();
        result = 31 * result + latitude.hashCode();
        result = 31 * result + ip.hashCode();
        result = 31 * result + port.hashCode();
        result = 31 * result + battery;
        result = 31 * result + name.hashCode();
        return result;
    }

}