package fr.istic.sit.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * @author FireDroneTeam
 */
public class Sinister {
    @Id
    private String id;
    private String code;
    private List<WayDefault> ways;

    public Sinister() {}

    public Sinister(String id, String code, List<WayDefault> ways) {
        this.id = id;
        this.code = code;
        this.setWays(ways);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<WayDefault> getWays() {
        return ways;
    }

    public void setWays(List<WayDefault> ways) {
        this.ways = ways;
    }


    @Override
    public String toString() {
        return "Sinister{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", ways=" + ways +
                '}';
    }
}
