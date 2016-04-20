package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

public class Way {
	@Id
	private String id;
	private String code;
	private String name;
	private String color;
	private String longitude;
	private String latitude;
	private String request_time;
	private String arriving_time;
	private String engaged_time;
	private String release_time;
	private String groupId;
	
	public Way(){}

	public Way(String id, String code, String name, String color, String longitude, String latitude,
			String request_time, String arriving_time, String engaged_time, String release_time, String groupId) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.color = color;
		this.longitude = longitude;
		this.latitude = latitude;
		this.request_time = request_time;
		this.arriving_time = arriving_time;
		this.engaged_time = engaged_time;
		this.release_time = release_time;
		this.groupId = groupId;
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

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public String getArriving_time() {
		return arriving_time;
	}

	public void setArriving_time(String arriving_time) {
		this.arriving_time = arriving_time;
	}

	public String getEngaged_time() {
		return engaged_time;
	}

	public void setEngaged_time(String engaged_time) {
		this.engaged_time = engaged_time;
	}

	public String getRelease_time() {
		return release_time;
	}

	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}