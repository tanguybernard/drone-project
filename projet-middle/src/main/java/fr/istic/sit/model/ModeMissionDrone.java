package fr.istic.sit.model;

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

    public static ModeMissionDrone getModeFromString(String value){

        for (ModeMissionDrone mode: ModeMissionDrone.values()) {
            if (mode.getValue().equals(value))
                return mode;
        }


        /*if ( value == ZONE.getValue() ) {
            return ZONE;
        }
        else if ( value == SEGMENT.getValue() ) {
            return SEGMENT;
        }
        else if ( value == LOOP.getValue() ) {
            return LOOP;
        }
        else {
            return null;
        }*/
        return null;
    }
}
