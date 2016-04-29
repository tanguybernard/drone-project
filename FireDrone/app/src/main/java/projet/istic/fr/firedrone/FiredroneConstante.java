package projet.istic.fr.firedrone;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by ramage on 22/04/16.
 */
public class FiredroneConstante {
    public static final String END_POINT= "http://m2gla-drone.istic.univ-rennes1.fr:8080";
    //public static final String END_POINT= "http://ec2-52-31-150-254.eu-west-1.compute.amazonaws.com:8080";
    public static final SimpleDateFormat DATE_FORMAT= new SimpleDateFormat("yyyyMMdd HHmm");

    public static final String ROLE_CODIS = "ROLE_CODIS";
    public static final String ROLE_COS = "ROLE_COS";
    public static final String ROLE_SIT = "ROLE_SIT";

    public static Toast getToastError(Context context){
        return Toast.makeText(context,"Impossible d'accéder au serveur",Toast.LENGTH_LONG);
    }


}


