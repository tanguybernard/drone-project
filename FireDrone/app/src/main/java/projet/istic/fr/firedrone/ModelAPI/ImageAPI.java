package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.ImageItem;
import projet.istic.fr.firedrone.model.Intervention;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by tbernard on 27/05/16.
 */
public interface ImageAPI {




    //photo?idIntervention=571f7731b760adc0c3bec8fb

    @GET("/photo")
    public void getImagesByIntervention(
            @Query("idIntervention") String idIntervention, Callback<List<ImageItem>> response);


}
