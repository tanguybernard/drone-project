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
import java.util.List;

import projet.istic.fr.firedrone.model.ImageItem;

/**
 * Created by tbernard on 25/05/16.
 */
public class ImageMapFragment extends SupportMapFragment implements
        GoogleMap.OnCameraChangeListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap myMap;

    private ImageFragment imageFragment = ImageFragment.getInstance();

    //ensemble des marqueurs, clé : identifiant du marqueur, valeur : marqueur
    private ArrayList<MarkerOptions> listMarkers = null;

    private List<ImageItem> listImage = new ArrayList<ImageItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            listMarkers = new ArrayList<>();
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
        if(listImage.isEmpty()) {

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
            for (int i = 0; i < listImage.size(); i++) {
                //Si on a bien une latitude et une longitude, on met le marqueur
                if ((listImage.get(i).getLatitude() != null) && (listImage.get(i).getLongitude() != null)) {


                    //Cast de la latitude et de la longitude
                    try {
                        Double latitude = Double.parseDouble(listImage.get(i).getLatitude());
                        Double longitude = Double.parseDouble(listImage.get(i).getLongitude());

                        //Nouvel objet LatLng
                        LatLng coordonnees = new LatLng(latitude, longitude);

                        //On récupère la référence pour l'afficher dans l'infobulle du marqueur
                        String idImage = listImage.get(i).getId();
                        //String dateInter = listImage.get(i).getDate();

                        String snippet = listImage.get(i).getLatitude()+"; "+listImage.get(i).getLongitude();



                        //Définition du marqueur
                        MarkerOptions marker = new MarkerOptions().position(coordonnees).title(snippet);


                        listMarkers.add(marker);
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
                final int padding = 100;
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

    public List<ImageItem> getListImage() {
        return listImage;
    }

    public void setListImage(List<ImageItem> listImage) {
        this.listImage = listImage;
    }


    /**
     * When Click on Marker: update the list
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng markerPosition = marker.getPosition();

        imageFragment.updateListImage(markerPosition);

        return false;
    }
}
