package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ramage on 26/04/16.
 */
public class DefaultWay {

    @SerializedName("code")
    private Long code;

    @SerializedName("acronym")
    private String acronym;

    @SerializedName("name")
    private String name;

    @SerializedName("color")
    private int color;

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronyme) {
        this.acronym = acronyme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
