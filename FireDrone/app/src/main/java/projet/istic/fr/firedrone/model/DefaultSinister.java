package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ramage on 26/04/16.
 */
public class DefaultSinister {

    @SerializedName("id")
    private String id;

    @SerializedName("code")
    private String code;

    @SerializedName("ways")
    private List<DefaultSinisterGroupWays> groupWays;

    public List<DefaultSinisterGroupWays> getGroupWays() {
        return groupWays;
    }

    public void setGroupWays(List<DefaultSinisterGroupWays> groupWays) {
        this.groupWays = groupWays;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
