package fr.istic.sit.model;

/**
 * @author Group A.
 */
public class PointMissionDrone {


    //**     -      Attributes      -     **//

    private double latitude;
    private double longitude;

    //**     -      Constructor      -     **//

    public PointMissionDrone(){

    }

    public PointMissionDrone(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //**     -      Getters & Setters      -     **//

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    //**     -      ToString      -     **//


    public String toJson() {
        return '{' +
                "\"latitude\" : " + latitude +
                ", \"longitude\" :" + longitude +
                '}';
    }

    public String stringCommand() {
        return "(" + latitude + "," + longitude + ")";
    }
}
