package projet.istic.fr.firedrone;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import projet.istic.fr.firedrone.model.Intervention;

/**
 * Created by nduquesne on 20/04/16.
 */
public class MapInterventionFragment extends SupportMapFragment implements
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback {

        private GoogleMap myMap;


        //ensemle des marqueurs, clé : identifiant du marqueur, valeur : marqueur
        private Map<String, Marker> listMarkers = null;

        private ArrayList<Intervention> listInter = new ArrayList<Intervention>();

        //private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes


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


            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for(int i = 0; i < listInter.size(); i++) {
                if((listInter.get(i).getLatitude() != null) && (listInter.get(i).getLongitude() != null)) {
                    Double latitude = Double.parseDouble(listInter.get(i).getLatitude());
                    Double longitude = Double.parseDouble(listInter.get(i).getLongitude());

                    LatLng coordonnees = new LatLng(latitude, longitude);

                    String refInter = listInter.get(i).getId();

                    MarkerOptions marker = new MarkerOptions().position(coordonnees).title(refInter);

                    myMap.addMarker(marker);

                    builder.include(marker.getPosition());
                }
                else{
                    MarkerOptions marker1 = new MarkerOptions().position(new LatLng(48.122834, -1.655931)).title("intervention 1 Chat dans l'arbre");
                    myMap.addMarker(marker1);

                    MarkerOptions marker2 = new MarkerOptions().position(new LatLng(48.134597 , -1.647305)).title("intervention 2 Parc des gayeulles");
                    myMap.addMarker(marker2);

                    MarkerOptions marker3 = new MarkerOptions().position(new LatLng(48.115434, -1.638722)).title("intervention 3 ISTIC");
                    myMap.addMarker(marker3);

                    builder.include(marker1.getPosition());
                    builder.include(marker2.getPosition());
                    builder.include(marker3.getPosition());

                }
            }

            LatLngBounds bounds = builder.build();
            int padding = 10; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cu);
        }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    public ArrayList<Intervention> getListInter() {
        return listInter;
    }

    public void setListInter(ArrayList<Intervention> listInter) {
        this.listInter = listInter;
    }
}
