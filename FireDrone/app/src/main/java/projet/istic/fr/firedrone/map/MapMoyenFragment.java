package projet.istic.fr.firedrone.map;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.ModelAPI.SIGAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.Resource;
import projet.istic.fr.firedrone.model.Sig;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.synchro.MyObservable;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ramage on 20/04/16.
 */
public class MapMoyenFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnCameraChangeListener,
        View.OnClickListener,MethodCallWhenDrag, Observateur {


    //item sélectionné dans le panel
    private MeansItem moyenItemSelected;

    private EnumPointType enumPointTypeSelected;

    //l'item sélectionné est a supprimé du panel une fois placé
    private boolean itemToRemove;

    private GoogleMap googleMap;
    private ImageButton suppressionMarker;
    private AbsoluteLayout.LayoutParams overlayLayoutParams;

    private Map<Marker,Object> mapMarkerItem = new HashMap<>();
    private ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;

    private View infoWindowContainer;
    private FrameLayout containerMap;
    private View viewFront;

    private TextView textView;
    private TextView button;
    private LatLng trackedPosition;
    private Marker markerSelected;

    private int popupXOffset;
    private int popupYOffset;

    private Handler handler;

    private Runnable positionUpdaterRunnable;

    //liste des Sig sur la carte
    private List<Sig> listSIG;

    private PanelMapMoyenFragment panelMapMoyenFragment;


    public MapMoyenFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        panelMapMoyenFragment = (PanelMapMoyenFragment) getArguments().getSerializable("panel");

        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MyObservable.getInstance().ajouterObservateur(this);

        View rootView = inflater.inflate(R.layout.fragment_map_moyen, null);
        containerMap = (FrameLayout) rootView.findViewById(R.id.container_map);
        FrameLayout mapView = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);
        containerMap.addView(mapView, new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        infoWindowContainer = rootView.findViewById(R.id.container_popup);

        infoWindowLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                popupXOffset = infoWindowContainer.getWidth() / 2;
                popupYOffset = infoWindowContainer.getHeight();
            }
        };
        infoWindowContainer.getViewTreeObserver().addOnGlobalLayoutListener(infoWindowLayoutListener);
        overlayLayoutParams = (AbsoluteLayout.LayoutParams) infoWindowContainer.getLayoutParams();

        textView = (TextView) infoWindowContainer.findViewById(R.id.textview_title);
        button = (TextView) infoWindowContainer.findViewById(R.id.button_change_state);

        //création du bouton de suppression des marqueurs
        suppressionMarker = new ImageButton(getContext());
        suppressionMarker.setPadding(5, 5, 5, 5);
        suppressionMarker.setBackgroundColor(DragRemoveOnMapListener.COLOR_BUTTON);
        suppressionMarker.setImageResource(R.drawable.delete_24dp_rouge);
        suppressionMarker.setVisibility(View.INVISIBLE);

        //ajout du bouton de suppression et placement
        mapView.addView(suppressionMarker, new FrameLayout.LayoutParams(150, 150, Gravity.CENTER_HORIZONTAL));

        button.setOnClickListener(this);
        return rootView;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Object object = mapMarkerItem.get(marker);
        button.setVisibility(View.VISIBLE);
        if(object !=null && object instanceof MeansItem) {
            MeansItem meansItem = (MeansItem) object;
            //si on est sur un marker de type moyen
            if (markerSelected != null && markerSelected.getId().equals(marker.getId()) && infoWindowContainer == viewFront) {
                setFrontMap(true);
            } else {
                Projection projection = googleMap.getProjection();
                trackedPosition = marker.getPosition();
                Point trackedPoint = projection.toScreenLocation(trackedPosition);
                trackedPoint.y -= popupYOffset / 2;
                LatLng newCameraLocation = projection.fromScreenLocation(trackedPoint);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(newCameraLocation), 500, null);

                textView.setText(meansItem.getMsMeanCode());
                setFrontMap(false);
                markerSelected = marker;

                //texte du bouton
                if (meansItem.getMsMeanHArriv() == null) {
                    button.setText("Arrivé");
                } else {
                    if (meansItem.getMsMeanHEngaged() == null) {
                        button.setText("Engagé");
                    } else {
                        if(meansItem.isRedeployement()){
                            button.setText("Redéployé");
                        }else {
                            if (meansItem.getMsMeanHFree() == null) {
                                button.setText("Libéré");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void dragEnd(final Marker marker){
        for(Marker markerSet : mapMarkerItem.keySet()){
            if(markerSet.getId().equals(marker.getId())){
                Object object = mapMarkerItem.get(markerSet);
                if(object instanceof MeansItem) {
                    MeansItem meansItem = (MeansItem) object;
                    if(meansItem.getMsMeanId().equals("")){
                       //si l'identifiant est nulle, on recherche l'identifiant dans la liste des identifiants
                        for(MeansItem moyenInter : InterventionSingleton.getInstance().getIntervention().getWays()){
                            if(moyenInter.getMsLongitude().equals(meansItem.getMsLongitude()) && moyenInter.getMsLatitude().equals(meansItem.getMsLatitude())){
                                meansItem.setMsMeanId(moyenInter.getMsMeanId());
                                break;
                            }
                        }
                    }
                    meansItem.setMsLongitude(String.valueOf(marker.getPosition().longitude));
                    meansItem.setMsLatitude(String.valueOf(marker.getPosition().latitude));
                    //cas ou le moyen était déjà engagé, après le déplacement on le passe en mode redéploiement
                    if(meansItem.getMsMeanHEngaged() != null ) {
                        meansItem.setRedeployement(true);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));
                    }

                    MeansItemService.editMean(meansItem,getContext());
                }else if (object instanceof Resource){
                    final Resource resource = (Resource) object;
                    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
                    final InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);
                    interventionAPI.getResources(InterventionSingleton.getInstance().getIntervention().getId(), new Callback<List<Resource>>() {
                        @Override
                        public void success(List<Resource> resources, Response response) {
                            for(Resource r : resources){
                                if(r.getLongitude().equals(resource.getLongitude()) && r.getLatitude().equals(resource.getLatitude())){
                                    resource.setId(r.getId());
                                    break;
                                }
                            }
                            resource.setLatitude(marker.getPosition().latitude);
                            resource.setLongitude(marker.getPosition().longitude);
                            interventionAPI.updateResource(InterventionSingleton.getInstance().getIntervention().getId(), resource, new Callback<List<Resource>>() {
                                @Override
                                public void success(List<Resource> resources, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    FiredroneConstante.getToastError(getContext()).show();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            FiredroneConstante.getToastError(getContext()).show();
                        }
                    });
                }
                break;
            }
        }
    }

    @Override
    public boolean displayButton(Marker marker) {
        for(Marker mar : mapMarkerItem.keySet()){
            if(mar.getId().equals(marker.getId())){
                if(mapMarkerItem.get(mar) instanceof MeansItem){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void dragRemove(Marker marker) {
        for(Marker mar : mapMarkerItem.keySet()){
            if(mar.getId().equals(marker.getId())){
                Object object = mapMarkerItem.get(mar);
                if(object instanceof Resource){
                    Resource resource = (Resource) object;
                    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
                    InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

                    interventionAPI.deleteResource(InterventionSingleton.getInstance().getIntervention().getId(), resource.getId(), new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("loldsfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsds");

        this.googleMap =googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, googleMap, null, this));
        googleMap.setOnMarkerClickListener(this);
        LatLng positionIntervention = new LatLng(Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLatitude()),Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLongitude()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionIntervention, 16));

        infoWindowContainer.setVisibility(View.VISIBLE);
        if(listSIG != null) {
            createSIG();
        }

        createMoyenOnMap();

        crateRessourceOnMap();

    }

    private void crateRessourceOnMap(){
        //récupération en base de données des ressources de l'intervention
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

        interventionAPI.getResources(InterventionSingleton.getInstance().getIntervention().getId(), new Callback<List<Resource>>() {
            @Override
            public void success(List<Resource> resources, Response response) {
                if(resources!=null) {
                    for (Resource r : resources) {
                        EnumPointType enumPointType = EnumPointType.valueOf(r.getType());
                        Marker marker = addResourceOnMap(enumPointType, new LatLng(r.getLatitude(), r.getLongitude()));
                        mapMarkerItem.put(marker, r);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });
    }

    private void createMoyenOnMap(){
        //placement des moyens qui sont sur la carte
        List<MeansItem> moyens = InterventionSingleton.getInstance().getIntervention().getWays();
        if(moyens != null) {
            //parcours de tous les moyens pour trouvés ceux déjà positionné sur la carte
            for (MeansItem moyen : moyens) {
                if (moyen.getMsLatitude() != null && moyen.getMsLongitude() != null  && moyen.getMsMeanHFree() == null) {
                    //on ajoute le moyen à la carte

                    if (!moyen.getMsLatitude().equals("") && !moyen.getMsLongitude().equals("")) {
                        Marker marker = addMeansOnMap(moyen, new LatLng(Double.parseDouble(moyen.getMsLatitude()), Double.parseDouble(moyen.getMsLongitude())));
                        mapMarkerItem.put(marker, moyen);

                    }

                }
            }
        }
    }

    private void createSIG(){


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();


        final SIGAPI sigApi = restAdapter.create(SIGAPI.class);

        //récupération des Sig en base de données
        sigApi.getSIGs(new Callback<List<Sig>>() {
            @Override
            public void success(List<Sig> sigs, Response response) {

                listSIG = sigs;
                //si google map existe déjà on place les sig
                //création des points Sig
                for (Sig sig : listSIG) {
                    //type WATER
                    if(sig.getType().equals("WATER")){
                        googleMap.addMarker( new MarkerOptions().position(new LatLng(Double.parseDouble(sig.getLatitude()),Double.parseDouble(sig.getLongitude())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.water))
                        );
                    }else if(sig.getType().equals("HYDRANT")){
                        //type HYDRANT
                        googleMap.addMarker( new MarkerOptions().position(new LatLng(Double.parseDouble(sig.getLatitude()),Double.parseDouble(sig.getLongitude())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hydrant))
                        );
                    }else if(sig.getType().equals("CHEMICALS")){
                        //type CHEMICALS
                        googleMap.addMarker( new MarkerOptions().position(new LatLng(Double.parseDouble(sig.getLatitude()),Double.parseDouble(sig.getLongitude())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.chemicals))
                        );
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });
    }

    public void setMoyenItemSelected(MeansItem pMoyenItemSelected) {
        moyenItemSelected =  pMoyenItemSelected;
        itemToRemove = false;
    }

    public void setMoyenItemAddSelected(MeansItem pMoyenItemSelected) {
        moyenItemSelected =  pMoyenItemSelected;
        itemToRemove = true;
    }

    public void setPointAddSelected(EnumPointType pEnumPolintTypeSelected) {
        enumPointTypeSelected =  pEnumPolintTypeSelected;
        moyenItemSelected = null;
    }

    private Marker addMeansOnMap(MeansItem meansItem,LatLng latLng){
        return googleMap.addMarker(new MarkerOptions()
                .position(latLng).draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap())));
                //.icon(BitmapDescriptorFactory.fromResource(meansItem.getResource())));
    }

    private Marker addResourceOnMap(EnumPointType enumPointType,LatLng latLng){
        return  googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(enumPointType.getResource())));
        //.icon(BitmapDescriptorFactory.fromResource(meansItem.getResource())));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //si un moyen a été sélectionné
        if(moyenItemSelected != null) {
            MeansItem meansItemCloned = moyenItemSelected.clone();
            Marker marker =addMeansOnMap(meansItemCloned,latLng );
            mapMarkerItem.put(marker, meansItemCloned);
            meansItemCloned.setMsLatitude(String.valueOf(latLng.latitude));
            meansItemCloned.setMsLongitude(String.valueOf(latLng.longitude));

            Date newDate = new Date();
            if(meansItemCloned.getMsMeanHCall() == null){
                meansItemCloned.setMsMeanHCall(FiredroneConstante.DATE_FORMAT.format(newDate));
            }


            //on supprime l'item du panel après l'avoir ajouté
            if(itemToRemove){
               panelMapMoyenFragment.removeItem(moyenItemSelected);
                MeansItemService.editMean(meansItemCloned,getContext());
                //on le met à nulle
                moyenItemSelected = null;;
            }else{
                MeansItemService.addMean(meansItemCloned,getContext(),false);
            }
        }else if(enumPointTypeSelected != null){
            //ajout du marker sur la carte
            Marker marker = addResourceOnMap(enumPointTypeSelected,latLng);


            //ajout en base de données
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
            InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

            //ressource a ajouté en base de données
            Resource resource = new Resource(enumPointTypeSelected.name(),latLng);

            mapMarkerItem.put(marker, resource);

            interventionAPI.addResource(InterventionSingleton.getInstance().getIntervention().getId(), resource, new Callback<List<Resource>>() {
                @Override
                public void success(List<Resource> resources, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    FiredroneConstante.getToastError(getContext()).show();
                }
            });
        }
       setFrontMap(true);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        containerMap.bringToFront();
    }

    private void setFrontMap(boolean frontMap){
        if(frontMap){
            containerMap.bringToFront();
            viewFront = containerMap;
        }else{
            infoWindowContainer.bringToFront();
            viewFront = infoWindowContainer;
        }

    }

    //au clique sur le bouton
    @Override
    public void onClick(View v) {
        Object object = mapMarkerItem.get(markerSelected);

        if(object instanceof MeansItem) {
            MeansItem moyenItem = (MeansItem) object;
            Date newDate = new Date();
            if (moyenItem.getMsMeanHArriv() == null) {
                moyenItem.setMsMeanHArriv(FiredroneConstante.DATE_FORMAT.format(newDate));
                MeansItemService.editMean(moyenItem,getContext());
                setFrontMap(true);
            } else {
                if (moyenItem.getMsMeanHEngaged() == null) {
                    moyenItem.setMsMeanHEngaged(FiredroneConstante.DATE_FORMAT.format(newDate));
                    MeansItemService.editMean(moyenItem,getContext());
                    // Refresh Mean's icon
                    markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(moyenItem.getBitmap()));

                    setFrontMap(true);
                } else {
                    if(moyenItem.isRedeployement()){
                        moyenItem.setRedeployement(false);
                        MeansItemService.editMean(moyenItem,getContext());
                        // Refresh Mean's icon
                        markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(moyenItem.getBitmap()));
                        setFrontMap(true);
                    }else {
                        if (moyenItem.getMsMeanHFree() == null) {
                            moyenItem.setMsMeanHFree(FiredroneConstante.DATE_FORMAT.format(newDate));
                            MeansItemService.editMean(moyenItem,getContext());
                            setFrontMap(true);
                            markerSelected.remove();
                            mapMarkerItem.remove(moyenItem);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        infoWindowContainer.getViewTreeObserver().removeGlobalOnLayoutListener(infoWindowLayoutListener);
        handler.removeCallbacks(positionUpdaterRunnable);
        handler = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler(Looper.getMainLooper());
        positionUpdaterRunnable = new PositionUpdaterRunnable();
        handler.post(positionUpdaterRunnable);
    }

    @Override
    public void dragStart() {
        setFrontMap(true);
    }

    //thread qui va déplacé la fenêtre d'information du marqueur
    private class PositionUpdaterRunnable implements Runnable {
        private int lastXPosition = Integer.MIN_VALUE;
        private int lastYPosition = Integer.MIN_VALUE;


        @Override
        public void run() {
            handler.postDelayed(this, 16);

            if (trackedPosition != null && infoWindowContainer.getVisibility() == View.VISIBLE) {
                Point targetPosition = getMap().getProjection().toScreenLocation(trackedPosition);

                if (lastXPosition != targetPosition.x || lastYPosition != targetPosition.y) {

                    overlayLayoutParams.x = targetPosition.x - popupXOffset;
                    overlayLayoutParams.y = targetPosition.y - popupYOffset -30  -30;
                    infoWindowContainer.setLayoutParams(overlayLayoutParams);

                    lastXPosition = targetPosition.x;
                    lastYPosition = targetPosition.y;
                }
            }
        }
    }


    @Override
    public void actualiser(Observable o) {
        if(o instanceof MyObservable){
            MapMoyenFragment myFragment = (MapMoyenFragment)getFragmentManager().findFragmentById(R.id.content_map_moyen);
            if (myFragment != null && myFragment.isVisible()) {
                //ICI ROMAIN
                googleMap.clear();
                mapMarkerItem.clear();
                if(listSIG != null) {
                    createSIG();
                }

                createMoyenOnMap();

                crateRessourceOnMap();
            }
        }
    }


}
