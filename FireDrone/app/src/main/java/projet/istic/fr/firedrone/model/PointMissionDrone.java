package projet.istic.fr.firedrone.model;

/**
 * @author Group A.
 */
public class PointMissionDrone {


    //**     -      Attributes      -     **//

    private double latitude;
    private double longitude;

    //**     -      Constructor      -     **//

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

    @Override
    public String toString() {
        return "PointMissionDrone{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
