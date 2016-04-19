package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;


/**
 * Created by fracma on 3/14/16.
 */
public class User {
	@Id
	private String id;

	private String login;
	/**
	 * TODO: Encrypted Password ?
	 */
	private String password;
	private String lastname;
	private String firstname;

	private String phone;

	private String email;

	private String role;

	public User() {
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
		this.lastname = "";
		this.firstname = "";
		this.phone = "";
	}

	public User(String id, String login, String password, String lastname, String firstname, String phone) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.lastname = lastname;
		this.firstname = firstname;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", lastname='" + lastname + '\'' +
				", firstname='" + firstname + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}
}
