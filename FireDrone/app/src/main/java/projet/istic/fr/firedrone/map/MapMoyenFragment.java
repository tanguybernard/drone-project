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
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.SIGAPI;
import projet.istic.fr.firedrone.ModelAPI.UserApi;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.SIG;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ramage on 20/04/16.
 */
public class MapMoyenFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnCameraChangeListener,
        View.OnClickListener,MethodCallWhenDrag{

    private static MapMoyenFragment INSTANCE;

    //item sélectionné dans le panel
    private MeansItem moyenItemSelected;
    //l'item sélectionné est a supprimé du panel une fois placé
    private boolean itemToRemove;

    private GoogleMap googleMap;
    private ImageButton suppressionMarker;
    private AbsoluteLayout.LayoutParams overlayLayoutParams;

    private Map<Marker,MeansItem> mapMeansItem = new HashMap<>();
    private ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;
    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes

    private View infoWindowContainer;
    private TextView textView;
    private TextView button;
    private LatLng trackedPosition;
    private Marker markerSelected;

    private int popupXOffset;
    private int popupYOffset;

    private Handler handler;

    private Runnable positionUpdaterRunnable;

    //liste des SIG sur la carte
    private List<SIG> listSIG;

    public static MapMoyenFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapMoyenFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)// get JSON answer
                .build();

        final SIGAPI sigApi = restAdapter.create(SIGAPI.class);

        //récupération des SIG en base de données
        sigApi.getSIGs(new Callback<List<SIG>>() {
                           @Override
                           public void success(List<SIG> sigs, Response response) {

                               listSIG = sigs;
                           }

                           @Override
                           public void failure(RetrofitError error) {
                           }
                       });


    }

    FrameLayout containerMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        Projection projection = googleMap.getProjection();
        trackedPosition = marker.getPosition();
        Point trackedPoint = projection.toScreenLocation(trackedPosition);
        trackedPoint.y -= popupYOffset / 2 ;
        LatLng newCameraLocation = projection.fromScreenLocation(trackedPoint);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(newCameraLocation), 500, null);

        MeansItem meansItem = mapMeansItem.get(marker);
        textView.setText(meansItem.getMsMeanCode());
        infoWindowContainer.bringToFront();
        markerSelected = marker;

        if(meansItem.getMsMeanHArriv()==null) {
            button.setText("Arrivé");
        }else{
            if(meansItem.getMsMeanHEngaged() == null){
                button.setText("Engagé");
            }else{

            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap =googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerDragListener(new DragRemoveOnMapListener(suppressionMarker, googleMap, null, this));
        googleMap.setOnMarkerClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rennes_istic, 16));

        if(listSIG != null) {
            //création des points SIG
            for (SIG sig : listSIG) {
                System.out.println(sig.getId() + " " + sig.getType());
            }
        }
    }

    public void setMoyenItemSelected(MeansItem pMoyenItemSelected) {
        moyenItemSelected =  pMoyenItemSelected;
        itemToRemove = false;
    }

    public void setMoyenItemAddSelected(MeansItem pMoyenItemSelected) {
        moyenItemSelected =  pMoyenItemSelected;
        itemToRemove = true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //si un moyen a été sélectionné
        if(moyenItemSelected != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng).draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(moyenItemSelected.getResource())));
            mapMeansItem.put(marker, moyenItemSelected);

            //on supprime l'item du panel après l'avoir ajouté
            if(itemToRemove){
                PanelMapMoyenFragment.getInstance().removeItem(moyenItemSelected);
                //on le met à nulle
                moyenItemSelected = null;
            }
        }
        containerMap.bringToFront();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        containerMap.bringToFront();
    }

    @Override
    public void onClick(View v) {
        MeansItem moyenItem = mapMeansItem.get(markerSelected);
        if(moyenItem.getMsMeanHArriv() == null){
            moyenItem.setMsMeanHArriv("heure arrivée");
            containerMap.bringToFront();
        }else{
            if(moyenItem.getMsMeanHEngaged() == null){
                moyenItem.setMsMeanHEngaged("heure engagement");
                containerMap.bringToFront();
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
        containerMap.bringToFront();
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
}
