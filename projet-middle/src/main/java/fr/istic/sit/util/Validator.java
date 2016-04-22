package fr.istic.sit.util;

/**
 * @author fracma
 */
public class Validator {

    public static boolean isEmpty(String data){
        if( data == null || data.isEmpty() )
            return true;
        else
            return false;
    }
}
