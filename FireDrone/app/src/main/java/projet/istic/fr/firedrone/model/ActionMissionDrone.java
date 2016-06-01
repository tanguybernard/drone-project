package projet.istic.fr.firedrone.model;


/**
 * @author Group A.
 */
public class ActionMissionDrone {

    //**     -      Attributes      -     **//
    private String idIntervention;
    private Drone drone;
    private MissionDrone mission;
    /**  Indicates if the Action is START or STOP  **/
    private boolean start;


    //**     -      Constructor      -     **//

    public ActionMissionDrone(String idIntervention, Drone drone, MissionDrone mission, boolean start) {
        this.idIntervention = idIntervention;
        this.drone = drone;
        this.mission = mission;
        this.start = start;
    }


    //**     -      Getters & Setters      -     **//


    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public MissionDrone getMission() {
        return mission;
    }

    public void setMission(MissionDrone mission) {
        this.mission = mission;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }


    //**     -      ToString      -     **//


    @Override
    public String toString() {
        return "ActionMissionDrone{" +
                "idIntervention='" + idIntervention + '\'' +
                ", drone=" + drone +
                ", mission=" + mission +
                ", start=" + start +
                '}';
    }
}

