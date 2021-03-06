package projet.istic.fr.firedrone.Interceptor;

import projet.istic.fr.firedrone.FiredroneConstante;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by mamadian on 22/04/16.
 *
 */
public class Interceptor {

    private String token;

    /** un interceptor de requete qui ajoute à toutes les requetes vers END_POINT un header (token) */
    private RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Authorization", token);
        }
    };

    /**Un adapter */
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(FiredroneConstante.END_POINT)
            .setRequestInterceptor(requestInterceptor)
            .build();

    /** Constructeur privé */
    private Interceptor()
    {}

    /** Instance unique non préinitialisée */
    private static Interceptor INSTANCE = null;


    /** Point d'accès pour l'instance unique de l'interceptor */
    public static Interceptor getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new Interceptor();
        }
        return INSTANCE;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
