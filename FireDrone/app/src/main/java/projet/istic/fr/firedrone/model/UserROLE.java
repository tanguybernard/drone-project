package projet.istic.fr.firedrone.model;

/**
 * @author Group A.
 */
public enum UserROLE {

    ROLE_SIT("ROLE_SIT"),
    ROLE_CODIS("ROLE_CODIS"),
    ROLE_BASE("ROLE_BASE");


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    UserROLE(String value) {
        this.value = value;
    }


}
