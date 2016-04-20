package projet.istic.fr.firedrone.model;

/**
 * Created by christophe on 19/04/16.
 */
public class MeansItem {
    long mlMeanId = -1;
    long mlInterventionId = -1;
    String msMeanCode = "";
    String msMeanLib = "";
    String msMeanHCall = "";
    String msMeanHArriv = "";
    String msMeanHEngaged = "";
    String msMeanHFree = "";

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
}
