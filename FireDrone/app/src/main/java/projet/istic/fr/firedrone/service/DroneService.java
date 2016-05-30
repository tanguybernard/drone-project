package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.util.Log;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.Interceptor.Interceptor;
import projet.istic.fr.firedrone.ModelAPI.DroneAPI;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.listener.DroneEventListenerInterface;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MissionDrone;
import projet.istic.fr.firedrone.model.StartMissionDrone;
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

    //**  -   -   -    Static Field   -   -   -   **//

    public static void askNewDrone(final DroneEventListenerInterface droneEventInterface, final Context context) {
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();

        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);

        droneAPI.addDrone(intervention.getId(), new Drone() , new Callback<Drone>() {

            @Override
            public void success(Drone drone, Response response) {
                intervention.addDrone(drone);
                droneEventInterface.update();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure", " - - - - - - > FLAG ERROR");
                FiredroneConstante.getToastError(context).show();
            }
        });

        Log.d("error", " - - - - - -- - - - - - > LISTE DE TOUS LES DRONES DE L'INTERVENTION EN COURS");
        System.out.println(intervention.getDrones());

    }


    public static void stopDrone(Drone drone, final Context context){
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String sIntervId = intervention.getId();

        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);

        droneAPI.stopDrone(sIntervId, drone, new Callback<Drone>() {
            @Override
            public void success(Drone drone, Response response) {
                //intervention.deleteDrone(drone);
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
            }
        });

    }


    /**
     * Launch the Drone into its mission
     * @param drone
     * @param context
     */
    public static void startDrone(Drone drone, MissionDrone mission, final Context context) {
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);
        StartMissionDrone startMissionDrone = new StartMissionDrone(InterventionSingleton.getInstance().getIntervention().getId(), drone.getId(), mission);

        droneAPI.startDrone(intervention.getId(), startMissionDrone, new Callback<Drone>() {

            @Override
            public void success(Drone newDrone, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error", " - - - - - - > FLAG ERROR");
                Log.d("error", "    " + error.getMessage());
                Log.d("error", " - - - - - - > FLAG ERROR");
                FiredroneConstante.getToastError(context).show();
            }
        });

    }


    /**
     * Stop the drone and Free it
     * @param drone
     * @param context
     */
    public static void freeDrone(Drone drone, final Context context) {
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();

        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);
        droneAPI.freeDrone(intervention.getId(), drone.getId(), new Callback<Drone>() {
            @Override
            public void success(Drone drone, Response response) {
                Log.e("Success", " Here we are in the Success");
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(context).show();
                Log.e("error", " Here we are in the ERROR");
            }
        });
    }




}
