package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.ImageItem;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.Resource;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
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
        public void getIntervention(@Query("status") String status, Callback<List<Intervention>> response);


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

        /**
         *
         * @param identifiant
         * @param resource
         * @param response
         */
        @POST("/intervention/{id}/ressource")
        public void addResource(@Path("id") String identifiant,@Body Resource resource, Callback<List<Resource>> response);

        /**
         *
         * @param identifiant
         * @param response
         */
        @GET("/intervention/{id}/ressource")
        public void getResources(@Path("id") String identifiant, Callback<List<Resource>> response);


        /**
         *
         * @param identifiant
         * @param response
         */
        @GET("/intervention/{id}/cos")
        public void getInterventionCos(@Path("id") String identifiant, Callback<Intervention> response);

        /**
         *
         * @param identifiant
         * @param response
         */

        @DELETE("/intervention/{id}/cos")
        public void deletenterventionCos(@Path("id") String identifiant, Callback<Intervention> response);

        @POST("/intervention/{id}/cos")
        public void setInterventionCos(@Path("id") String identifiant, @Body String body, Callback<Intervention> response);

        @PATCH("/intervention/{id}/ressource")
        public void updateResource(@Path("id") String identifiant,@Body Resource resource, Callback<List<Resource>> response);

        @DELETE("/intervention/{id}/ressource/{idRessource}")
        public void deleteResource(@Path("id") String identifiantIntervention,@Path("idRessource") String identifiantRessource,Callback<Void> response);

        /**
         *
         * @param identifiant
         * @param response
         */
        @GET("/intervention/{id}/image")
        public void getImages(@Path("id") String identifiant, Callback<List<ImageItem>> response);



        @GET("/intervention/way")
        public void getAllWayRequested(@Query("statusWay") String status, Callback<List<Intervention>> callback);


        @GET("/intervention/{id}/drone")
        public void getAllDrone(@Path("id") String identifiantIntervention,Callback<List<Drone>> callback);




}
