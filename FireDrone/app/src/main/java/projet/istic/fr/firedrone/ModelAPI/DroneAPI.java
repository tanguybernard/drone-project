package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.Drone;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author Group A
 * Retrofit Interface for DRONES
 */
public interface DroneAPI {

    @GET("/drone")
    void getDrones(retrofit.Callback<List<Drone>> response);

    @GET("/drone/{id}")
    void getDroneById(@Path("id") String queryParam,retrofit.Callback<Drone> response);

    @GET("/intervention/{intervId}/drone")
    void getDronesByIntervention(@Path("intervId") String psIntervQuery,retrofit.Callback<List<Drone>> response);

    @GET("/intervention/{intervId}/drone/{id}")
    void getDroneByInterventionById(@Path("intervId") String psIntervQuery,retrofit.Callback<List<Drone>> response);

    @POST("/intervention/{intervId}/drone")
    void addDrone(@Path("intervId") String psIntervQuery, @Body Drone drone, retrofit.Callback<Drone> response);

    @PATCH("/intervention/{intervId}/drone")
    void editDrone(@Path("intervId") String psIntervQuery, @Body Drone drone, retrofit.Callback<List<Drone>> response);

}
