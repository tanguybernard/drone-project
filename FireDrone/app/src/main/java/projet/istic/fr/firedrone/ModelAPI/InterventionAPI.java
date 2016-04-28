package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.Resource;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mamadian on 21/03/16.
 */
public interface InterventionAPI {

        /**
         * Permet de recuperer une intervention en passant son identifiant
         * @param queryParam
         * @param response
         */
        @GET("/intervention/{queryId}")
        public void getInterventionById(@Path("queryId") String queryParam,retrofit.Callback<Intervention> response);

        @GET("/intervention")
        public void getIntervention(
                @Query("status") String status, Callback<List<Intervention>> response);


        /**
         * Permet de recuperer toutes les interventions
         * @param response
         */
        @GET("/intervention")
        public void GetInterventions(retrofit.Callback<List<Intervention>> response);

        /**
         * cette fonction permet de creer une intervention
         * @param intervention
         * @param response
         */
        @POST("/intervention")
        public void createIntervention(@Body Intervention intervention,retrofit.Callback<Intervention> response);

        @POST("/intervention/{id}/ressource")
        public void addResource(@Path("id") String identifiant,@Body Resource resource, Callback<List<Resource>> response);


        @GET("/intervention/{id}/ressource")
        public void getResources(@Path("id") String identifiant, Callback<List<Resource>> response);

        @PATCH("/intervention/{id}/ressource")
        public void updateResource(@Path("id") String identifiant,@Body Resource resource, Callback<List<Resource>> response);
}
