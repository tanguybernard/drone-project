package projet.istic.fr.firedrone.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mamadian on 21/03/16.
 */
public class Intervention {

    @SerializedName("id")
    public  String id;

    @SerializedName("codeSinistre")
    public String codeSinistre;

    @SerializedName("date")
    public String date;

    @SerializedName("adresse")
    public String adresse;

    @SerializedName("satatus")
    public String satatus;

}
