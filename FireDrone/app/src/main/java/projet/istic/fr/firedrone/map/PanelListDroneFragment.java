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

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.listener.ButtonNewDroneEventListener;
import projet.istic.fr.firedrone.listener.DroneEventListenerInterface;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MissionDrone;
import projet.istic.fr.firedrone.model.ModeMissionDrone;
import projet.istic.fr.firedrone.service.DroneService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;

/**
 * Created by Mamadian
 */
public class PanelListDroneFragment extends Fragment implements Serializable, DroneEventListenerInterface {

    /**   Reference to the Map Fragment   **/
    private transient MapDroneFragment mapDroneFragment;

    /**   CurrentDrone   **/
    private transient Drone currentDrone;

    //**   -   -  -    MODE Flag    -  -   -  **//
    /**  SEGMENT MODE Boolean Flag  **/
    private boolean segmentMode = false;
    /**  ZONE MODE Boolean Flag  **/
    private boolean zoneMode = false;
    /**  LOOP MODE Boolean Flag  **/
    private boolean loopMode = false;
    /**  EXCLUSION MODE Boolean Flag  **/
    private boolean exclusionMode = false;

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
    private transient Button buttonStart;
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
        defaultTextColor = buttonZone.getCurrentTextColor();
        defaultBackgroundColor = buttonZone.getDrawingCacheBackgroundColor();

        //**   -  Initialization of Buttons  -   **//
        initButtons();

