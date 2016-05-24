package projet.istic.fr.firedrone.model;

import android.graphics.Bitmap;

import projet.istic.fr.firedrone.graphic.impl.MeanGRAPHICALGen;

import com.google.gson.annotations.SerializedName;

/**
 * Created by christophe on 19/04/16.
 */
public class MeansItem {

    @SerializedName("id")
    private String msMeanId = "";
    @SerializedName("code")
    private String msMeanCode = "";
    @SerializedName("name")
    private String msMeanName = "";
    @SerializedName("request_time")
    private String msMeanHCall = null;
    @SerializedName("arriving_time")
    private String msMeanHArriv = null;
    @SerializedName("engaged_time")
    private String msMeanHEngaged = null;
    @SerializedName("release_time")
    private String msMeanHFree = null;
    @SerializedName("longitude")
    private String msLongitude;
    @SerializedName("latitude")
    private String msLatitude;
    @SerializedName("color")
    private String msColor;
    @SerializedName("groupId")
    private String msGroupeId = "";

    /**
     * STATUS : See MeansItemStatus
     */
    @SerializedName("status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeansItem meansItem = (MeansItem) o;

        if (status != meansItem.status) return false;
        if (msMeanId != null ? !msMeanId.equals(meansItem.msMeanId) : meansItem.msMeanId != null)
            return false;
        if (msMeanCode != null ? !msMeanCode.equals(meansItem.msMeanCode) : meansItem.msMeanCode != null)
            return false;
        if (msMeanName != null ? !msMeanName.equals(meansItem.msMeanName) : meansItem.msMeanName != null)
            return false;
        if (msMeanHCall != null ? !msMeanHCall.equals(meansItem.msMeanHCall) : meansItem.msMeanHCall != null)
            return false;
        if (msMeanHArriv != null ? !msMeanHArriv.equals(meansItem.msMeanHArriv) : meansItem.msMeanHArriv != null)
            return false;
        if (msMeanHEngaged != null ? !msMeanHEngaged.equals(meansItem.msMeanHEngaged) : meansItem.msMeanHEngaged != null)
            return false;
        if (msMeanHFree != null ? !msMeanHFree.equals(meansItem.msMeanHFree) : meansItem.msMeanHFree != null)
            return false;
        if (msLongitude != null ? !msLongitude.equals(meansItem.msLongitude) : meansItem.msLongitude != null)
            return false;
        if (msLatitude != null ? !msLatitude.equals(meansItem.msLatitude) : meansItem.msLatitude != null)
            return false;
        if (msColor != null ? !msColor.equals(meansItem.msColor) : meansItem.msColor != null)
            return false;
        return !(msGroupeId != null ? !msGroupeId.equals(meansItem.msGroupeId) : meansItem.msGroupeId != null);

    }

    @Override
    public int hashCode() {
        int result = msMeanId != null ? msMeanId.hashCode() : 0;
        result = 31 * result + (msMeanCode != null ? msMeanCode.hashCode() : 0);
        result = 31 * result + (msMeanName != null ? msMeanName.hashCode() : 0);
        result = 31 * result + (msMeanHCall != null ? msMeanHCall.hashCode() : 0);
        result = 31 * result + (msMeanHArriv != null ? msMeanHArriv.hashCode() : 0);
        result = 31 * result + (msMeanHEngaged != null ? msMeanHEngaged.hashCode() : 0);
        result = 31 * result + (msMeanHFree != null ? msMeanHFree.hashCode() : 0);
        result = 31 * result + (msLongitude != null ? msLongitude.hashCode() : 0);
        result = 31 * result + (msLatitude != null ? msLatitude.hashCode() : 0);
        result = 31 * result + (msColor != null ? msColor.hashCode() : 0);
        result = 31 * result + (msGroupeId != null ? msGroupeId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }


    //**  -  -  -   Getter & Setters   -  -  -  **//

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMsMeanName() {
        return msMeanName;
    }

    public void setMsMeanName(String msMeanName) {
        this.msMeanName = msMeanName;
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


    /**
     *
     * @return Bitmap corresponding to Graphical Representation of Means
     */
    public Bitmap getBitmap() {
        MeanGRAPHICALGen generator = new MeanGRAPHICALGen(msMeanCode, msMeanName, msColor);
        Bitmap bitmap = generator.getBitMap(this);
        return bitmap;
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


    public String getMsMeanId() {
        return msMeanId;
    }

    public MeansItem clone() {
        MeansItem clone = new MeansItem();
        clone.setMsMeanId(this.msMeanId);
        clone.setMsMeanCode(this.msMeanCode);
        clone.setMsMeanHEngaged(this.msMeanHEngaged);
        clone.setMsMeanHArriv(this.msMeanHArriv);
        clone.setMsColor(this.msColor);
        clone.setMsMeanHCall(this.msMeanHCall);
        clone.setMsMeanHFree(this.msMeanHFree);
        clone.setMsMeanName(this.msMeanName);
        return clone;
    }
}
