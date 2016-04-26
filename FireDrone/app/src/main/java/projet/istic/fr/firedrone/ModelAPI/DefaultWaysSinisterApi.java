package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.DefaultSinister;
import projet.istic.fr.firedrone.model.DefaultWay;
import projet.istic.fr.firedrone.model.Intervention;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by tbernard on 26/04/16.
 */
public interface DefaultWaysSinisterApi {


    @GET("/sinister")
    public void getSinisters(retrofit.Callback<List<DefaultSinister>> response);


}
