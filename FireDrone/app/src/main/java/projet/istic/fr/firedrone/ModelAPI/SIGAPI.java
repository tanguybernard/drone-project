package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.SIG;
import projet.istic.fr.firedrone.model.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by ramage on 22/04/16.
 */
public interface SIGAPI {

    @GET("/sig")
    public void getSIGs( Callback<List<SIG>> callback);
}
