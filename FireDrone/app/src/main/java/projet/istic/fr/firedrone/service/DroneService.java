package projet.istic.fr.firedrone.service;


import android.content.Context;


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


    public static Drone askNewDrone(final Context context) {
        //  Intervention  //
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = intervention.getId();

        // Rest Adapter Retrofit  //
        RestAdapter restAdapter = new RestAdapter.Builder().
                setEndpoint(FiredroneConstante.END_POINT).
                setLogLevel(RestAdapter.LogLevel.FULL).
                build();

        DroneAPI droneAPI = restAdapter.create(DroneAPI.class);
        droneAPI.addDrone(sIntervId, new Drone(), new Callback<Drone>() {

            @Override
            public void success(Drone newDrone, Response response) {
                intervention.addDrone(newDrone);
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
            }
        });

        return intervention.getDroneByUserID(UserSingleton.getInstance().getUser().getId());
    }


    public static void stopDrone(Drone drone, final Context context){
        //  Intervention  //
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = intervention.getId();

        // Rest Adapter Retrofit  //
        RestAdapter restAdapter = Interceptor.getInstance().getRestAdapter();
        DroneAPI droneAPI = restAdapter.create(DroneAPI.class);

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
