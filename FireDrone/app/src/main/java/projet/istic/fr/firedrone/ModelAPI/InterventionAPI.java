package projet.istic.fr.firedrone.ModelAPI;

import java.util.List;

import projet.istic.fr.firedrone.model.Intervention;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

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
        public void GetIntervention(@Path("queryId") String queryParam,retrofit.Callback<Intervention> response);

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
}
