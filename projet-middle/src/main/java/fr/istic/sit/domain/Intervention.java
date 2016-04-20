package fr.istic.sit.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * Created by fracma on 3/14/16.
 */
public class Intervention {
    @Id
    private String id;
    private String code;
    private String date;
    private String address;
    private String latitude;
    private String longitude;
    private String author;
    private String status;
    private List<Way> ways;    

    public Intervention() {}

    public Intervention(String id, String code, String date, String address, String latitude, 
    		String longitude, String author, String status, List<Way> ways) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.setWays(ways);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
