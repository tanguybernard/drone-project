package fr.istic.sit.model;


import fr.istic.sit.domain.Drone;

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

    public ActionMissionDrone(){

    }

    public ActionMissionDrone(String idIntervention, Drone drone, MissionDrone mission, boolean start) {
        this.idIntervention = idIntervention;
        this.drone = drone;
        this.mission = mission;
        this.start = start;

        System.out.println();
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


    public String toJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("'{");
        builder.append("\"mission\" :");
        builder.append("[");
        boolean firstRound = true;
        for(PointMissionDrone pointMissionDrone: mission.getPoints()){
            if(firstRound) {
                firstRound = !firstRound;
            }
            else {
                builder.append(", ");
            }
            builder.append(pointMissionDrone.toJson());
        }
        builder.append("]");
        builder.append("}'");

        return builder.toString();
    }

    public String stringCommand(){
        StringBuilder builder = new StringBuilder();
        builder.append("'(");
        boolean firstRound = true;
        for(PointMissionDrone pointMissionDrone: mission.getPoints()){
            if(firstRound) {
                firstRound = !firstRound;
            }
            else {
                builder.append(",");
            }
            builder.append(pointMissionDrone.stringCommand());
        }
        builder.append(")'");

        return builder.toString();
    }
}
