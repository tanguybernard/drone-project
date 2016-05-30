package projet.istic.fr.firedrone.service;


import android.content.Context;
import android.util.Log;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.DroneAPI;
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

    public static Drone askNewDrone(final Context context) {
        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        String interventionID = intervention.getId();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();
        DroneAPI droneAPI = restAdapter.create(DroneAPI.class);
        droneAPI.addDrone(interventionID, new Drone(), new Callback<Drone>() {

            @Override
            public void success(Drone newDrone, Response response) {
                intervention.addDrone(newDrone);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error", " - - - - - - > FLAG ERROR");
                System.out.println(error);
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();
        DroneAPI droneAPI = restAdapter.create(DroneAPI.class);

        StartMissionDrone startMissionDrone = new StartMissionDrone(drone.getIdIntervention(), drone.getId(), mission);

        droneAPI.startDrone(drone.getIdIntervention(), startMissionDrone, new Callback<Drone>() {

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


}
