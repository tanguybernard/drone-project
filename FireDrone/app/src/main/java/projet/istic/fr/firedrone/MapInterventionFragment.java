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
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.model.Intervention;

/**
 * Created by nduquesne on 20/04/16.
 */
public class MapInterventionFragment extends SupportMapFragment implements
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

        private GoogleMap myMap;

        private InterventionsListFragment interListFragment = InterventionsListFragment.getInstance();

        //ensemble des marqueurs, clé : identifiant du marqueur, valeur : marqueur
        private Map<String, Marker> listMarkers = null;

        private List<Intervention> listInter = new ArrayList<Intervention>();

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

            //Boolean pour vérifier si on a au moins une intervention avec des coordonnées correctes
            //pour ne pas faire planter le builder au moment du build
            boolean verifInter = false;

            //Si la liste est vide, on met les coordonnées de l'Ille-et-Vilaine
            if(listInter.isEmpty()) {

                final LatLng coordIlle = new LatLng(48.2292016, -1.5300694999999678);
                //Zoom sur la zone des marqueurs
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordIlle, 8));
                    }
                });

            }
            else {
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
                            String idInter = listInter.get(i).getId();
                            String addressInter = listInter.get(i).getAddress();
                            String codeSinistreInter = listInter.get(i).getSinisterCode();
                            String dateInter = listInter.get(i).getDate();

                            String snippet = "Adresse : " + addressInter;

                            // TODO : Récupérer d'autres infos à mettre dans l'infobulle (voir tuto google)
                            // TODO : Gérer la sélection d'un marqueur et mettre en évidence dans la liste (et inversement)

                            //Définition du marqueur
                            MarkerOptions marker = new MarkerOptions().position(coordonnees).title(idInter).snippet(snippet);

                            //Ajout du marqueur
                            myMap.addMarker(marker);

                            //Récupération de sa position pour déterminer le zoom sur les interventions
                            builder.include(coordonnees);

                            //J'ai au moins une intervention avec de bonnes coordonnées
                            verifInter = true;

                        } catch (NumberFormatException e) {
                            System.out.println("Coordonnées fausses");
                        }
                    }
                }

                //Si la vérification de l'existence d'une coordonnée est bonne
                if(verifInter = true) {
                    //délimitation du zoom sur la carte par rapport à l'ensemble des marqueurs
                    final LatLngBounds bounds = builder.build();
                    //Définition du padding autour des marqueurs
                    final int padding = 30;
                    //Zoom sur la zone des marqueurs
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                        }
                    });
                }
                //Sinon on met l'Ille-et-Vilaine si pas d'adresse valide
                else {
                    final LatLng coordIlle = new LatLng(48.2292016, -1.5300694999999678);
                    //Zoom sur la zone des marqueurs
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordIlle, 8));
                        }
                    });
                }
            }


            myMap.setOnMarkerClickListener(this);
        }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    public List<Intervention> getListInter() {
        return listInter;
    }

    public void setListInter(List<Intervention> listInter) {
        this.listInter = listInter;
    }


    /**
     * When Click on Marker: update the list and set Focus on intervention
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        String idInter = marker.getTitle();

        interListFragment.UpdateList(idInter);

        return false;
    }
}
