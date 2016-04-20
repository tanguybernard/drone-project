package projet.istic.fr.firedrone.ModelAPI;

import projet.istic.fr.firedrone.model.UserLogin;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by mamadian on 18/04/16.
 */
public interface UserLoginApi {

    /**
     * cette fonction permet de connecter un userLogin
     * @param userLogin
     * @param response
     */
    @POST("/login")
    public void connectUser(@Body UserLogin userLogin, retrofit.Callback<UserLogin> response);
}
