package projet.istic.fr.firedrone.model;

import com.fasterxml.jackson.core.io.SegmentedStringWriter;

/**
 * @author Group A.
 */
public enum ModeMissionDrone {

    ZONE("MODE_ZONE"),
    SEGMENT("MODE_SEGMENT"),
    LOOP("MODE_LOOP");


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ModeMissionDrone(String value) {
        this.value = value;
    }
}
