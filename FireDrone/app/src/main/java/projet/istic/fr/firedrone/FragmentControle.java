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
import com.o3dr.android.client.apis.mission.MissionApi;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.connection.ConnectionParameter;
import com.o3dr.services.android.lib.drone.connection.ConnectionType;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.Altitude;
import com.o3dr.services.android.lib.drone.property.Gps;
import com.o3dr.services.android.lib.drone.property.Home;
import com.o3dr.services.android.lib.drone.property.Speed;
import com.o3dr.services.android.lib.drone.property.State;
import com.o3dr.services.android.lib.drone.property.Type;
import com.o3dr.services.android.lib.drone.property.VehicleMode;

import java.util.List;

import projet.istic.fr.firedrone.listener.DroneListenerEvent;

/**
 * Created by nduquesne on 18/03/16.
 */
public class FragmentControle extends Fragment {


    private Drone drone;
    private int droneType = Type.TYPE_UNKNOWN;
    private final Handler handler = new Handler();
    //listener qui va écouter tout les évènements envoyés par le drone
    private DroneListenerEvent droneListenerEvent;

    private Button connectButton;
    private Button takeOfftButton;
    private Button gotButton;
    private Button takeSnapshot;
    Spinner modeSelector;
    private static FragmentControle INSTANCE;



    public static FragmentControle getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FragmentControle();
        }
        return INSTANCE;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getTower().isTowerConnected()) {
            getTower().connect((MainActivity) getActivity());
        }
        drone.registerDroneListener(droneListenerEvent);

        connectButton = (Button) getView().findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnConnectTap(v);
            }
        });
        takeOfftButton = (Button) getView().findViewById(R.id.btnArmTakeOff);
        takeOfftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onArmButtonTap(v);
            }
        });
        gotButton = (Button) getView().findViewById(R.id.btnGo);
        gotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDrone(v);
            }
        });
        takeSnapshot = (Button) getView().findViewById(R.id.btnSnapshot);
        takeSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSnapshot(v);
            }
        });

        this.modeSelector = (Spinner) getView().findViewById(R.id.modeSelect);
        this.modeSelector.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onFlightModeSelected(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        updateConnectedButton();
        updateDistanceFromHome();
        updateAltitude();
        updateScreenShotButton();
        updateArmButton();
        updateSpeed();
        updateVehicleMode();
        updateVehicleModesForType(droneType);
    }

    public void onFlightModeSelected(View view) {
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
        TextView altitudeTextView = (TextView) getView().findViewById(R.id.altitudeValueTextView);
        Altitude droneAltitude = this.drone.getAttribute(AttributeType.ALTITUDE);
        altitudeTextView.setText(String.format("%3.1f", droneAltitude.getAltitude()) + "m");
    }

    public void updateSpeed() {
        TextView speedTextView = (TextView) getView().findViewById(R.id.speedValueTextView);
        Speed droneSpeed = this.drone.getAttribute(AttributeType.SPEED);
        speedTextView.setText(String.format("%3.1f", droneSpeed.getGroundSpeed()) + "m/s");
    }

    public void goDrone(View view) {
       /* System.out.println("fffdfgdfgdfgdfgdfgfdgdfgdfgfgdfggdgd");
        Random random = new Random();
        drone.sendGuidedPoint(new LatLong(random.nextDouble(), random.nextDouble()), false);*/
        List<LatLng> positions = getListOfPoint();
        if (positions != null) {
            Mission mission = new Mission();

            for (LatLng point : positions) {
                Waypoint waypoint = new Waypoint();
                waypoint.setCoordinate(new LatLongAlt(point.latitude, point.longitude, 0));
                mission.addMissionItem(waypoint);
            }
            MissionApi.setMission(drone, mission, true);
            MissionApi.loadWaypoints(drone);
        }
    }

    public void updateDistanceFromHome() {
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
        if (this.drone.isConnected()) {
            this.drone.disconnect();
        } else {
            Bundle extraParams = new Bundle();
            extraParams.putInt(ConnectionType.EXTRA_UDP_SERVER_PORT, 14550); // Set default port to 14550

            ConnectionParameter connectionParams = new ConnectionParameter(ConnectionType.TYPE_UDP, extraParams, null);
            this.drone.connect(connectionParams);
        }
    }

    public void alertUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateConnectedButton() {
        if (drone.isConnected()) {
            connectButton.setText("Disconnect");
        } else {
            connectButton.setText("Connect");
        }
    }


    public void updateScreenShotButton() {
        if (!this.drone.isConnected()) {
            takeSnapshot.setVisibility(View.INVISIBLE);
        } else {
            takeSnapshot.setVisibility(View.VISIBLE);
        }
    }

    public void updateArmButton() {
        State vehicleState = this.drone.getAttribute(AttributeType.STATE);

        if (!this.drone.isConnected()) {
            takeOfftButton.setVisibility(View.INVISIBLE);
        } else {
            takeOfftButton.setVisibility(View.VISIBLE);
        }

        if (vehicleState.isFlying()) {
            // Land
            takeOfftButton.setText("LAND");
        } else if (vehicleState.isArmed()) {
            // Take off
            takeOfftButton.setText("TAKE OFF");
        } else if (vehicleState.isConnected()) {
            // Connected but not Armed
            takeOfftButton.setText("ARM");
        }
    }

    public void onArmButtonTap(View view) {
        Button thisButton = (Button) view;
        State vehicleState = this.drone.getAttribute(AttributeType.STATE);

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

    public ControlTower getTower() {
        return ((MainActivity) getActivity()).getControlTower();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controle, container, false);
    }

    private void takeSnapshot(View v) {

    }

    private List<LatLng> getListOfPoint() {
        return ((MainActivity) getActivity()).getArrayPointsForMission();
    }

    public void setDroneListernEvent(DroneListenerEvent pDroneListenerEvent) {
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
