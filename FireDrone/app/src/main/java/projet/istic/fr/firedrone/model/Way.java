package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tbernard on 21/04/16.
 */
public class Way {
    @SerializedName("id")
    private String msMeanId = "";
    @SerializedName("code")
    private String msMeanCode = "";
    @SerializedName("name")
    private String msMeanLib = "";
    @SerializedName("request_time")
    private String msMeanHCall = "";
    @SerializedName("arriving_time")
    private String msMeanHArriv = "";
    @SerializedName("engaged_time")
    private String msMeanHEngaged = "";
    @SerializedName("release_time")
    private String msMeanHFree = "";
    @SerializedName("longitude")
    private String msLongitude = "";
    @SerializedName("latitude")
    private String msLatitude = "";
    @SerializedName("color")
    private String msColor = "";
    @SerializedName("groupId")
    private String msGroupeId = "";


    public String getMsMeanId() {
        return msMeanId;
    }

    public void setMsMeanId(String msMeanId) {
        this.msMeanId = msMeanId;
    }

    public String getMsMeanCode() {
        return msMeanCode;
    }

    public void setMsMeanCode(String msMeanCode) {
        this.msMeanCode = msMeanCode;
    }

    public String getMsMeanLib() {
        return msMeanLib;
    }

    public void setMsMeanLib(String msMeanLib) {
        this.msMeanLib = msMeanLib;
    }

    public String getMsMeanHCall() {
        return msMeanHCall;
    }

    public void setMsMeanHCall(String msMeanHCall) {
        this.msMeanHCall = msMeanHCall;
    }

    public String getMsMeanHArriv() {
        return msMeanHArriv;
    }

    public void setMsMeanHArriv(String msMeanHArriv) {
        this.msMeanHArriv = msMeanHArriv;
    }

    public String getMsMeanHEngaged() {
        return msMeanHEngaged;
    }

    public void setMsMeanHEngaged(String msMeanHEngaged) {
        this.msMeanHEngaged = msMeanHEngaged;
    }

    public String getMsMeanHFree() {
        return msMeanHFree;
    }

    public void setMsMeanHFree(String msMeanHFree) {
        this.msMeanHFree = msMeanHFree;
    }

    public String getMsLongitude() {
        return msLongitude;
    }

    public void setMsLongitude(String msLongitude) {
        this.msLongitude = msLongitude;
    }

    public String getMsLatitude() {
        return msLatitude;
    }

    public void setMsLatitude(String msLatitude) {
        this.msLatitude = msLatitude;
    }

    public String getMsColor() {
        return msColor;
    }

    public void setMsColor(String msColor) {
        this.msColor = msColor;
    }

    public String getMsGroupeId() {
        return msGroupeId;
    }

    public void setMsGroupeId(String msGroupeId) {
        this.msGroupeId = msGroupeId;
    }
}
