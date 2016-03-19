package projet.istic.fr.firedrone;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentDrawPath extends SupportMapFragment implements
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback  {

    private GoogleMap myMap;
    private List<LatLng> arrayPoints = null;//List of mark points
    private PolylineOptions polylineOptions;//add lines bettwen markers
    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);//LatLng of ISTIC rennes


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayPoints = new ArrayList<LatLng>();
        getMapAsync(this);

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



}
