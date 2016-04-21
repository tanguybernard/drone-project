package projet.istic.fr.firedrone.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mamadian on 21/03/16.
 */
public class Intervention {

    @SerializedName("id")
    public  String id;

    @SerializedName("sinisterCode")
    public String sinisterCode;

    @SerializedName("date")
    public String date;

    @SerializedName("address")
    public String address;

    @SerializedName("status")
    public String status;


    //private String latitude;
    //private String longitude;

    //private List<Way> ways;




    public Intervention(String sinisterCode, String date, String address, String status ) {

        this.sinisterCode = sinisterCode;
        this.date = date;
        this.address = address;
        this.status = status;

    }

    /*public void setWays(){
        new Way(nom, quantité)
        new Way(nom, quantité)

    }*/


}
