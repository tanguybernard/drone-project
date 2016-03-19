package projet.istic.fr.firedrone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.interfaces.TowerListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TowerListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback {

    //tower pour se connecter au drone et recevoir les évènements du drone
    private ControlTower controlTower;


    private DrawerLayout myDrawer;
    private GoogleMap myMap;

    private List<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;

    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);

    @Override
    protected void onStart() {
        super.onStart();
        this.controlTower.connect(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.controlTower.disconnect();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialisation de la tower
        this.controlTower = new ControlTower(getApplicationContext());

        arrayPoints = new ArrayList<LatLng>();
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_fiche:
                break;
            case R.id.nav_moyen:
                break;
            case R.id.nav_rapport:
                break;
            case R.id.nav_directive:
                break;
            case R.id.nav_plan:
                break;
            case R.id.nav_parcours:
                SupportMapFragment supportMapFragment = new SupportMapFragment();
                supportMapFragment.getMapAsync(this);
                fragment = supportMapFragment;
                break;
            case R.id.nav_controle:
                fragment=new DroneControlFragment();
                break;
            case R.id.nav_image:
                break;
        }
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).addToBackStack("detailFragment").commit();
        }
        myDrawer.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
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




/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fiche) {
            // Handle the camera action
        } else if (id == R.id.nav_moyen) {

        } else if (id == R.id.nav_rapport) {

        } else if (id == R.id.nav_directive) {

        } else if (id == R.id.nav_plan) {

        } else if (id == R.id.nav_parcours) {

        } else if (id == R.id.nav_controle) {

        } else if (id == R.id.nav_image) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/



    @Override
    public void onMapClick(LatLng point) {
        Log.v("MainActivity", "onMapClick(LatLng point)");

        //add marker
        putMarker(point,arrayPoints.size());
        // settin polyline in the map
        polylineOptions = new PolylineOptions();
        //polygonOptions.strokeColor(Color.RED);
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        arrayPoints.add(point);


        polylineOptions.addAll(arrayPoints);
        myMap.addPolyline(polylineOptions);
    }


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

    }

    public ControlTower getControlTower(){
        return controlTower;
    }

    @Override
    public void onTowerConnected() {
        //on enregistre le drone dans la tower

    }

    public List<LatLng> getArrayPoints(){
        return arrayPoints;
    }

    @Override
    public void onTowerDisconnected() {

    }
}
