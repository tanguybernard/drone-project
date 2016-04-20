package projet.istic.fr.firedrone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mamadian on 18/04/16.
 */
public class UserLogin {

    @SerializedName("login")
    String login;

    @SerializedName("password")
    String password;


    public UserLogin(String login, String password) {
        this.login = login;
        this.password = password;
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

}
