package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.util.Log;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.Interceptor.Interceptor;
import projet.istic.fr.firedrone.ModelAPI.DroneAPI;
import projet.istic.fr.firedrone.listener.DroneEventListenerInterface;
import projet.istic.fr.firedrone.model.ActionMissionDrone;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MissionDrone;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Group A.
 */
public class DroneService {

    //**  -   -   -    Static Field   -   -   -   **//

    /**
     * Create the New Drone
     * @param droneEventInterface
     * @param context
     */
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

        Log.d("**FLAG**", " - - - - - - > LISTE DE TOUS LES DRONES DE L'INTERVENTION EN COURS");
        Log.d("**FLAG**", intervention.getDrones().toString());

    }


    /**
     * Stop the Drone
     * @param drone
     * @param context
     */
    public static void stopDrone(Drone drone, final Context context){
        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);

        ActionMissionDrone actionMissionDrone = new ActionMissionDrone(InterventionSingleton.getInstance().getIntervention().getId(), drone, null, false);
        droneAPI.actionDrone(actionMissionDrone, new Callback<Drone>() {
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
     * Start Drone Mission
     * @param drone
     * @param context
     */
    public static void startDrone(Drone drone, MissionDrone mission, final Context context) {
        final DroneAPI droneAPI = Interceptor.getInstance().getRestAdapter().create(DroneAPI.class);
        ActionMissionDrone actionMissionDrone = new ActionMissionDrone(InterventionSingleton.getInstance().getIntervention().getId(), drone, mission, true);

        droneAPI.actionDrone(actionMissionDrone, new Callback<Drone>() {

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
