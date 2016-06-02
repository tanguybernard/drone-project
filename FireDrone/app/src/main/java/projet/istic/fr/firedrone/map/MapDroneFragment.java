package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.listener.DroneMissionFragmentInterface;
import projet.istic.fr.firedrone.model.Drone;
import projet.istic.fr.firedrone.model.PointMissionDrone;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.synchro.MyObservable;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class MapDroneFragment extends SupportMapFragment implements
        GoogleMap.OnMapClickListener,
        DroneMissionFragmentInterface,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback, ManagePolyline, Serializable,Observateur {

    private transient GoogleMap myMap;
    //ensemle des marqueurs, clé : identifiant du marqueur, valeur : marqueur
    private transient Map<String, Marker> listMarkers = null;

    private transient PolylineOptions polylineOptions;//add lines bettwen markers

    private transient Polyline polyline;


    //options polyline du drone
    private transient PolylineOptions polylineOptionsDrone;
    private transient Polyline polylineDrone;
    //dernier point atteint par le drône
    private transient LatLng dernierPoint;

    //bouton de suppression de marqueur
    private transient ImageButton suppressionMarker;

    //marqueur du drône
    transient Marker markerDrone;

    /**   CurrentDrone   **/
    transient private Drone currentDrone;

    private ModeMissionDrone mode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            listMarkers = new LinkedHashMap<String, Marker>();
            getMapAsync(this);
        }

        //ControlDroneFragmentFragment controlDroneFragment = (ControlDroneFragmentFragment) getArguments().getSerializable("panel");

        /**  Initialize Current Drone **/
        if (!InterventionSingleton.getInstance().getIntervention().getDrones().isEmpty()) {
            currentDrone = InterventionSingleton.getInstance().getIntervention().getDrones().get(0);
        }
        //**   -  DEFAULT MODE  -   **//
        setMode(ModeMissionDrone.NONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyObservable.getInstance().setFragment(this);

        FrameLayout mapView = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        //création du bouton de suppression des marqueurs
        suppressionMarker = new ImageButton(getContext());
        suppressionMarker.setPadding(5, 5, 5, 5);
        suppressionMarker.setBackgroundColor(DragRemoveOnMapListener.COLOR_BUTTON);
        suppressionMarker.setImageResource(R.drawable.delete_24dp_rouge);
        suppressionMarker.setVisibility(View.INVISIBLE);

        //ajout du bouton de suppression et placement
        mapView.addView(suppressionMarker, new FrameLayout.LayoutParams(150, 150, Gravity.CENTER_HORIZONTAL));


        if(myMap != null){
            //création d'un listener pour écouter le mouvement du drag and drop sur les marqueurs de la carte
            myMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, myMap, this, null));
        }
        return mapView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //passage en mode Earth (avec les routes)
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        myMap = googleMap;

        LatLng positionIntervention = new LatLng(Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLatitude()),Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLongitude()));
        MarkerOptions marker = new MarkerOptions();

        marker.position(positionIntervention);

        myMap.addMarker(marker);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionIntervention, 16));



        //initialisation de l'option de polyline du drône
        initPolylineDrone();

        //création d'un listener pour écouter le mouvement du drag and drop sur les marqueurs de la carte
        myMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, myMap, this, null));


        refreshPointDrone();
        //initPositionDroneOnMap();
    }


    @Override
    public void onMapClick(LatLng point) {
        Log.v("MainActivity", "onMapClick(LatLng point)");

        //add marker
        if (getMode() != ModeMissionDrone.NONE) {
            putMarker(point, listMarkers.size());
        }
    }


    /**
     * Put a marker in google maps
     * @param clickedPosition
     * @param num for indicate the number of the merker
     */
    public void putMarker(LatLng clickedPosition,int num){

        //canvas.drawText(Integer.toString(num), 0, 50, paint); // paint defines the text color, stroke width, size
        Marker marker = myMap.addMarker(new MarkerOptions()
                .position(clickedPosition)
                .title(Integer.toString(num)).draggable(true));
        addPolyline(marker);

    }


    @Override
    public void onMapLongClick(LatLng point) {
        //myMap.clear();
        //listMarkers.clear();
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        System.out.println("onCameraChange");

    }

    public Collection<LatLng> getListMarkers(){
        return getListPoint();
    }


    /**
     * Actualize the position of the Drone
     */
    public void refreshPointDrone(){
        List<Drone> drones=InterventionSingleton.getInstance().getIntervention().getDrones();

        if(drones.size() >0) {
            LatLng position = new LatLng(Double.valueOf(drones.get(0).getLatitude()), Double.valueOf(drones.get(0).getLongitude()));

            if (markerDrone == null) {
                markerDrone = myMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_36_36)));
            }else{
                markerDrone.setPosition(position);
            }
        }
    }

    /**
     * Clean the Map from Mission Markers
     */
    public void clearMissionOnMap(){
        for(String key : listMarkers.keySet() ) {
            listMarkers.get(key).remove();
        }
        listMarkers.clear();

        updatePolyline();
    }


    /**
     * Update Polyline (Strokes) on the map
     */
    @Override
    public void updatePolyline() {
        polylineOptions = new PolylineOptions();
        switch (getMode()) {
            case EXCLUSION:
                polylineOptions.color(Color.DKGRAY);
                break;
            case LOOP:
                polylineOptions.color(Color.BLUE);
                break;
            case SEGMENT:
                polylineOptions.color(Color.RED);
                break;
            case ZONE:
                polylineOptions.color(Color.GREEN);
                break;
        }
        polylineOptions.width(5);
        polylineOptions.addAll(getListPoint());
        if(mode == ModeMissionDrone.LOOP && listMarkers.size() >2) {
            polylineOptions.add(listMarkers.values().iterator().next().getPosition());
        }
        //s'il existe déjà un polyline, on le supprime
        if(polyline != null){
            polyline.remove();
        }
        //récupération du polyline
        polyline=myMap.addPolyline(polylineOptions);

        int i=0;
        //on remet dans l'ordre les numéros des marqueurs
        for( Marker marker :listMarkers.values() ){
            marker.setTitle(String.valueOf(i));
            i++;
        }
    }

    /**
     *
     */
    private void initPolylineDrone(){
        // settin polyline in the map
        polylineOptionsDrone = new PolylineOptions();
        //polygonOptions.strokeColor(Color.RED);
        polylineOptionsDrone.color(Color.YELLOW);
        polylineOptionsDrone.width(7);
    }

    /**
     *
     * @param point
     */
    private void addPolylineDrone(LatLng point){
        if(dernierPoint != null) {
            polylineDrone = myMap.addPolyline(polylineOptionsDrone.add(dernierPoint, point));
        }
        dernierPoint = point;
    }


    /**
     * suppression d'un marqueur sur la carte
     * @param marker
     */
    @Override
    public void removePoint(Marker marker) {
        listMarkers.remove(marker.getId());
        //mise à jour des polylines
        updatePolyline();
    }

    /**
     *
     * @param marker
     */
    @Override
    public void addPolyline(Marker marker) {
        //ajout d'un marqueur
        listMarkers.put(marker.getId(), marker);
        //mise à jour des polylines
        updatePolyline();
    }


    //**  - Getter and Setter -  **//

    /**
     * Get the List of all Points of the Drone's Mission
     * @return
     */
    @Override
    public List<PointMissionDrone> getListPointsMissionDrone() {
        List<PointMissionDrone> listPoint = new ArrayList<>();

        for(Marker marker: listMarkers.values()) {
            listPoint.add(new PointMissionDrone(marker.getPosition().latitude, marker.getPosition().longitude));
        }

        return listPoint;
    }

    /**
     *
     * @return
     */
    private List<LatLng> getListPoint(){
        List<LatLng> listPoint = new ArrayList<>();
        for(Marker marker: listMarkers.values()){
            listPoint.add(marker.getPosition());
        }
        return listPoint;
    }

    public Drone getCurrentDrone() {
        return currentDrone;
    }

    public void setCurrentDrone(Drone currentDrone) {
        this.currentDrone = currentDrone;
    }

    public void setMode(ModeMissionDrone mode) {
        this.mode = mode;
    }

    @Override
    public ModeMissionDrone getMode() {
        return this.mode;
    }

    @Override
    public void actualiser(Observable o) {
        if(o instanceof MyObservable) {
            if(this.isVisible()) {
                refreshPointDrone();
            }
        }

    }
}
