package projet.istic.fr.firedrone.map;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.ModelAPI.SIGAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.MeansItemStatus;
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
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by ramage on 20/04/16.
 */
public class MapMoyenFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnCameraChangeListener,
        View.OnClickListener,MethodCallWhenDrag, Observateur,Serializable {


    //item sélectionné dans le panel
    private Object itemSelected;

    //l'item sélectionné est a supprimé du panel une fois placé
    private boolean itemToRemove;

    private transient GoogleMap googleMap;
    private transient ImageButton suppressionMarker;
    private transient AbsoluteLayout.LayoutParams overlayLayoutParams;

    private transient Map<Marker,Object> mapMarkerItem = new HashMap<>();
    private transient ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;

    private transient View infoWindowContainer;
    private transient FrameLayout containerMap;
    private transient View viewFront;

    private transient TextView textView;
    private transient TextView button;
    private transient LatLng trackedPosition;
    private transient Marker markerSelected;

    private transient int popupXOffset;
    private transient int popupYOffset;

    private transient Handler handler;

    private transient Runnable positionUpdaterRunnable;

    //liste des Sig sur la carte
    private transient List<Sig> listSIG;

    private transient PanelListFragment panelListFragment;


    public MapMoyenFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        panelListFragment = (PanelListFragment) getArguments().getSerializable("panel");

        super.onCreate(savedInstanceState);
        getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MyObservable.getInstance().setFragment(this);

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

        if(googleMap != null) {
            createSIG();
        }
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

                /**  Button's Text  **/
                switch (meansItem.getStatus()) {
                    case STATUS_DEMANDE:
                        button.setEnabled(false);
                        button.setText(MeansItemStatus.STATUS_ARRIVE.description());
                        break;
                    case STATUS_VALIDE:
                        button.setEnabled(true);
                        button.setText(MeansItemStatus.STATUS_ARRIVE.description());
                        break;
                    case STATUS_ARRIVE:
                        button.setEnabled(true);
                        button.setText(MeansItemStatus.STATUS_ENGAGE.description());
                        break;
                    case STATUS_ENGAGE:
                        button.setEnabled(true);
                        button.setText(MeansItemStatus.STATUS_LIBERE.description());
                        break;
                    case STATUS_ENTRANSIT:
                        button.setEnabled(true);
                        button.setText(MeansItemStatus.STATUS_ARRIVE.description());
                        break;
                    case STATUS_LIBERE:
                        button.setEnabled(false);
                }
            }
        }
        return true;
    }

    /**
     *
     * @param marker
     */
    public void dragEnd(final Marker marker){
        for(Marker markerSet : mapMarkerItem.keySet()){
            if(markerSet.getId().equals(marker.getId())){
                Object object = mapMarkerItem.get(markerSet);
                if(object instanceof MeansItem) {
                    MeansItem meansItem = (MeansItem) object;
                    if(meansItem.getMsMeanId().equals("")){
                        // Si l'identifiant est nulle, on recherche l'identifiant dans la liste des identifiants
                        for(MeansItem moyenInter : InterventionSingleton.getInstance().getIntervention().getWays()){
                            if(moyenInter.getMsLongitude().equals(meansItem.getMsLongitude()) && moyenInter.getMsLatitude().equals(meansItem.getMsLatitude())){
                                meansItem.setMsMeanId(moyenInter.getMsMeanId());
                                break;
                            }
                        }
                    }
                    meansItem.setMsLongitude(String.valueOf(marker.getPosition().longitude));
                    meansItem.setMsLatitude(String.valueOf(marker.getPosition().latitude));

                    //**   If Mean is : ENGAGED and User move it, its status goes to : ENTRANSIT   **//
                    if (meansItem.getStatus() == MeansItemStatus.STATUS_ENGAGE) {
                        meansItem.setStatus(MeansItemStatus.STATUS_ENTRANSIT.state());
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));
                    }

                    MeansItemService.editMean(meansItem,getContext());
                }
                else if (object instanceof Resource){
                    final Resource resource = (Resource) object;
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(FiredroneConstante.END_POINT)
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setClient(new OkClient())
                            .build();
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
                            FiredroneConstante.getToastError(getContext()).show();
                        }
                    });
                }
            }
        }
    }


    /**
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap =googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, googleMap, null, this));
        googleMap.setOnMarkerClickListener(this);
        LatLng positionIntervention = new LatLng(Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLatitude()),Double.valueOf(InterventionSingleton.getInstance().getIntervention().getLongitude()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionIntervention, 16));

        infoWindowContainer.setVisibility(View.VISIBLE);

        createSIG();
        createMeansOnMap();
        createRessourceOnMap();
    }

    private void createRessourceOnMap(){
        //récupération en base de données des ressources de l'intervention
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FiredroneConstante.END_POINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

        interventionAPI.getResources(InterventionSingleton.getInstance().getIntervention().getId(), new Callback<List<Resource>>() {
            @Override
            public void success(List<Resource> resources, Response response) {
                if (resources != null) {
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

    /**
     * Put every MEANS on the map
     */
    private void createMeansOnMap(){
        List<MeansItem> moyens = InterventionSingleton.getInstance().getIntervention().getWays();
        if(moyens != null) {
            // Check every MEANS in order to find those which are ALREADY PRINTED on the MAP
            for (MeansItem moyen : moyens) {
                if (moyen.getMsLatitude() != null && moyen.getMsLongitude() != null  &&  moyen.getStatus() != MeansItemStatus.STATUS_LIBERE &&  moyen.getStatus() != MeansItemStatus.STATUS_REFUSE) {
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

        // récupération des Sig en base de données
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

    public void setItemSelected(Object item,boolean toRemove) {
        itemSelected =  item;
        itemToRemove = toRemove;
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
        if(itemSelected instanceof MeansItem) {
            MeansItem moyenItemSelected = (MeansItem) itemSelected;
            MeansItem meansItemCloned = moyenItemSelected.clone();
            Marker marker =addMeansOnMap(meansItemCloned,latLng );
            mapMarkerItem.put(marker, meansItemCloned);
            meansItemCloned.setMsLatitude(String.valueOf(latLng.latitude));
            meansItemCloned.setMsLongitude(String.valueOf(latLng.longitude));

            Date newDate = new Date();
            if(meansItemCloned.getMsMeanHCall() == null){
                meansItemCloned.setMsMeanHCall(FiredroneConstante.DATE_FORMAT.format(newDate));
                meansItemCloned.setStatus("D");
            }

            //on supprime l'item du panel après l'avoir ajouté
            if(itemToRemove){
               panelListFragment.removeItem(moyenItemSelected);
                MeansItemService.editMean(meansItemCloned,getContext());
                //on le met à nulle
                itemSelected = null;
            }
            else {
                MeansItemService.addMean(meansItemCloned,getContext(),false);
            }
        }else if(itemSelected instanceof EnumPointType){
            EnumPointType enumPointTypeSelected = (EnumPointType) itemSelected;
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

    /**
     * OnClick Listener for the BUTTON
     * This button has different behaviour regarding the MEAN'S STATUS
     * @param v
     */
    @Override
    public void onClick(View v) {
        Object object = mapMarkerItem.get(markerSelected);

        if(object instanceof MeansItem) {
            MeansItem meansItem = (MeansItem) object;
            Date newDate = new Date();

            /**  texte du bouton  **/
            switch (meansItem.getStatus()) {
                case STATUS_DEMANDE:
                    button.setText(MeansItemStatus.STATUS_VALIDE.description());
                    break;
                case STATUS_VALIDE:
                    meansItem.setMsMeanHArriv(FiredroneConstante.DATE_FORMAT.format(newDate));
                    meansItem.setStatus(MeansItemStatus.STATUS_ARRIVE.state());
                    MeansItemService.editMean(meansItem, getContext() );
                    setFrontMap(true);
                    break;

                case STATUS_ARRIVE:
                    meansItem.setStatus(MeansItemStatus.STATUS_ENGAGE.state());
                    meansItem.setMsMeanHEngaged(FiredroneConstante.DATE_FORMAT.format(newDate));
                    MeansItemService.editMean(meansItem, getContext() );
                    //**  Refresh the MEAN'S ICON  **/
                    markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));

                    setFrontMap(true);
                    break;

                case STATUS_ENGAGE:

                    meansItem.setMsMeanHFree(FiredroneConstante.DATE_FORMAT.format(newDate));
                    meansItem.setStatus(MeansItemStatus.STATUS_LIBERE.state());
                    MeansItemService.editMean(meansItem, getContext());

                    setFrontMap(true);
                    markerSelected.remove();
                    mapMarkerItem.remove(meansItem);
                    break;

                case STATUS_ENTRANSIT:
                    meansItem.setStatus(MeansItemStatus.STATUS_ARRIVE.state());
                    MeansItemService.editMean(meansItem, getContext());
                    //**  Refresh the MEAN'S ICON  **/
                    markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));
                    setFrontMap(true);

                    break;
            }

            /** TODO: DELETE THE COMMENTED CODE BELOW
            if ( meansItem.getMsMeanHArriv() == null ) {
                meansItem.setMsMeanHArriv(FiredroneConstante.DATE_FORMAT.format(newDate));
                MeansItemService.editMean(meansItem, getContext() );
                setFrontMap(true);
            }
            else {
                if (meansItem.getMsMeanHEngaged() == null) {
                    meansItem.setMsMeanHEngaged(FiredroneConstante.DATE_FORMAT.format(newDate));
                    MeansItemService.editMean(meansItem, getContext() );
                    // Refresh Mean's icon
                    markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));

                    setFrontMap(true);
                }
                else {
                    if( meansItem.getStatus() == MeansItemStatus.STATUS_ENTRANSIT ) {
                        meansItem.setStatus(false);
                        MeansItemService.editMean(meansItem, getContext());
                        // Refresh Mean's Icon
                        markerSelected.setIcon(BitmapDescriptorFactory.fromBitmap(meansItem.getBitmap()));
                        setFrontMap(true);
                    }
                    else {
                        if (meansItem.getMsMeanHFree() == null) {
                            meansItem.setMsMeanHFree(FiredroneConstante.DATE_FORMAT.format(newDate));
                            MeansItemService.editMean(meansItem,getContext());
                            setFrontMap(true);
                            markerSelected.remove();
                            mapMarkerItem.remove(meansItem);
                        }
                    }
                }
            }**/
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
            MapMoyenFragment myFragment = (MapMoyenFragment) getFragmentManager().findFragmentById(R.id.content_map_moyen);
            if (myFragment != null && myFragment.isVisible()) {
                //ICI ROMAIN
                googleMap.clear();
                mapMarkerItem.clear();
                if (listSIG != null) {
                    createSIG();
                }

                createMeansOnMap();
                createRessourceOnMap();
            }
        }
    }


}
