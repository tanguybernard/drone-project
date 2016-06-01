package projet.istic.fr.firedrone.map;

/**
 * @author Group A.
 */
public enum ModeMissionDrone {

    NONE("MODE_NONE"),
    LOOP("MODE_LOOP"), SEGMENT("MODE_SEGMENT"), ZONE("MODE_ZONE"), EXCLUSION("MODE_EXCLUSION");

    private String value;

    ModeMissionDrone(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
