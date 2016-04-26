package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tbernard on 26/04/16.
 */
public class DefaultSinisterGroupWays {


    @SerializedName("acronym")
    private String acronym;

    @SerializedName("count")
    private String count;

    @SerializedName("color")
    private String color;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }



    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
