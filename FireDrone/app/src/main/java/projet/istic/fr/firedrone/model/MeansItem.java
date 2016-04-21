package projet.istic.fr.firedrone.model;

import com.google.android.gms.maps.model.LatLng;

import projet.istic.fr.firedrone.R;

/**
 * Created by christophe on 19/04/16.
 */
public class MeansItem {
    long mlMeanId = -1;
    long mlInterventionId = -1;
    String msMeanCode = "";
    String msMeanLib = "";
    String msMeanHCall = null;
    String msMeanHArriv = null;
    String msMeanHEngaged = null;
    String msMeanHFree = "";
    CouleurMoyen color;
    LatLng position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeansItem meansItem = (MeansItem) o;

        if (mlMeanId != meansItem.mlMeanId) return false;
        if (mlInterventionId != meansItem.mlInterventionId) return false;
        if (msMeanCode != null ? !msMeanCode.equals(meansItem.msMeanCode) : meansItem.msMeanCode != null)
            return false;
        if (msMeanLib != null ? !msMeanLib.equals(meansItem.msMeanLib) : meansItem.msMeanLib != null)
            return false;
        if (msMeanHCall != null ? !msMeanHCall.equals(meansItem.msMeanHCall) : meansItem.msMeanHCall != null)
            return false;
        if (msMeanHArriv != null ? !msMeanHArriv.equals(meansItem.msMeanHArriv) : meansItem.msMeanHArriv != null)
            return false;
        if (msMeanHEngaged != null ? !msMeanHEngaged.equals(meansItem.msMeanHEngaged) : meansItem.msMeanHEngaged != null)
            return false;
        if (msMeanHFree != null ? !msMeanHFree.equals(meansItem.msMeanHFree) : meansItem.msMeanHFree != null)
            return false;
        if (color != meansItem.color) return false;
        return !(position != null ? !position.equals(meansItem.position) : meansItem.position != null);

    }

    @Override
    public int hashCode() {
        return (int) (mlMeanId ^ (mlMeanId >>> 32));
    }

    public long getMlMeanId() {
        return mlMeanId;
    }

    public void setMlMeanId(long mlMeanId) {
        this.mlMeanId = mlMeanId;
    }

    public long getMlInterventionId() {
        return mlInterventionId;
    }

    public void setMlInterventionId(long mlInterventionId) {
        this.mlInterventionId = mlInterventionId;
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

    public Integer getResource(){
        if(mlMeanId ==1){
            return R.drawable.delete_24dp_rouge;
        }
        return R.drawable.drone_36_36;
    }
}
