package projet.istic.fr.firedrone.listener;

import android.view.View;

import projet.istic.fr.firedrone.service.DroneService;


/**
 *
 */
public class ButtonNewDroneEventListener implements View.OnClickListener {

    private DroneEventListenerInterface droneEventListenerInterface;

    //**     Constructor    **//
    public ButtonNewDroneEventListener(DroneEventListenerInterface d) {
        this.droneEventListenerInterface = d;
    }


    @Override
    public void onClick(View v) {
        DroneService.askNewDrone(droneEventListenerInterface, v.getContext());
    }
}
