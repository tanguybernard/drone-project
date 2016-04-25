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
    private List<Way> ways;

    public Sinister() {}

    public Sinister(String id, String code, List<Way> ways) {
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

    public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public List<Way> getWays() {
		return ways;
	}

	public void setWays(List<Way> ways) {
		this.ways = ways;
	}

	@Override
    public String toString() {
        return "Intervention{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
