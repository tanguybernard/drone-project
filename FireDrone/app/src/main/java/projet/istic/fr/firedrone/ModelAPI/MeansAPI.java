package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.DefaultWay;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.Sig;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by christophe on 21/04/16.
 */
public interface MeansAPI {

    @GET("/intervention/{intervId}/way")
    void GetMeans(@Path("intervId") String psIntervQuery,retrofit.Callback<List<MeansItem>> response);

    @PATCH("/intervention/{intervId}/way")
    void EditMean(@Path("intervId") String psIntervQuery, @Body MeansItem poMean,retrofit.Callback<List<MeansItem>> response);

    @POST("/intervention/{intervId}/way")
    void AddMean(@Path("intervId") String psIntervQuery, @Body MeansItem poMean,retrofit.Callback<List<MeansItem>> response);

    @GET("/way")
    void getWay(Callback<List<DefaultWay>> callback);

    @GET("/intervention/{intervId}/way")
    void getWayRequested(@Path("intervId") String psIntervQuery,@Query("status") String status, Callback<List<MeansItem>> callback);


}
