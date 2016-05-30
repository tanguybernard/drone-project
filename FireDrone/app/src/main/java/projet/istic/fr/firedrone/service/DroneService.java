package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.util.Log;

import projet.istic.fr.firedrone.FiredroneConstante;
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

    public static Drone askNewDrone(final Context context) {
         final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
         String sIntervId = intervention.getId();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();
         DroneAPI droneAPI = restAdapter.create(DroneAPI.class);
        droneAPI.addDrone(sIntervId, new Drone(), new Callback<Drone>() {

            @Override
            public void success(Drone newDrone, Response response) {
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

        Log.d("error", " - - - - - -- - - - - - > LISTE DE TOUS LES DRONES DE L'INTERVENTION EN COURS");
        System.out.println(intervention.getDrones());



        return intervention.getDroneByUserID(UserSingleton.getInstance().getUser().getId());
    }


    public static void stopDrone(Drone drone, final Context context){
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = intervention.getId();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();
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

    public static void free(Drone drone, final Context context){

        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = intervention.getId();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();
        DroneAPI droneAPI = restAdapter.create(DroneAPI.class);

        droneAPI.freeDrone(sIntervId, drone.getId(), new Callback<Drone>() {
            @Override
            public void success(Drone drone, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
            }
        });
    }

    public static void startDrone(Drone currentDrone, Context context) {

    }

    public static void freeDrone(Drone currentDrone, Context context) {

    }


}
