package fr.istic.sit.util;

/**
 * @author FireDroneTeam
 */
public class Validator {

    public static boolean isEmpty(String data){
        if( data == null || data.isEmpty() )
            return true;
        else
            return false;
    }
}
