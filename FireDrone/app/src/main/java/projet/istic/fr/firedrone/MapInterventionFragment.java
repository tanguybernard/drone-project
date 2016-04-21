package projet.istic.fr.firedrone;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nduquesne on 20/04/16.
 */
public class MapInterventionFragment extends SupportMapFragment implements
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback {


        private GoogleMap myMap;
        //ensemle des marqueurs, clé : identifiant du marqueur, valeur : marqueur
        private Map<String, Marker> listMarkers = null;

        private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (savedInstanceState == null) {
                listMarkers = new LinkedHashMap<String, Marker>();
                getMapAsync(this);
            }
        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            //passage en mode Earth (avec les routes)
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            myMap = googleMap;

            MarkerOptions marker = new MarkerOptions();

            marker.position(rennes_istic);

            myMap.addMarker(marker);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rennes_istic, 16));


        }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

}
