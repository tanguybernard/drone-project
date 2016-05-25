package projet.istic.fr.firedrone.listener;

import android.os.Bundle;

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
import java.util.Date;
import java.util.List;

import projet.istic.fr.firedrone.ControleFragmentNEW;

/**
 * @author Group A
 *
 */
public class DroneListenerEventNEW implements DroneListener {

    /** Control Drone Fragment **/
    private ControleFragmentNEW controlFragment;

    /**   Last time the drone moved   **/
    private long lastDateTimeDroneMove;

    /**  FLAG : indicates if we're in the Drone Control Panel **/
    private boolean usingControlPanel;

    //listener qui va être appelé par le fragment map quand le drône a effectué certaines actions
    private DroneActionMapListener droneMoveListener;

    public DroneListenerEventNEW(ControleFragmentNEW pDroneControlFragment){
        controlFragment = pDroneControlFragment;
        controlFragment.setDroneListenerEvent(this);
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
                //lorsqu'on est connecté au drône
                if(usingControlPanel) {
                    controlFragment.alertUser("Drone Connected");
                    controlFragment.updateConnectedButton();
                    controlFragment.updateArmButton();
                    controlFragment.updateScreenShotButton();
                }
                break;

            case AttributeEvent.STATE_DISCONNECTED:
                //lorsque on est déconnecté du drône
                if(usingControlPanel) {
                    controlFragment.alertUser("Drone Disconnected");
                    //mise à jour des boutons
                    controlFragment.updateConnectedButton();
                    controlFragment.updateArmButton();
                    controlFragment.updateScreenShotButton();
                }
                break;

            case AttributeEvent.STATE_UPDATED:
            case AttributeEvent.STATE_ARMING:
                //lorsque le drone a changé son armement
                if(usingControlPanel) {
                    //mise à jour du bouton arm
                    controlFragment.updateArmButton();
                    //mise à jour du boutn screenshot
                    controlFragment.updateScreenShotButton();
                }
                break;

            case AttributeEvent.TYPE_UPDATED:
                //lorsque la liste des types disponible a changé
                Type newDroneType = controlFragment.getDrone().getAttribute(AttributeType.TYPE);
                if (newDroneType.getDroneType() != controlFragment.getDroneType()) {
                   controlFragment.setDroneType(newDroneType.getDroneType());
                    if(usingControlPanel) {
                        //mise à jour des types de mode
                        controlFragment.updateVehicleModesForType(controlFragment.getDroneType());
                    }
                }
                break;

            case AttributeEvent.STATE_VEHICLE_MODE:
                //lorsque le drône a changé de mode
                if(usingControlPanel) {
                    //mise à jour du mode du drône
                    controlFragment.updateVehicleMode();
                }
                break;


            case AttributeEvent.SPEED_UPDATED:
                //lorsque la vitesse du drône a changé
                if(usingControlPanel) {
                    //mise à jour de l'altitude du drone
                    controlFragment.updateAltitude();
                    //mise à jour de la vitesse du drône
                    controlFragment.updateSpeed();
                }
                break;
            case AttributeEvent.GPS_POSITION:
                //lorsque la position du drone a bougé
                long time = new Date().getTime();
                if(droneMoveListener != null ){
                    //on récupère la position du drône
                    Gps gps = controlFragment.getDrone().getAttribute(AttributeType.GPS);
                    LatLong position = gps.getPosition();

                    if(position != null && (time - lastDateTimeDroneMove > 1000)) {
                        lastDateTimeDroneMove = time;
                        //on appelle la méthode qui gère lle mouveemnt du drone
                        droneMoveListener.onDroneMove(new LatLng(gps.getPosition().getLatitude(), gps.getPosition().getLongitude()));
                    }
                }
                break;

            case AttributeEvent.HOME_UPDATED:
                //lorsque la position de la maison à été modifié
                //mise à jour de la position de la maison
                controlFragment.updateDistanceFromHome();
                break;
            case AttributeEvent.MISSION_UPDATED:
                //lorsqu'une missuion vient d'être reçu
                if(droneMoveListener != null) {

                    //liste des points de la mission effectués par le drône
                    List<LatLng> pointsMissions = new ArrayList<LatLng>();

                    //on récupère la mission recu
                    Mission mission = controlFragment.getDrone().getAttribute(AttributeType.MISSION);

                    //parcours de tous les missions items
                    for(MissionItem item :mission.getMissionItems()){
                        if(item.getType() == MissionItemType.WAYPOINT){
                            Waypoint point = (Waypoint)item;
                            //si c'est une waypoint, on rajoute les coordonnées
                            pointsMissions.add(new LatLng(point.getCoordinate().getLatitude(),point.getCoordinate().getLongitude()));
                        }
                    }
                    //appel de la méthode qui gère la liste des points d'une mission
                    droneMoveListener.droneReceivedMissionPoint(pointsMissions);
                }
                break;

            default:
                //Log.i("DRONE_EVENT", event);
                break;
        }
    }

    @Override
    public void onDroneServiceInterrupted(String errorMsg) {

    }

    //on set le listener que l'on va utiliser
    public void setDroneMoveListener(DroneActionMapListener droneMoveListener) {
        this.droneMoveListener = droneMoveListener;
    }

    /**  Interface: Handles Drone's movements  **/
    public interface DroneActionMapListener{
        /**
         * Méthode appelé quand le drone a changé de position.
         * @param position : nouvelle position du drône
         */
        void onDroneMove(LatLng position);

        /**
         * Méthode qui réagit à la réception de missions par le drone
         * @param pointsMissions : liste des points waypoint recu par le drone pour ca mission
         */
        void droneReceivedMissionPoint(List<LatLng> pointsMissions);
    }
}
