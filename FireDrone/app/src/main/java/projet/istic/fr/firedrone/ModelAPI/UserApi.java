package projet.istic.fr.firedrone.ModelAPI;

import projet.istic.fr.firedrone.model.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mamadian on 18/04/16.
 */
public interface UserApi {

    /**
     *
     * @param authorization

     * @param response
     */
    @GET("/oauth/token")
    public void connectUser(
            @Header("Authorization") String authorization,
            @Query("username") String username,
            @Query("password") String password,
            @Query("grant_type") String grant_type,
            Callback<User> response);

    @GET("/user/{username}")
    public void getUser(
            @Header("Authorization") String authorization,
            @Path("username") String username,
            Callback<User> callback);
}
