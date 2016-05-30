package fr.istic.sit.model;

import fr.istic.sit.domain.Way;

import java.util.List;

/**
 * Created by fracma on 5/26/16.
 */
public class InterventionWay {
    private String id;
    private String sinisterCode;
    private String date;
    private String address;
    private List<Way> ways;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSinisterCode() {
        return sinisterCode;
    }

    public void setSinisterCode(String sinisterCode) {
        this.sinisterCode = sinisterCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Way> getWays() {
        return ways;
    }

    public void setWays(List<Way> ways) {
        this.ways = ways;
    }
}
