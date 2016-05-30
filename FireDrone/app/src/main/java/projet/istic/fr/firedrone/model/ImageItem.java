package projet.istic.fr.firedrone.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tbernard on 25/05/16.
 */
public class ImageItem {


    @SerializedName("longitude")
    private String longitude;

    @SerializedName("date")
    private String date;

    @SerializedName("id")
    private String id;

    @SerializedName("idIntervention")
    private String idIntervention;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("imageURL")
    private String imageUrl;

    private Bitmap imageBitmap;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
