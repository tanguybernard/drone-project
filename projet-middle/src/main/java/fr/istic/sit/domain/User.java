package fr.istic.sit.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by fracma on 3/14/16.
 */
public class User {
    @Id
    private String id;

    private String login;
    private String password;
    private String lastname;
    private String firstname;
    private String phone;

    public User() {
    }

    public User(String id, String login, String password, String lastname, String firstname, String phone) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
        this.phone = phone;
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
