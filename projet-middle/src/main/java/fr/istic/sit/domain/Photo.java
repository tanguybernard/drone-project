package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by fracma on 5/27/16.
 */
public class Photo {

    @Id
    private String id;
    private String idIntervention;
    private Double latitude;
    private Double longitude;
    private String imageURL;
    private String date;


    public Photo(String id, String idIntervention, Double latitude, Double longitude, String imageURL, String date) {
        this.id = id;
        this.idIntervention = idIntervention;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageURL = imageURL;
        this.date = date;
    }

    public Photo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", idIntervention='" + idIntervention + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imageURL='" + imageURL + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
