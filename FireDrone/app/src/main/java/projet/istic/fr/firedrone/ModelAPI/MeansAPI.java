package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.MeansItem;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by christophe on 21/04/16.
 */
public interface MeansAPI {
    @GET("/intervention/{intervId}/way")
    public void GetMeans(@Path("intervId") String psIntervQuery,retrofit.Callback<List<MeansItem>> response);

    @PATCH("/intervention/{intervId}/way")
    public void EditMean(@Path("intervId") String psIntervQuery, @Body MeansItem poMean,retrofit.Callback<List<MeansItem>> response);

    @POST("/intervention/{intervId}/way")
    public void AddMean(@Path("intervId") String psIntervQuery, @Body MeansItem poMean,retrofit.Callback<List<MeansItem>> response);
}
