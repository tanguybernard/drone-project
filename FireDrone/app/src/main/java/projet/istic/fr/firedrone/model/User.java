package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mamadian on 18/04/16.
 */
public class User {

    @SerializedName("login")
    String login;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("phone")
    String phone;

    @SerializedName("email")
    String email;

    @SerializedName("role")
    String role;

    public User(String lastname, String login, String firstname, String phone, String email,String role) {
        this.lastname = lastname;
        this.login = login;
        this.firstname = firstname;
        this.phone = phone;
        this.email = email;
        this.role=role;
    }

    public String getLogin() {
        return login;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
