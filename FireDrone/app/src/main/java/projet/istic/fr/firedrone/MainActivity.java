package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.interfaces.TowerListener;

import java.util.List;

import projet.istic.fr.firedrone.listener.DroneListenerEvent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TowerListener{

    //tower pour se connecter au drone et recevoir les évènements du drone
    private ControlTower controlTower;

    private DrawerLayout myDrawer;

    //listener qui va écouter tout les évènements envoyés par le drone
    private DroneListenerEvent droneListenerEvent;

    private FragmentDrawPath fragmentDrawPath;
    private FragmentFiche fragmentFiche;

    //fragment pour contrôler le drône
    private FragmentControle droneControlFragment;

    @Override
    protected void onStart() {
        super.onStart();
        this.controlTower.connect(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (droneControlFragment.getDrone().isConnected()) {
            droneControlFragment.getDrone().disconnect();
        }
        //on supprime le drône de la tower
        controlTower.unregisterDrone(droneControlFragment.getDrone());
        //on se déconnecte de la tower
        controlTower.disconnect();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instanciation du fragment de contrôle du drône
        droneControlFragment = FragmentControle.getInstance();
        //on crée le drône içi
        droneControlFragment.setDrone( new Drone(getApplicationContext()));;
        //création du listener qui écoute le drône
        droneListenerEvent = new DroneListenerEvent(droneControlFragment);

        //instanciation du contrôle tower
        this.controlTower = new ControlTower(getApplicationContext());

        fragmentDrawPath = FragmentDrawPath.getInstance();

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
        //booléen indiquant si on va dans le panel du contrôle du drône
        boolean usingControlDrone = false;
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_fiche:
                fragmentFiche = FragmentFiche.getInstance();
                fragment = fragmentFiche;
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
                fragmentDrawPath = FragmentDrawPath.getInstance();

                //fragmentDrawPath.getMapAsync(this);
                fragment = fragmentDrawPath;
                break;
            case R.id.nav_controle:
                droneControlFragment = FragmentControle.getInstance();
                usingControlDrone = true;
                fragment= droneControlFragment;
                break;
            case R.id.nav_image:
                break;
        }
        //on remplace ici l'ancien fragment
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("detailFragment").commit();
        }
        droneListenerEvent.setUsingControlPanel(usingControlDrone);
        myDrawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        droneListenerEvent.setUsingControlPanel(false);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    public void setDroneMoveListener(DroneListenerEvent.DroneActionMapListener droneMoveListener){
        if(droneListenerEvent != null) {
            droneListenerEvent.setDroneMoveListener(droneMoveListener);
        }
    }


    public ControlTower getControlTower(){
        return controlTower;
    }

    @Override
    public void onTowerConnected() {
        //quand la tower est connecté, on enregistre le drône
        controlTower.registerDrone(droneControlFragment.getDrone(), droneControlFragment.getHandler());
    }

    public List<LatLng> getArrayPointsForMission(){
        if(fragmentDrawPath == null){
            return null;
        }
        return fragmentDrawPath.getArrayPoints();
    }

    @Override
    public void onTowerDisconnected() {

    }
}
