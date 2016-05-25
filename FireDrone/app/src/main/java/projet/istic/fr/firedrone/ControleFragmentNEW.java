package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.apis.MissionApi;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.item.command.DoJump;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.Altitude;
import com.o3dr.services.android.lib.drone.property.Gps;
import com.o3dr.services.android.lib.drone.property.Home;
import com.o3dr.services.android.lib.drone.property.Speed;
import com.o3dr.services.android.lib.drone.property.State;
import com.o3dr.services.android.lib.drone.property.Type;
import com.o3dr.services.android.lib.drone.property.VehicleMode;
import com.o3dr.services.android.lib.model.AbstractCommandListener;

import java.util.Collection;
import java.util.List;

import projet.istic.fr.firedrone.listener.DroneListenerEvent;
import projet.istic.fr.firedrone.listener.DroneListenerEventNEW;

/**
 * @author Group A
 */
public class ControleFragmentNEW extends Fragment {

    /** référence vers le drône commandé par l'application **/
    private Drone drone;

    /** type du drône **/
    private int droneType = Type.TYPE_COPTER;

    /** **/
    private final Handler handler = new Handler();

    /** Listener qui va écouter tout les évènements envoyés par le drone  **/
    private DroneListenerEventNEW droneListenerEvent;

    /** Liste des boutons utilisés par le panel de contrôle du drône  **/
    private Button connectButton;
    private Button takeOffButton;
    private Button goButton;
    private Button takeSnapshotButton;

    /** spinner de sélection du mode du drône  **/
    Spinner modeSelector;

    /**  Singelton Instance **/
    private static ControleFragmentNEW INSTANCE;


    //singleton, une seule instance du fragment controle

