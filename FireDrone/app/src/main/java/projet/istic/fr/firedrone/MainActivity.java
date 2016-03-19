package projet.istic.fr.firedrone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback {

    private DrawerLayout myDrawer;
    private GoogleMap myMap;
    SupportMapFragment supportMapFragment;

    private ArrayList<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;

    private LatLng rennes_istic = new LatLng(48.1154538, -1.6387933);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayPoints = new ArrayList<LatLng>();
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);


        supportMapFragment = new SupportMapFragment();
        supportMapFragment.getMapAsync(this);


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

        Log.v("MainActivity", "selectDrawItem(MenuItem item)");
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;

        System.out.println(item.getTitle());

        System.out.println(R.id.nav_parcours == item.getItemId());




                switch (item.getItemId()) {
                    case R.id.nav_fiche:
                        fragmentClass = FragmentFiche.class;
                        break;
                    case R.id.nav_moyen:
                        fragmentClass = FragmentMoyen.class;
                        break;
                    case R.id.nav_rapport:
                        fragmentClass = FragmentRapport.class;
                        break;
                    case R.id.nav_directive:
                        fragmentClass = FragmentDirective.class;
                        break;
                    case R.id.nav_plan:
                        fragmentClass = FragmentPlanning.class;
                        break;
                    case R.id.nav_parcours:
                        System.out.println("IAM in nav_parcours");
                        fragmentClass = null;

                        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, supportMapFragment).addToBackStack("detailFragment").commit();


                        break;
                    case R.id.nav_controle:
                        fragmentClass = FragmentControle.class;
                        break;
                    case R.id.nav_image:
                        fragmentClass = FragmentImage.class;
                        break;
                    default:
                        fragmentClass = FragmentFiche.class;
                }

                try {
                    if(item.getItemId()!=R.id.nav_parcours){
                        fragment = (Fragment) fragmentClass.newInstance();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

        if(item.getItemId()!=R.id.nav_parcours) {

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();

        }

                // Highlight the selected item, update the title, and close the drawer
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());

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
        System.out.println("onCameraChange");

    }
}
