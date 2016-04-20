package projet.istic.fr.firedrone.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mamadian on 21/03/16.
 */
public class Intervention {

    @SerializedName("id")
    public  String id;

    @SerializedName("content")
    public String content;

    @SerializedName("date")
    public String date;

    @SerializedName("type")
    public String type;

    @SerializedName("author")
    public String author;

}
