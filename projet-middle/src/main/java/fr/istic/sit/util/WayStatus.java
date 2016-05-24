package fr.istic.sit.util;

/**
 * @author FireDroneTeam
 */
public enum WayStatus {
    REQUESTED ("D","Demandé"),
    VALIDATE ("V","Validé"),
    ARRIVE("A","Arrivé"),
    INVEST("E","Engagé"),
    TRANSIT("T","En transit"),
    FREE ("L","Liberé"),
    DENIED("R", "Refusé");


    private String status;
    private String description;

     WayStatus(String status, String description){
        this.status = status;
        this.description = description;
    }

    public static String getDescription(String status){
        if(!status.isEmpty())
            for (WayStatus s : WayStatus.values()) {
               if(s.status.equalsIgnoreCase(status))
                   return s.description;
            }
        return null;
    }

}
