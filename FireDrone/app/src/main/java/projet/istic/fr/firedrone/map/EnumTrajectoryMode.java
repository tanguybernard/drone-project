package projet.istic.fr.firedrone.map;

/**
 * @author Group A.
 */
public enum EnumTrajectoryMode {

    NONE("NONE"),
    LOOP("LOOP"), SEGMENT("SEGMENT"), ZONE("ZONE"), EXCLUSION("EXCLUSION");

    private String value;

    EnumTrajectoryMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
