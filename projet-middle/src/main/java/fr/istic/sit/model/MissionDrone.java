package fr.istic.sit.model;

import java.util.List;

/**
 * @author Group A.
 */
public class MissionDrone {

    //**     -      Attributes      -     **//

    private String mode;
    private List<PointMissionDrone> points;


    //**     -      Constructor      -     **//

    public MissionDrone() {
    }

    public MissionDrone(String mode, List<PointMissionDrone> points) {
        this.mode = mode;
        this.points = points;
    }


    //**     -      Getters & Setters      -     **//

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
        return "mission{" + points + '}';
    }
}
