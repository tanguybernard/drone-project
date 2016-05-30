package projet.istic.fr.firedrone.model;

import android.graphics.Point;

import java.util.List;

/**
 * @author Group A.
 */
public class MissionDrone {

    //**     -      Attributes      -     **//

    private String Mode;
    private List<PointMissionDrone> points;


    //**     -      Constructor      -     **//

    public MissionDrone() {
    }

    public MissionDrone(String mode, List<PointMissionDrone> points) {
        Mode = mode;
        this.points = points;
    }


    //**     -      Getters & Setters      -     **//

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public List<PointMissionDrone> getPoints() {
        return points;
    }

    public void setPoints(List<PointMissionDrone> points) {
        this.points = points;
    }



    //**     -      Getters & Setters      -     **//

    @Override
    public String toString() {
        return "MissionDrone{" +
                "Mode='" + Mode + '\'' +
                ", points=" + points +
                '}';
    }
}
