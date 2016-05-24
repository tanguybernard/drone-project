package projet.istic.fr.firedrone.model;

/**
 * @author Group A
 * ENUM CLASS
 * Define all diferent STATUS of a MEAN on a INTERVENTION
 */
public enum MeansItemStatus {

    /**  -   -   -  Value  -  -  -  **/

    /** **/
    STATUS_DEMANDE("D", "Demandé"),
    STATUS_VALIDE("V", "Validé"),
    STATUS_ARRIVE("A", "Arrivé"),
    STATUS_ENGAGE("E", "Engagé"),
    STATUS_ENTRANSIT("T", "En Transit"),
    STATUS_LIBERE("L", "Libéré"),
    STATUS_DEFAULT("X", "ERROR");


    private final String state;
    private final String description;


    /**
     * Default constructor
     * @param aState
     * @param desc
     */
    MeansItemStatus(String aState, String desc){
        this.state = aState;
        this.description = desc;
    }

    public String state(){
        return this.state;
    }

    public String description(){
        return this.description;
    }
    

}
