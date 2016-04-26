package projet.istic.fr.firedrone.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.MainActivity;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.listener.DroneListenerEvent;


public class MapDroneFragment extends SupportMapFragment implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback, ManagePolyline {

    private GoogleMap myMap;
    //ensemle des marqueurs, clé : identifiant du marqueur, valeur : marqueur
    private Map<String, Marker> listMarkers = null;

    private PolylineOptions polylineOptions;//add lines bettwen markers
    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes

    private Polyline polyline;

    //options polyline du drone
    private PolylineOptions polylineOptionsDrone;
    private Polyline polylineDrone;
    //dernier point atteint par le drône
    private LatLng dernierPoint;

    //bouton de suppression de marqueur
    private ImageButton suppressionMarker;

    //marqueur du drône
    Marker markerDrone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            listMarkers = new LinkedHashMap<String, Marker>();
            getMapAsync(this);
        }

        ((MainActivity) getActivity()).setDroneMoveListener(new DroneListenerEvent.DroneActionMapListener() {
            @Override
            public void onDroneMove(LatLng point) {

                //Lorsque le drone change de position il appelle cette méthode
                if (markerDrone != null) {
                    markerDrone.setPosition(point);
                    markerDrone.setVisible(true);
                    addPolylineDrone(point);
                }

            }

            //lorsqu'un drone recoit un ordre de mission, il appelle cette méthode là
            @Override
            public void droneReceivedMissionPoint(List<LatLng> pointsMissions) {
                //on cherche à trouver tous les marqueurs proche des points (à moins de 1 mètres) reçues par le drône
                for(Marker marker : listMarkers.values()){

                    //pour tous les points recues par le drône
                    for(LatLng point:pointsMissions) {
                        float[] result = new float[1];
                        //calcul de la distance entre le marqueur et le point
                        Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude, point.latitude, point.longitude, result);
                        //si on est à moins de 1 mètre on change la couleur de l'icône
                        if (result[0]<1) {
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        }
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            myMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, myMap, this,null));
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

        MarkerOptions marker = new MarkerOptions();

        marker.position(rennes_istic);

        myMap.addMarker(marker);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rennes_istic, 16));

        markerDrone = myMap.addMarker(new MarkerOptions()
                        .position(rennes_istic)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_36_36))
        );

        //initialisation de l'option de polyline du drône
        initPolylineDrone();

        //création d'un listener pour écouter le mouvement du drag and drop sur les marqueurs de la carte
        myMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, myMap, this, null));
    }


    @Override
    public void onMapClick(LatLng point) {
        Log.v("MainActivity", "onMapClick(LatLng point)");

        //add marker
        putMarker(point, listMarkers.size());

        Marker mar = myMap.addMarker(new MarkerOptions()
                .position(rennes_istic)
                .title("Rennes")
                .snippet("Population: ??"));
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




    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }




    public void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {


        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 10));

        animateMarker(myMap, marker, directionPoint, false);
    }


    private void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                System.out.println("run marker");
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition(directionPoint.get(i));
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 3000);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private List<LatLng> getListPoint(){
        List<LatLng> listPoint = new ArrayList<>();
        for(Marker marker: listMarkers.values()){
            listPoint.add(marker.getPosition());
        }
        return listPoint;
    }

    @Override
    public void updatePolyline() {
        // settin polyline in the map
        polylineOptions = new PolylineOptions();
        //polygonOptions.strokeColor(Color.RED);
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        polylineOptions.addAll(getListPoint());

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

    private void initPolylineDrone(){

        // settin polyline in the map
        polylineOptionsDrone = new PolylineOptions();
        //polygonOptions.strokeColor(Color.RED);
        polylineOptionsDrone.color(Color.YELLOW);
        polylineOptionsDrone.width(7);
    }


    private void addPolylineDrone(LatLng point){
        if(dernierPoint != null) {
            polylineDrone = myMap.addPolyline(polylineOptionsDrone.add(dernierPoint, point));
        }
        dernierPoint = point;
    }


    @Override
    public void removePoint(Marker marker) {
        //suppression d'un marqueur sur la carte
        listMarkers.remove(marker.getId());
        //mise à jour des polylines
        updatePolyline();
    }

    @Override
    public void addPolyline(Marker marker) {
        //ajout d'un marqueur
        listMarkers.put(marker.getId(), marker);
        //mise à jour des polylines
        updatePolyline();
    }
}