        return view;
    }


    /**
     * initialize Buttons
     */
    private void initButtons() {
        initAskADroneButton();
        initTrajectoryButtons();
        initExclusionButton();
        initControlsButtons();

        if ( ! InterventionSingleton.getInstance().getIntervention().getDrones().isEmpty()) {
            disableAllButtons();
        }
    }


    /**
     * Disable all the Buttons
     * This method is called when a drone is already Use.
     */
    private void disableAllButtons() {
        buttonZone.setEnabled(false);
        buttonAskADrone.setEnabled(false);
        buttonAskADrone.setEnabled(false);
        buttonZone.setEnabled(false);
        buttonSegment.setEnabled(false);
        buttonLoop.setEnabled(false);
        buttonExclusion.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(false);

        buttonFreeDrone.setVisibility(View.VISIBLE);
        buttonFreeDrone.setEnabled(true);
    }


    /**
     * Init Trajectory Buttons
     */
    private void initTrajectoryButtons(){

        //**          -    Zone    -         **//
        buttonZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!zoneMode) {
                    setZoneMode(true);
                    setSegmentMode(false);
                    setLoopMode(false);

                    buttonZone.setTextColor(Color.BLACK);
                    buttonZone.setBackgroundColor(Color.BLUE);

                    buttonLoop.setTextColor(defaultTextColor);
                    buttonLoop.setBackgroundColor(defaultBackgroundColor);
                    buttonSegment.setTextColor(defaultTextColor);
                    buttonSegment.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });

        //**          -    Segment    -         **//
        buttonSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!segmentMode) {
                    setZoneMode(false);
                    setSegmentMode(true);
                    setLoopMode(false);

                    buttonSegment.setTextColor(Color.BLACK);
                    buttonSegment.setBackgroundColor(Color.BLUE);

                    buttonLoop.setTextColor(defaultTextColor);
                    buttonLoop.setBackgroundColor(defaultBackgroundColor);
                    buttonZone.setTextColor(defaultTextColor);
                    buttonZone.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });


        //**          -    Loop    -         **//
        buttonLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loopMode) {
                    setZoneMode(false);
                    setSegmentMode(false);
                    setLoopMode(true);

                    buttonLoop.setTextColor(Color.BLACK);
                    buttonLoop.setBackgroundColor(Color.BLUE);

                    buttonSegment.setTextColor(defaultTextColor);
                    buttonSegment.setBackgroundColor(defaultBackgroundColor);
                    buttonZone.setTextColor(defaultTextColor);
                    buttonZone.setBackgroundColor(defaultBackgroundColor);
                }
            }
        });
    }


    /**
     *
     */
    private void initExclusionButton() {
        buttonExclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exclusionMode = !exclusionMode;
            }
        });
    }

    /**
     *
     */
    private void initControlsButtons() {

        /**    -- StartDrone --    **/
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DroneService.startDrone(
                        mapDroneFragment.getCurrentDrone(),
                        new MissionDrone(getCurrentMode(), mapDroneFragment.getListPointsMissionDrone() ),
                        v.getContext());
            }
        });

        /**    -- StopDrone --    **/
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drone currentDrone = mapDroneFragment.getCurrentDrone();
                DroneService.stopDrone(currentDrone, v.getContext());
                buttonStop.setEnabled(false);

                DroneService.stopDrone(mapDroneFragment.getCurrentDrone(), v.getContext());

            }
        });

        /**    -- FreeDrone --    **/
        buttonFreeDrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonFreeDrone.setVisibility(View.INVISIBLE);
                buttonAskADrone.setVisibility(View.VISIBLE);
                buttonAskADrone.setEnabled(true);
                buttonExclusion.setEnabled(false);
                buttonZone.setEnabled(false);
                buttonSegment.setEnabled(false);
                buttonLoop.setEnabled(false);
                buttonExclusion.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(false);

                DroneService.freeDrone(mapDroneFragment.getCurrentDrone(), v.getContext());;
            }
        });

    }


    /**
     *
     * @return
     */
    private String getCurrentMode() {
        if(segmentMode) {
            return ModeMissionDrone.SEGMENT.getValue();
        }
        if(zoneMode) {
            return ModeMissionDrone.ZONE.getValue();
        }
        if(loopMode) {
            return ModeMissionDrone.LOOP.getValue();
        }

        return ModeMissionDrone.SEGMENT.getValue();
    }

    /**
     * Ask a DRONE for this INTERVENTION
     */
    private void initAskADroneButton(){
        buttonAskADrone.setOnClickListener(new ButtonNewDroneEventListener(this));
    }


    //**   -   -  -    Getters & Setters    -  -   -  **//

    public boolean isSegmentMode() {
        return segmentMode;
    }

    public void setSegmentMode(Boolean segmentMode) {
        this.segmentMode = segmentMode;
    }

    public boolean isZoneMode() {
        return zoneMode;
    }

    public void setZoneMode(Boolean zoneMode) {
        this.zoneMode = zoneMode;
    }

    public boolean isLoopMode() {
        return loopMode;
    }

    public void setLoopMode(Boolean loopMode) {
        this.loopMode = loopMode;
    }

    public boolean isExclusionMode() {
        return exclusionMode;
    }

    public void setExclusionMode(boolean exclusionMode) {
        this.exclusionMode = exclusionMode;
    }



    //**   -    -     - - DroneEventListenerInterface - -    -     -   **//

    /**
     *
     */
    @Override
    public void update() {

        Log.e("- - - * * * * MY FLAG", "HERE YOU ARE MOTHER FUCKER");
        Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        if(!intervention.getDrones().isEmpty()) {

            mapDroneFragment.setCurrentDrone(currentDrone);

            buttonFreeDrone.setVisibility(View.VISIBLE);
            buttonAskADrone.setVisibility(View.INVISIBLE);
            buttonExclusion.setEnabled(true);
            buttonZone.setEnabled(true);
            buttonSegment.setEnabled(true);
            buttonLoop.setEnabled(true);
            buttonExclusion.setEnabled(true);
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(true);

            mapDroneFragment.initPositionDroneOnMap();
            mapDroneFragment.setCurrentDrone(intervention.getDrones().get(0));
        }
        else {
            Toast.makeText(getContext(), "Vous n'avez pas pu avoir de Drone", Toast.LENGTH_LONG).show();
        }
    }
}

