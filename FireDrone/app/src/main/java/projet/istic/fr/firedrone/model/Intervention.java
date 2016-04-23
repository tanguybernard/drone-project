package projet.istic.fr.firedrone.model;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

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

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("ways")
    private List<MeansItem> ways;

    //private List<Way> ways;

    public String getId() {
        return id;
    }

    public void setId(String myId) {
        this.id = myId;
    }

    public String getSinisterCode() {
        return sinisterCode;
    }

    public void setSinisterCode(String codeSinistre) {
        this.sinisterCode = codeSinistre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date){this.date = date;}

    public String getDate(){return date;}

    public void setStatus(String status){this.status = status;}

    public String getStatus(){return status;}

    public void setLatitude(String latitude){this.latitude = latitude;}

    public String getLatitude(){return latitude;}

    public void setLongitude(String longitude){this.longitude = longitude;}

    public String getLongitude(){return longitude;}

    public void setWays(List<MeansItem> ways){
        this.ways = ways;

    }

    public Intervention(){

    }

    public Intervention(String sinisterCode, String date, String address, String status ) {

        this.sinisterCode = sinisterCode;
        this.date = date;
        this.address = address;
        this.status = status;

    }

}
