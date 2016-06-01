package projet.istic.fr.firedrone.listener;

import android.view.View;

import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.MissionDrone;
import projet.istic.fr.firedrone.service.DroneService;


/**
 * @author Group A.
 */
public class ActionOnDroneEventListener implements View.OnClickListener {

    public enum ActionEventDroneType {
        CREATE, START, STOP, FREE;
    }

    /** **/
    private transient DroneEventListenerFragmentInterface droneEventListenerFragmentInterface;

    /** **/
    private ActionEventDroneType actionEventDroneType;

    /** **/
    private Drone drone;

    /** **/
    private MissionDrone mission;

    //**     Constructor    **//

    /**
     *
     * @param droneEventListenerFragmentInterface
     * @param drone
     * @param actionEventDroneType
     * @param mission
     */
    public ActionOnDroneEventListener(DroneEventListenerFragmentInterface droneEventListenerFragmentInterface, Drone drone, ActionEventDroneType actionEventDroneType, MissionDrone mission) {
        this.droneEventListenerFragmentInterface = droneEventListenerFragmentInterface;
        this.drone = drone;
        this.actionEventDroneType = actionEventDroneType;
        this.mission = mission;
    }


    @Override
    public void onClick(View v) {
        switch (actionEventDroneType) {
            case START:
                DroneService.startDrone(droneEventListenerFragmentInterface, drone, mission, v.getContext());
                break;
            case STOP:
                DroneService.stopDrone(droneEventListenerFragmentInterface, drone, v.getContext());
                break;
            case CREATE:
                DroneService.askNewDrone(droneEventListenerFragmentInterface, v.getContext());
                break;
            case FREE:
                DroneService.freeDrone(droneEventListenerFragmentInterface, drone, v.getContext());
                break;
        }

    }
}
