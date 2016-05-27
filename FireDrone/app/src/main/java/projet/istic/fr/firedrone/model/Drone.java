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
    @SerializedName("idIntervention")
    private String idIntervention = "";
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("addressip")
    private String addressip;
    @SerializedName("port")
    private String port;



    //**  -  -  -   Constructor   -  -  -  **//

    public Drone() {
        this.id = "";
        this.idUser = "";
        this.idIntervention = "";
        this.longitude = "";
        this.latitude = "";
        this.addressip = "";
        this.port = "";
    }

    public Drone(String id, String idUser, String idIntervention, String longitude, String latitude, String addressip, String port) {
        this.id = id;
        this.idUser = idUser;
        this.idIntervention = idIntervention;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressip = addressip;
        this.port = port;
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

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
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

    public String getAddressip() {
        return addressip;
    }

    public void setAddressip(String addressip) {
        this.addressip = addressip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    //**  -  -  -   To String   -  -  -  **//

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", idUser='" + idUser + '\'' +
                ", idIntervention='" + idIntervention + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", addressip='" + addressip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    //**  -  -  -   Equals   -  -  -  **//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drone drone = (Drone) o;

        if (id != null ? !id.equals(drone.id) : drone.id != null) return false;
        if (idUser != null ? !idUser.equals(drone.idUser) : drone.idUser != null) return false;
        if (idIntervention != null ? !idIntervention.equals(drone.idIntervention) : drone.idIntervention != null)
            return false;
        if (longitude != null ? !longitude.equals(drone.longitude) : drone.longitude != null)
            return false;
        if (latitude != null ? !latitude.equals(drone.latitude) : drone.latitude != null)
            return false;
        if (addressip != null ? !addressip.equals(drone.addressip) : drone.addressip != null)
            return false;
        return !(port != null ? !port.equals(drone.port) : drone.port != null);

    }


    //**  -  -  -   HashCode   -  -  -  **//

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idUser != null ? idUser.hashCode() : 0);
        result = 31 * result + (idIntervention != null ? idIntervention.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (addressip != null ? addressip.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}