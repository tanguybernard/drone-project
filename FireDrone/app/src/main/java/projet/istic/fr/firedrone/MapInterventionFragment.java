package projet.istic.fr.firedrone;

import android.os.Bundle;

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


        //ensemble des marqueurs, clé : identifiant du marqueur, valeur : marqueur
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
        public void onMapReady(final GoogleMap googleMap) {
            //passage en mode Earth (avec les routes)
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            myMap = googleMap;

            //Sert à définir les limites de l'ensemble des marqueurs
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            MarkerOptions marker;

            //Parcours de la liste des interventions
            for (int i = 0; i < listInter.size(); i++) {
                //Si on a bien une latitude et une longitude, on met le marqueur
                if ((listInter.get(i).getLatitude() != null) && (listInter.get(i).getLongitude() != null)) {
                    //Cast de la latitude et de la longitude
                    try {
                        Double latitude = Double.parseDouble(listInter.get(i).getLatitude());
                        Double longitude = Double.parseDouble(listInter.get(i).getLongitude());

                        //Nouvel objet LatLng
                        LatLng coordonnees = new LatLng(latitude, longitude);

                        //On récupère la référence pour l'afficher dans l'infobulle du marqueur
                        String refInter = listInter.get(i).getId();

                        // TODO : Récupérer d'autres infos à mettre dans l'infobulle (voir tuto google)
                        // TODO : Gérer la sélection d'un marqueur et mettre en évidence dans la liste (et inversement)

                        //Définition du marqueur
                        marker = new MarkerOptions().position(coordonnees).title(refInter);

                        //Ajout du marqueur
                        myMap.addMarker(marker);

                        //Récupération de sa position pour déterminer le zoom sur les interventions
                        builder.include(coordonnees);
                    }
                    catch(NumberFormatException e) {
                        System.out.println("Coordonnées fausses");
                    }
                }

                //A supprimer après, le temps d'avoir des valeurs de latitude et longitude en base
                else {

                    //Nouvel objet LatLng
                    LatLng coordIlle = new LatLng(48.2292016, -1.5300694999999678);

                    builder.include(coordIlle);

                }
            }

            //if (builder != null) {
                //délimitation du zoom sur la carte par rapport à l'ensemble des marqueurs
                final LatLngBounds bounds = builder.build();
                //Définition du padding autour des marqueurs
                final int padding = 0;
                //Zoom sur la zone des marqueurs
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    }
                });
            //}
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
