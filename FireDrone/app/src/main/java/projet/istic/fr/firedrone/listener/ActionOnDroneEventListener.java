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
    private transient DroneMissionFragmentInterface droneMissionFragmentInterface;

    /** **/
    private ActionEventDroneType actionEventDroneType;


    /** **/
    private Drone drone;



    //**     Constructor    **//


    public ActionOnDroneEventListener(DroneEventListenerFragmentInterface droneEventListenerFragmentInterface,
                                      DroneMissionFragmentInterface droneMissionFragmentInterface,
                                      Drone drone,
                                      ActionEventDroneType actionEventDroneType) {
        this.droneEventListenerFragmentInterface = droneEventListenerFragmentInterface;
        this.droneMissionFragmentInterface = droneMissionFragmentInterface;
        this.drone = drone;
        this.actionEventDroneType = actionEventDroneType;
    }


    @Override
    public void onClick(View v) {
        switch (actionEventDroneType) {
            case START:
                DroneService.startDrone(droneEventListenerFragmentInterface,
                        drone,
                        new MissionDrone(droneMissionFragmentInterface.getMode().getValue(), droneMissionFragmentInterface.getListPointsMissionDrone()),
                        v.getContext());
                break;
            case STOP:
                DroneService.stopDrone(droneEventListenerFragmentInterface,
                        drone,
                        v.getContext());
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
