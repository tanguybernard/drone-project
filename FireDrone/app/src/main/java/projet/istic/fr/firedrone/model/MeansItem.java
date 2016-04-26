package projet.istic.fr.firedrone.model;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophe on 19/04/16.
 */
public class MeansItem {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";

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
    private String msLongitude = "";
    @SerializedName("latitude")
    private String msLatitude = "";
    @SerializedName("color")
    private String msColor = "";
    @SerializedName("groupId")
    private String msGroupeId = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeansItem meansItem = (MeansItem) o;

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
        return msMeanId != null ? msMeanId.hashCode() : 0;
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

    public Integer getResource() {
        if (msMeanId.equals("1")) {
            return R.drawable.delete_24dp_rouge;
        }
        return R.drawable.drone_36_36;
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

    public List<MeansItem> addMean() {
        final Intervention oIntervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = oIntervention.getId();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.AddMean(sIntervId, this, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {
                oIntervention.setWays(ploMeans);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("CONNEXION ERROR", error.getMessage());
            }
        });
        return oIntervention.getWays();
    }

    public List<MeansItem> editMean() {
        final Intervention oIntervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = oIntervention.getId();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.EditMean(sIntervId, this, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {
                oIntervention.setWays(ploMeans);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("CONNEXION ERROR", error.getMessage());
            }
        });
        return oIntervention.getWays();
    }
}
