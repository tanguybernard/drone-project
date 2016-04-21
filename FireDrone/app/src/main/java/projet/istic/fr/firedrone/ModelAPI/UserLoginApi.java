package projet.istic.fr.firedrone.ModelAPI;

import projet.istic.fr.firedrone.model.UserLogin;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;

/**
 * Created by mamadian on 18/04/16.
 */
public interface UserLoginApi {

    /**
     *
     * @param authorization

     * @param response
     */
    @GET("/oauth/token?username=monica&password=monica&grant_type=password")
    public void connectUser(
            @Header("Authorization") String authorization,

            Callback<UserLogin> response);
}