    /**
     *
     * @return the only one INSTANCE of ControleFragment
     */
    public static ControleFragmentNEW getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControleFragmentNEW();
        }
        return INSTANCE;
    }

    /** * *     -        OnCreatedView       -     * * **/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //si la tour n'est pas connecté on essaye de se connecter
        if (!getTower().isTowerConnected()) {
            getTower().connect((MainActivity) getActivity());
        }

        if(drone != null) {
            //on enregistre le drone et on le lie au listener event
            drone.registerDroneListener(droneListenerEvent);

        }
        //récupération des boutons et set des listeners onClick
        connectButton = (Button) getView().findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnConnectTap(v);
            }
        });
        takeOffButton = (Button) getView().findViewById(R.id.btnArmTakeOff);
        takeOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onArmButtonTap(v);
            }
        });
        goButton = (Button) getView().findViewById(R.id.btnGo);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDrone(v);
            }
        });
        takeSnapshotButton = (Button) getView().findViewById(R.id.btnSnapshot);
        takeSnapshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSnapshot(v);
            }
        });

        //récupération du spinner
        this.modeSelector = (Spinner) getView().findViewById(R.id.modeSelect);

        //lorsqu'on a sélectionné un mode
        this.modeSelector.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mise à jour du mode du drône
                onFlightModeSelected(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        /**  Print INFORMATIONS  **/
        updateConnectedButton();
        updateDistanceFromHome();
        updateAltitude();
        updateScreenShotButton();
        updateArmButton();
        updateSpeed();
        updateVehicleMode();
        updateVehicleModesForType(droneType);
    }


    private void takeSnapshot(View v) {
    }

    public void onFlightModeSelected(View view) {
        //mise à jour du mode du drône
        VehicleMode vehicleMode = (VehicleMode) this.modeSelector.getSelectedItem();
        this.drone.changeVehicleMode(vehicleMode);
    }

    public void updateVehicleModesForType(int droneType) {
        List<VehicleMode> vehicleModes = VehicleMode.getVehicleModePerDroneType(droneType);
        ArrayAdapter<VehicleMode> vehicleModeArrayAdapter = new ArrayAdapter<VehicleMode>(getContext(), android.R.layout.simple_spinner_item, vehicleModes);
        vehicleModeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.modeSelector.setAdapter(vehicleModeArrayAdapter);
    }

    public void updateVehicleMode() {
        State vehicleState = this.drone.getAttribute(AttributeType.STATE);
        VehicleMode vehicleMode = vehicleState.getVehicleMode();
        ArrayAdapter arrayAdapter = (ArrayAdapter) this.modeSelector.getAdapter();
        if (arrayAdapter == null) {
            Type droneType = this.drone.getAttribute(AttributeType.TYPE);
            updateVehicleModesForType(droneType.getDroneType());
            arrayAdapter = (ArrayAdapter) this.modeSelector.getAdapter();
        }
        this.modeSelector.setSelection(arrayAdapter.getPosition(vehicleMode));
    }



    public void updateAltitude() {
        //mise à jour du textview affichant l'altitude du drône
        TextView altitudeTextView = (TextView) getView().findViewById(R.id.altitudeValueTextView);
        Altitude droneAltitude = this.drone.getAttribute(AttributeType.ALTITUDE);
        altitudeTextView.setText(String.format("%3.1f", droneAltitude.getAltitude()) + "m");
    }

    public void updateSpeed() {
        //mise à jour de la Tewtview affichant la vitesse du drône
        TextView speedTextView = (TextView) getView().findViewById(R.id.speedValueTextView);
        Speed droneSpeed = this.drone.getAttribute(AttributeType.SPEED);
        speedTextView.setText(String.format("%3.1f", droneSpeed.getGroundSpeed()) + "m/s");
    }


    /**
     * GoButton OnClick event
     * Send the Drone into a MISSION
     * @param view
     */
    public void goDrone(View view) {
        //**  Get all points/markers that the user put on the map **//
        Collection<LatLng> positions = getListOfPoint();

        if (positions != null) {

            //** Create new Mission **//
            Mission mission = new Mission();


            //TODO: switch the behaviour of the Drone regarding -   -   -   -   -   -   -  - --
            //Switch ()

            //**  *  *        -        SEGMENT: INFINITE COURSE       -       *  *  **//
            //**  ADD POINTS (Waypoint) to the mission and define its ROUTE  **/
            for (LatLng point : positions) {
                Waypoint waypoint = new Waypoint();
                waypoint.setCoordinate(new LatLongAlt(point.latitude, point.longitude, 0));
                mission.addMissionItem(waypoint);
            }
            // Round trip //
            if(positions.size()>1) {
                for (int i = positions.size() - 2; i >= 1; i--) {
                    LatLng latLng = (LatLng) positions.toArray()[i];
                    Waypoint waypoint = new Waypoint();
                    waypoint.setCoordinate(new LatLongAlt(latLng.latitude, latLng.longitude, 0));
                    mission.addMissionItem(waypoint);
                }
            }
            // Infinite Round Trip //
            DoJump roundTripJUMP = new DoJump();
            roundTripJUMP.setWaypoint(1);
            roundTripJUMP.setRepeatCount(-1);
            mission.addMissionItem(roundTripJUMP);
            //  -  Uncoment the code below to: GO BACK HOME  - //
            /*Waypoint waypoint = new Waypoint();
            waypoint.setCoordinate(((Home) drone.getAttribute(AttributeType.HOME)).getCoordinate());
            mission.addMissionItem(waypoint);*/
            //**  *  *        -  END OF SEGMENT: INFINITE COURSE      -       *  *  **//

            //**    START MISSION    **//
                    ((Mission) drone.getAttribute(AttributeType.MISSION)).clear();
            MissionApi.getApi(drone).setMission(mission, true);
            MissionApi.getApi(drone).startMission(true, true, new AbstractCommandListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int executionError) {

                }

                @Override
                public void onTimeout() {

                }
            });
        }
    }

    public void updateDistanceFromHome() {
        //mise à jour du Textview affichant la distance du drône à la maison
        TextView distanceTextView = (TextView) getView().findViewById(R.id.distanceValueTextView);
        Altitude droneAltitude = this.drone.getAttribute(AttributeType.ALTITUDE);
        double vehicleAltitude = droneAltitude.getAltitude();
        Gps droneGps = this.drone.getAttribute(AttributeType.GPS);
        LatLong vehiclePosition = droneGps.getPosition();

        double distanceFromHome = 0;

        if (droneGps.isValid()) {
            LatLongAlt vehicle3DPosition = new LatLongAlt(vehiclePosition.getLatitude(), vehiclePosition.getLongitude(), vehicleAltitude);
            Home droneHome = this.drone.getAttribute(AttributeType.HOME);
            distanceFromHome = distanceBetweenPoints(droneHome.getCoordinate(), vehicle3DPosition);
        } else {
            distanceFromHome = 0;
        }

        distanceTextView.setText(String.format("%3.1f", distanceFromHome) + "m");
    }

    protected double distanceBetweenPoints(LatLongAlt pointA, LatLongAlt pointB) {
        if (pointA == null || pointB == null) {
            return 0;
        }
        double dx = pointA.getLatitude() - pointB.getLatitude();
        double dy = pointA.getLongitude() - pointB.getLongitude();
        double dz = pointA.getAltitude() - pointB.getAltitude();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onBtnConnectTap(View view) {
        //lorsqu'on clique sur le bouton connect
        if (this.drone.isConnected()) {
            //si on était connecté, on se déconnecte
            this.drone.disconnect();
        } else {
            Bundle extraParams = new Bundle();
            //port 14550
            extraParams.putInt(ConnectionType.EXTRA_UDP_SERVER_PORT, 14550); // Set default port to 14550

            //type UDP
            ConnectionParameter connectionParams = new ConnectionParameter(ConnectionType.TYPE_UDP, extraParams, null);

            //on se connecte au drône
            this.drone.connect(connectionParams);
        }
    }

    public void alertUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateConnectedButton() {
        //Mise à jour du texte du bouton connecté
        if(drone != null) {
            if (drone.isConnected()) {
                connectButton.setText("Disconnect");
            } else {
                connectButton.setText("Connect");
            }
        }
    }


    public void updateScreenShotButton() {
        //mise à jour de la visibilité du bouton prendre une photo
        if (!this.drone.isConnected()) {
            takeSnapshotButton.setVisibility(View.INVISIBLE);
        } else {
            takeSnapshotButton.setVisibility(View.VISIBLE);
        }
    }

    public void updateArmButton() {
        State vehicleState = this.drone.getAttribute(AttributeType.STATE);

        //mise à jour du bouton takeoff en fonction de la connexion au drone
        if (!this.drone.isConnected()) {
            takeOffButton.setVisibility(View.INVISIBLE);
        } else {
            takeOffButton.setVisibility(View.VISIBLE);
        }

        //on affiche un message en fonction de l'état du drône
        if (vehicleState.isFlying()) {
            // Land
            takeOffButton.setText("LAND");
        } else if (vehicleState.isArmed()) {
            // Take off
            takeOffButton.setText("TAKE OFF");
        } else if (vehicleState.isConnected()) {
            // Connected but not Armed
            takeOffButton.setText("ARM");
        }
    }


    /**
     * Arm Button OnClick event
     * @param view
     */
    public void onArmButtonTap(View view) {
        //lorsqu'on clique sur le bouton arm
        Button thisButton = (Button) view;
        //état du drône
        State vehicleState = this.drone.getAttribute(AttributeType.STATE);

        //on met à jour le drône en fonction de l'état dans lequel y est
        if (vehicleState.isFlying()) {
            // Land
            this.drone.changeVehicleMode(VehicleMode.COPTER_LAND);
        } else if (vehicleState.isArmed()) {
            // Take off
            this.drone.doGuidedTakeoff(10); // Default take off altitude is 10m
        } else if (!vehicleState.isConnected()) {
            // Connect
            alertUser("Connect to a drone first");
        } else if (vehicleState.isConnected() && !vehicleState.isArmed()) {
            // Connected but not Armed
            this.drone.arm(true);
        }
    }

    /**
     * @return ControlTower
     */
    public ControlTower getTower() {
        return ((MainActivity) getActivity()).getControlTower();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controle, container, false);
    }



    /**
     * Get all coordinates (LatLng) of markers the users put on the map
     * @return COLLECTION of markers Coordinates (LatLng)
     */
    private Collection<LatLng> getListOfPoint() {
        return ((MainActivity) getActivity()).getArrayPointsForMission();
    }

    //on set le listener
    public void setDroneListenerEvent(DroneListenerEventNEW pDroneListenerEvent) {
        droneListenerEvent = pDroneListenerEvent;
    }

    public int getDroneType() {
        return droneType;
    }

    public void setDroneType(int pDroneType) {
        droneType = pDroneType;
    }

    public Drone getDrone() {
        return drone;
    }

    public Handler getHandler(){
        return handler;
    }

    public void setDrone(Drone pDrone){
        drone = pDrone;
    }
}
