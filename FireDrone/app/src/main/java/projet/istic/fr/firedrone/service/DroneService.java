package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.util.Log;


import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.Interceptor.Interceptor;
import projet.istic.fr.firedrone.ModelAPI.DroneAPI;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Group A.
 */
public class DroneService {

    /**  -   -   -    Static Field   -   -   -   **/
    static final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
    static String sIntervId = intervention.getId();

    static RestAdapter restAdapter = Interceptor.getInstance().getRestAdapter();
    static DroneAPI droneAPI = restAdapter.create(DroneAPI.class);


    public static Drone askNewDrone(final Context context) {
        droneAPI.addDrone(sIntervId, new Drone(), new Callback<Drone>() {

            @Override
            public void success(Drone newDrone, Response response) {
                Log.d("error", " - - - - - - > FLAG");
                System.out.println(newDrone.toString());
                Log.d("error", " - - - - - - > FLAG");

                intervention.addDrone(newDrone);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error", " - - - - - - > FLAG ERROR");
                System.out.println( error );
                Log.d("error", " - - - - - - > FLAG ERROR");
                FiredroneConstante.getToastError(context).show();
            }
        });

        return intervention.getDroneByUserID(UserSingleton.getInstance().getUser().getId());
    }


    public static void stopDrone(Drone drone, final Context context){
        droneAPI.stopDrone(sIntervId, drone, new Callback<Drone>() {
            @Override
            public void success(Drone drone, Response response) {
                intervention.deleteDrone(drone);
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
            }
        });

    }


}
