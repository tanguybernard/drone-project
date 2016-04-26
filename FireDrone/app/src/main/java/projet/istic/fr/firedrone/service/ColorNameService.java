package projet.istic.fr.firedrone.service;

/**
 * Created by tbernard on 26/04/16.
 */
public class ColorNameService {



    public static String getColor(String name){

        String hexaColor;
        switch (name){

            case "INCENDIE":
                hexaColor = "#FFCC0000";
                break;
            case "SAP":
                hexaColor = "#FF669900";
                break;
            case "EAU":
                hexaColor = "#FF33B5E5";
                break;
            default: hexaColor = "#FFFFFF";
                break;

        }
        return hexaColor;




    }


}
