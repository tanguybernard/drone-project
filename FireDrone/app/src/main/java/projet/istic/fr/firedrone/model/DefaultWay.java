package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ramage on 26/04/16.
 */
public class DefaultWay {

    @SerializedName("code")
    private Long code;

    @SerializedName("acronyme")
    private String acronyme;

    @SerializedName("name")
    private String name;

    @SerializedName("color")
    private String color;

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
