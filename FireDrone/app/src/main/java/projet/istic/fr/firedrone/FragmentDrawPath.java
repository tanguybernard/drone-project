package projet.istic.fr.firedrone;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import projet.istic.fr.firedrone.listener.DroneListenerEvent;


public class FragmentDrawPath extends SupportMapFragment implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback  {

    private GoogleMap myMap;
    private List<LatLng> arrayPoints = null;//List of mark points
    private PolylineOptions polylineOptions;//add lines bettwen markers
    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes
    private static FragmentDrawPath INSTANCE;

    Marker markerDrone;




    public static FragmentDrawPath getInstance() {
        if(INSTANCE == null){
            INSTANCE = new FragmentDrawPath();
        }
        return INSTANCE;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            arrayPoints = new ArrayList<LatLng>();
            getMapAsync(this);
        }

        ((MainActivity)getActivity()).setDroneMoveListener(new DroneListenerEvent.DroneMoveListener() {
            @Override
            public void onDroneMove(LatLng point) {
                //Lorsque le drone change de position il appelle cette méthode


                System.out.println("run marker");
                System.out.println(point);
                if(markerDrone != null) {
                    markerDrone.setPosition(point);
                    markerDrone.setVisible(true);
                }



            }

            @Override
            public void droneReceivedMissionPoint(List<LatLng> pointsMissions) {

            }
        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnCameraChangeListener(this);

        myMap = googleMap;
        MarkerOptions marker=new MarkerOptions();
        marker.position(rennes_istic);

        myMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rennes_istic, 16));



        markerDrone = myMap.addMarker(new MarkerOptions()
                        .position(rennes_istic)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_36_36))
        );


    }


    @Override
    public void onMapClick(LatLng point) {
        Log.v("MainActivity", "onMapClick(LatLng point)");

        //add marker
        putMarker(point, arrayPoints.size());
        // settin polyline in the map
        polylineOptions = new PolylineOptions();
        //polygonOptions.strokeColor(Color.RED);
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        arrayPoints.add(point);


        polylineOptions.addAll(arrayPoints);
        myMap.addPolyline(polylineOptions);


        Marker marker = myMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.7750, 122.4183))
                .title("San Francisco")
                .snippet("Population: 776733"));

        Marker mar = myMap.addMarker(new MarkerOptions()
                .position(rennes_istic)
                .title("Rennes")
                .snippet("Population: ??"));

        List<LatLng> childList = new ArrayList<LatLng>(); // new routs
        childList.add(new LatLng(48.1157045,-1.6379779));
        childList.add(new LatLng(48.1155670,- 1.6372698));
        childList.add(new LatLng(48.1153670,- 1.6372698));
        childList.add(new LatLng(48.1150670,- 1.6372698));


        //(rennes_istic);
        //animateMarker(myMap,mar,childList,false);

    }


    /**
     * Put a marker in google maps
     * @param clickedPosition
     * @param num for indicate the number of the merker
     */
    public void putMarker(LatLng clickedPosition,int num){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(200, 50, conf);
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);


        //canvas.drawText(Integer.toString(num), 0, 50, paint); // paint defines the text color, stroke width, size
        myMap.addMarker(new MarkerOptions()
                        .position(clickedPosition)
                        .title(Integer.toString(num))
        );
    }

    @Override
    public void onMapLongClick(LatLng point) {
        myMap.clear();
        arrayPoints.clear();
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        System.out.println("onCameraChange");

    }

    public List<LatLng> getArrayPoints(){
        return arrayPoints;
    }




    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }




    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {


        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 10));

        animateMarker(myMap, marker, directionPoint, false);
    }


    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
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
}
