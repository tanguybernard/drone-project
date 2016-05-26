package projet.istic.fr.firedrone.model;

import android.graphics.Bitmap;

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

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("image")
    private Bitmap image;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
