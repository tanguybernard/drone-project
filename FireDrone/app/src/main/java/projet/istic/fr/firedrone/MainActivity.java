package projet.istic.fr.firedrone;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.interfaces.TowerListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TowerListener {

    //tower pour se connecter au drone et recevoir les évènements du drone
    private ControlTower controlTower;

    //fragment du controle du drone
    private DroneControlFragment fragmentControlDrone;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialisation de la tower
        this.controlTower = new ControlTower(getApplicationContext());
        fragmentControlDrone = new DroneControlFragment();
        FragmentTransaction transaction  =  getSupportFragmentManager().beginTransaction().add(R.id.droneControle,fragmentControlDrone);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

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
    }

    public ControlTower getControlTower(){
        return controlTower;
    }

    @Override
    public void onTowerConnected() {
        //on enregistre le drone dans la tower
        controlTower.registerDrone(fragmentControlDrone.getDrone(), fragmentControlDrone.getHandler());
        fragmentControlDrone.registerDroneListener();
    }

    @Override
    public void onTowerDisconnected() {

    }
}
