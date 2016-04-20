package projet.istic.fr.firedrone.listener;

import projet.istic.fr.firedrone.model.Intervention;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mamadian on 21/03/16.
 */
public interface InterventionAPI {
        @GET("/intervention/{queryId}")
        public void GetIntervention(@Path("queryId") String queryParam,retrofit.Callback<Intervention> response);
}
