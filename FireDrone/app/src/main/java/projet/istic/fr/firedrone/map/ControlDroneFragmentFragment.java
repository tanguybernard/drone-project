package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.listener.ActionOnDroneEventListener;
import projet.istic.fr.firedrone.listener.DroneEventListenerFragmentInterface;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;

/**
 * Group A.
 */
public class ControlDroneFragmentFragment extends Fragment implements Serializable, DroneEventListenerFragmentInterface {

    /**   Reference to the Map Fragment   **/
    private transient MapDroneFragment mapDroneFragment;

    /**   CurrentDrone   **/
    private transient Drone currentDrone;

    //**   -   -  -    TRAJECTORY MODE    -  -   -  **//
    /**  SEGMENT MODE Boolean Flag  **/
    private ModeMissionDrone mode;

    //**   -   -  -    Button    -  -   -  **//
    /**  ASK FOR A DRONE BUTTON  **/
    private transient Button buttonAskADrone;
    /**  ZONE BUTTON  **/
    private transient Button buttonZone;
    /**  SEGMENT BUTTON  **/
    private transient Button buttonSegment;
    /**  LOOP BUTTON  **/
    private transient Button buttonLoop;
    /**  EXCLUSION BUTTON Flag  **/
    private transient Button buttonExclusion;
    /**  START BUTTON Flag  **/
    private  Button buttonStart;
    /**  STOP BUTTON Flag  **/
    private transient Button buttonStop;
    /**  FREE DRONE BUTTON Flag  **/
    private transient Button buttonFreeDrone;


    //**   -   -  -    Default Colors    -  -   -  **//
    /**  LOOP BUTTON Flag  **/
    private int defaultTextColor;
    /**  LOOP BUTTON Flag  **/
    private int defaultBackgroundColor;
    //**   -   -  -         - -          -  -   -  **//


