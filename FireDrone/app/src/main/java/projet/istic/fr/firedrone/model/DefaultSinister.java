package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ramage on 26/04/16.
 */
public class DefaultSinister {

    @SerializedName("code")
    private Long code;

    @SerializedName("group_ways")
    private DefaultSinisterGroupWays groupWays;

    public DefaultSinisterGroupWays getGroupWays() {
        return groupWays;
    }

    public void setGroupWays(DefaultSinisterGroupWays groupWays) {
        this.groupWays = groupWays;
    }




    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
