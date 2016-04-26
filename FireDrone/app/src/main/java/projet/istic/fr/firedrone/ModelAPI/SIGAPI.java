package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.Sig;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ramage on 22/04/16.
 */
public interface SIGAPI {

    @GET("/sig")
    public void getSIGs( Callback<List<Sig>> callback);
}
