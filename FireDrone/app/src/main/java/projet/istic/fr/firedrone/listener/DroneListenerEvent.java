package projet.istic.fr.firedrone.listener;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.o3dr.android.client.interfaces.DroneListener;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.connection.ConnectionResult;
import com.o3dr.services.android.lib.drone.mission.Mission;
import com.o3dr.services.android.lib.drone.mission.MissionItemType;
import com.o3dr.services.android.lib.drone.mission.item.MissionItem;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.Gps;
import com.o3dr.services.android.lib.drone.property.Type;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FragmentControle;

/**
 * Created by ramage on 20/03/16.
 */
public class DroneListenerEvent implements DroneListener {

    private FragmentControle controlFragement;
    private boolean usingControlPanel;
    private DroneMoveListener droneMoveListener;

    public DroneListenerEvent(FragmentControle pDroneControlFragment){
        controlFragement = pDroneControlFragment;
        controlFragement.setDroneListernEvent(this);
    }

    public void setUsingControlPanel(boolean pUsingControlPanel){
        usingControlPanel = pUsingControlPanel;
    }

    @Override
    public void onDroneConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onDroneEvent(String event, Bundle extras) {

        switch (event) {

            case AttributeEvent.STATE_CONNECTED:
                if(usingControlPanel) {
                    controlFragement.alertUser("Drone Connected");
                    controlFragement.updateConnectedButton();
                    controlFragement.updateArmButton();
                    controlFragement.updateScreenShotButton();
                }
                break;

            case AttributeEvent.STATE_DISCONNECTED:
                if(usingControlPanel) {
                    controlFragement.alertUser("Drone Disconnected");
                    controlFragement.updateConnectedButton();
                    controlFragement.updateArmButton();
                    controlFragement.updateScreenShotButton();
                }
                break;

            case AttributeEvent.STATE_UPDATED:
            case AttributeEvent.STATE_ARMING:
                if(usingControlPanel) {
                    controlFragement.updateArmButton();
                    controlFragement.updateScreenShotButton();
                }
                break;

            case AttributeEvent.TYPE_UPDATED:
                Type newDroneType = controlFragement.getDrone().getAttribute(AttributeType.TYPE);
                if (newDroneType.getDroneType() != controlFragement.getDroneType()) {
                   controlFragement.setDroneType(newDroneType.getDroneType());
                    if(usingControlPanel) {
                        controlFragement.updateVehicleModesForType(controlFragement.getDroneType());
                    }
                }
                break;

            case AttributeEvent.STATE_VEHICLE_MODE:
                if(usingControlPanel) {
                    controlFragement.updateVehicleMode();
                }
                break;


            case AttributeEvent.SPEED_UPDATED:
                if(usingControlPanel) {
                    controlFragement.updateAltitude();
                    controlFragement.updateSpeed();
                }
                break;
            case AttributeEvent.GPS_POSITION:
                if(droneMoveListener != null){
                    Gps gps = controlFragement.getDrone().getAttribute(AttributeType.GPS);
                    LatLong position = gps.getPosition();
                    if(position != null) {
                        droneMoveListener.onDroneMove(new LatLng(gps.getPosition().getLatitude(), gps.getPosition().getLongitude()));
                    }
                }
                break;

            case AttributeEvent.HOME_UPDATED:
                controlFragement.updateDistanceFromHome();
                break;
            case AttributeEvent.MISSION_UPDATED:
                if(droneMoveListener != null) {
                    List<LatLng> pointsMissions = new ArrayList<LatLng>();
                    Mission mission = controlFragement.getDrone().getAttribute(AttributeType.MISSION);
                    for(MissionItem item :mission.getMissionItems()){
                        if(item.getType() == MissionItemType.WAYPOINT){
                            Waypoint point = (Waypoint)item;
                            pointsMissions.add(new LatLng(point.getCoordinate().getLatitude(),point.getCoordinate().getLongitude()));
                        }
                    }
                    droneMoveListener.droneReceivedMissionPoint(pointsMissions);
                }
                break;

            default:
                Log.i("DRONE_EVENT", event);
                break;

        }
    }

    @Override
    public void onDroneServiceInterrupted(String errorMsg) {

    }


    public void setDroneMoveListener(DroneMoveListener droneMoveListener) {
        this.droneMoveListener = droneMoveListener;
    }

    public interface DroneMoveListener{
        void onDroneMove(LatLng position);

        void droneReceivedMissionPoint(List<LatLng> pointsMissions);
    }
}
