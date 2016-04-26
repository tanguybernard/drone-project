package projet.istic.fr.firedrone.service;

/**
 * Created by tbernard on 26/04/16.
 */
public class ColorNameService {



    public static int getColor(String name){

        int hexaColor;
        switch (name){

            case "INCENDIE":
                hexaColor = 0XFFCC0000;
                break;
            case "SAP":
                hexaColor = 0XFF669900;
                break;
            case "EAU":
                hexaColor = 0XFF33B5E5;
                break;
            default: hexaColor = 0XFFFFFF;
                break;

        }
        return hexaColor;




    }


}
