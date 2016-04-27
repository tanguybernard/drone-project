package fr.istic.sit.domain;

/**
 * @author FireDroneTeam
 */
public class WayDefault {
    private String acronym;
    private String color;
    private Integer count;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WayDefault{" +
                "acronym='" + acronym + '\'' +
                ", color='" + color + '\'' +
                ", count=" + count +
                '}';
    }
}