    /**
     *
     * @param inflater
     * @param container
     * @param saveInstantState
     * @return
     */
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle saveInstantState) {


        //**   -  Layout  -   **//
        final View view = inflater.inflate(R.layout.fragment_list_panel_drone, container, false);
        //**   -  Reference to Map Drone Fragment  -   **//
        mapDroneFragment = (MapDroneFragment) getArguments().getSerializable("map") ;
        //**   -  Trajectory Buttons  -   **//
        buttonAskADrone = (Button) view.findViewById(R.id.buttonAskADrone);

        //**   -  Trajectory Buttons  -   **//
        buttonZone = (Button) view.findViewById(R.id.Bzone);
        buttonSegment = (Button) view.findViewById(R.id.Bsegment);
        buttonLoop = (Button) view.findViewById(R.id.Bboucle);

        //**   -  Exclusion Button  -   **//
        buttonExclusion = (Button) view.findViewById(R.id.buttonExclusion);

        //**   -  Control Buttons  -   **//
        buttonStart = (Button) view.findViewById(R.id.buttonStart);
        buttonStop = (Button) view.findViewById(R.id.buttonStop);
        buttonFreeDrone = (Button) view.findViewById(R.id.buttonFreeDrone);

        //**   -  DefaultColor  -   **//
        defaultTextColor = Color.BLACK;
        defaultBackgroundColor = Color.TRANSPARENT;
        //defaultBackgroundColor = ((ColorDrawable) buttonZone.getBackground()).getColor();

        //**   -  DEFAULT MODE  -   **//
        setMode(ModeMissionDrone.NONE);

        //**   -  Initialization of Buttons  -   **//
        initButtons();

        return view;
    }


    /**
     * initialize all Buttons of this view.
     */
    private void initButtons() {
        List<Drone> drones = InterventionSingleton.getInstance().getIntervention().getDrones();

        initAskADroneButton();
        initTrajectoryButtons();


        //**  Block the Buttons if a Drone (Yours or Other's) is already Active  **//
        if ( ! drones.isEmpty()) {
            currentDrone = drones.get(0);
            if (( ! currentDrone.getIdUser().equals(UserSingleton.getInstance().getUser().getId()))) {
                anotherDroneIsAlreadyActiveLAYOUTMODE();
            }
            yourDroneIsActiveLAYOUTMODE();
            initControlsButtons();
        }

    }

    //**    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -    **//
    //**   -    -     - - Initialization (EventListener) of Button  - -    -     -   **//

    /**
     * Init Trajectory Buttons
     */
    private void initTrajectoryButtons(){

        //**          -    Loop    -         **//
        buttonLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMode() != ModeMissionDrone.LOOP) {
                    setMode(ModeMissionDrone.LOOP);
                    mapDroneFragment.setMode(ModeMissionDrone.LOOP);
                    mapDroneFragment.updatePolyline();

                    buttonExclusion.setEnabled(false);

                    buttonLoop.setTextColor(Color.BLACK);
                    buttonLoop.setBackgroundColor(Color.BLUE);

                    buttonSegment.setTextColor(defaultTextColor);
                    buttonSegment.setBackgroundColor(defaultBackgroundColor);
                    buttonZone.setTextColor(defaultTextColor);
                    buttonZone.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });


        //**          -    Segment    -         **//
        buttonSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMode() != ModeMissionDrone.SEGMENT) {
                    setMode(ModeMissionDrone.SEGMENT);
                    mapDroneFragment.setMode(ModeMissionDrone.SEGMENT);
                    mapDroneFragment.updatePolyline();

                    buttonExclusion.setEnabled(false);

                    buttonSegment.setTextColor(Color.BLACK);
                    buttonSegment.setBackgroundColor(Color.RED);


                    buttonLoop.setTextColor(defaultTextColor);
                    buttonLoop.setBackgroundColor(defaultBackgroundColor);
                    buttonZone.setTextColor(defaultTextColor);
                    buttonZone.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });


        //**          -    Zone    -         **//
        buttonZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMode() != ModeMissionDrone.ZONE) {
                    setMode(ModeMissionDrone.ZONE);
                    mapDroneFragment.setMode(ModeMissionDrone.ZONE);
                    mapDroneFragment.updatePolyline();

                    buttonExclusion.setEnabled(true);

                    buttonZone.setTextColor(Color.BLACK);
                    buttonZone.setBackgroundColor(Color.GREEN);

                    buttonLoop.setTextColor(defaultTextColor);
                    buttonLoop.setBackgroundColor(defaultBackgroundColor);
                    buttonSegment.setTextColor(defaultTextColor);
                    buttonSegment.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });

        //**          -    Exclusion    -         **//
        buttonExclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMode() != ModeMissionDrone.EXCLUSION) {
                    buttonExclusion.setTextColor(Color.WHITE);
                    buttonExclusion.setBackgroundColor(Color.DKGRAY);

                    setMode(ModeMissionDrone.EXCLUSION);
                    mapDroneFragment.setMode(ModeMissionDrone.EXCLUSION);
                    mapDroneFragment.updatePolyline();
                }
                else {
                    buttonExclusion.setTextColor(defaultTextColor);
                    buttonExclusion.setBackgroundColor(defaultBackgroundColor);

                    setMode(ModeMissionDrone.ZONE);
                    mapDroneFragment.setMode(ModeMissionDrone.ZONE);
                    mapDroneFragment.updatePolyline();
                }

            }
        });

    }


    /**
     *
     */
    private void initControlsButtons() {
        buttonLoop.setTextColor(defaultTextColor);
        buttonLoop.setBackgroundColor(defaultBackgroundColor);

        buttonSegment.setTextColor(defaultTextColor);
        buttonSegment.setBackgroundColor(defaultBackgroundColor);

        buttonZone.setTextColor(defaultTextColor);
        buttonZone.setBackgroundColor(defaultBackgroundColor);


        /**    -- StartDrone --    **/
        Log.e("FLAG", "- - - - - -     --> BUTTON START" + buttonStart);
        Log.e("FLAG", "- - - - - -     -->        CURRENTDRONE " + currentDrone);
        buttonStart.setOnClickListener(new ActionOnDroneEventListener(
                this, mapDroneFragment, currentDrone,
                ActionOnDroneEventListener.ActionEventDroneType.START));


        /**    -- StopDrone --    **/
        buttonStop.setOnClickListener(new ActionOnDroneEventListener(
                this, mapDroneFragment, currentDrone,
                ActionOnDroneEventListener.ActionEventDroneType.STOP));


        /**    -- FreeDrone --    **/
        buttonFreeDrone.setOnClickListener(new ActionOnDroneEventListener(
                this, mapDroneFragment, currentDrone,
                ActionOnDroneEventListener.ActionEventDroneType.FREE));
    }


    /**
     * Init CREATE Drone Button
     * Ask a DRONE for this INTERVENTION
     */
    private void initAskADroneButton(){
        buttonAskADrone.setOnClickListener(new ActionOnDroneEventListener(
                this, mapDroneFragment, new Drone(),
                ActionOnDroneEventListener.ActionEventDroneType.CREATE));
    }


    //**   -   -     -   -   -   -   -   -   -   -   - **//
    //**   -    -     - - LAYOUT MODE - -    -     -   **//

    /**
     * ENABLE ONLY "ASK A NEW DRONE" BUTTON
     * When no drone is in the Intervention
     */
    private void noDroneLAYOUTMODE() {
        buttonDefaultColors();
        buttonAskADrone.setVisibility(View.VISIBLE);
        buttonAskADrone.setEnabled(true);
        buttonExclusion.setEnabled(false);
        buttonExclusion.setTextColor(Color.LTGRAY);
        buttonLoop.setEnabled(false);
        buttonLoop.setTextColor(Color.LTGRAY);
        buttonSegment.setEnabled(false);
        buttonSegment.setTextColor(Color.LTGRAY);
        buttonStart.setEnabled(false);
        buttonStart.setTextColor(Color.LTGRAY);
        buttonStop.setEnabled(false);
        buttonStop.setTextColor(Color.LTGRAY);
        buttonZone.setEnabled(false);
        buttonZone.setTextColor(Color.LTGRAY);
        buttonFreeDrone.setEnabled(false);
        buttonFreeDrone.setVisibility(View.INVISIBLE);
    }

    /**
     * Disable ALL BUTTONS
     * When SIT user can't use a Drone because Another SIT is
     */
    private void anotherDroneIsAlreadyActiveLAYOUTMODE() {
        buttonAskADrone.setEnabled(false);
        buttonExclusion.setEnabled(false);
        buttonLoop.setEnabled(false);
        buttonSegment.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonZone.setEnabled(false);
        buttonFreeDrone.setEnabled(false);
        buttonFreeDrone.setVisibility(View.INVISIBLE);
    }

    /**
     * ENABLE ALL BUTTONS (Hide: Ask A new Drone Button, Exclusion Button)
     * This method is called when the actual SIT user is using its Drone.
     */
    private void yourDroneIsActiveLAYOUTMODE() {
        buttonAskADrone.setVisibility(View.INVISIBLE);
        buttonAskADrone.setEnabled(false);
        buttonExclusion.setEnabled(true);
        buttonLoop.setEnabled(true);
        buttonSegment.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonStart.setTextColor(Color.BLACK);
        buttonStop.setEnabled(false);
        buttonStop.setTextColor(Color.LTGRAY);
        buttonZone.setEnabled(true);
        buttonFreeDrone.setEnabled(true);
        buttonFreeDrone.setVisibility(View.VISIBLE);
    }

    /**
     *
     */
    private void yourDroneIsMovingLAYOUTMODE() {
        buttonAskADrone.setVisibility(View.INVISIBLE);
        buttonAskADrone.setEnabled(false);
        buttonExclusion.setEnabled(false);
        buttonLoop.setEnabled(false);
        buttonSegment.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStart.setTextColor(Color.LTGRAY);
        buttonStop.setEnabled(true);
        buttonStart.setTextColor(Color.BLACK);
        buttonZone.setEnabled(false);
        buttonFreeDrone.setEnabled(true);
        buttonFreeDrone.setVisibility(View.VISIBLE);
    }


    /**
     * Set all Buttons to their Default Color
     */
    private void buttonDefaultColors() {
        buttonSegment.setTextColor(defaultTextColor);
        buttonSegment.setBackgroundColor(defaultBackgroundColor);

        buttonZone.setTextColor(defaultTextColor);
        buttonZone.setBackgroundColor(defaultBackgroundColor);

        buttonLoop.setTextColor(defaultTextColor);
        buttonLoop.setBackgroundColor(defaultBackgroundColor);

        buttonExclusion.setTextColor(defaultTextColor);
        buttonExclusion.setBackgroundColor(defaultBackgroundColor);
    }


    //**    -   -   -   -   -   -   -   -   -   -   -   -    **//
    //**   -    -     - - Getters & Setters - -    -     -   **//

    public ModeMissionDrone getMode() {
        return mode;
    }

    public void setMode(ModeMissionDrone mode) {
        this.mode = mode;
    }


    //**   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   **//
    //**   -    -     - - DroneEventListenerFragmentInterface - -    -     -   **//

    /**
     *
     */
    @Override
    public void updateCreateDrone() {
        Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        if(!intervention.getDrones().isEmpty()) {
            currentDrone = intervention.getDrones().get(0);
            mapDroneFragment.setCurrentDrone(currentDrone);

            yourDroneIsActiveLAYOUTMODE();
            initControlsButtons();

            //**  Refresh MAP  **//
            mapDroneFragment.refreshPointDrone();
            //**  -  -   -  -  **//
        }
        else {
            Toast.makeText(getContext(), "Vous n'avez pas pu avoir de Drone", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateDeleteDrone() {
        noDroneLAYOUTMODE();
        mapDroneFragment.clearMissionOnMap();
        mapDroneFragment.setMode(ModeMissionDrone.NONE);
        //**  Refresh MAP  **//
        mapDroneFragment.refreshPointDrone();
        //**  -  -   -  -  **//
    }



    @Override
    public void updateStartDrone() {
        yourDroneIsMovingLAYOUTMODE();
        //**  Refresh MAP  **//
        mapDroneFragment.refreshPointDrone();
        //**  -  -   -  -  **//
    }

    @Override
    public void updateStopDrone() {
        mapDroneFragment.clearMissionOnMap();
        //**  Refresh MAP  **//
        mapDroneFragment.refreshPointDrone();
        //**  -  -   -  -  **//
    }

}

