package projet.istic.fr.firedrone.model;


/**
 * @author Group A.
 */
public class StartMissionDrone {

    //**     -      Attributes      -     **//
    private String idIntervetion;
    private String idDrone;
    private MissionDrone mission;


    //**     -      Constructor      -     **//

    public StartMissionDrone(String idIntervetion, String idDrone, MissionDrone mission) {
        this.idIntervetion = idIntervetion;
        this.idDrone = idDrone;
        this.mission = mission;
    }


    //**     -      Getters & Setters      -     **//
    public String getIdIntervetion() {
        return idIntervetion;
    }

    public void setIdIntervetion(String idIntervetion) {
        this.idIntervetion = idIntervetion;
    }

    public String getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(String idDrone) {
        this.idDrone = idDrone;
    }

    public MissionDrone getMission() {
        return mission;
    }

    public void setMission(MissionDrone mission) {
        this.mission = mission;
    }

    //**     -      ToString      -     **//
    @Override
    public String toString() {
        return "StartMissionDrone{" +
                "idIntervetion='" + idIntervetion + '\'' +
                ", idDrone='" + idDrone + '\'' +
                ", mission=" + mission +
                '}';
    }
}
