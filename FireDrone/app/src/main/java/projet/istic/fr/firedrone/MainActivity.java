package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.interfaces.TowerListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TowerListener{

    //tower pour se connecter au drone et recevoir les évènements du drone
    private ControlTower controlTower;


    private DrawerLayout myDrawer;

    private FragmentDrawPath fragmentDrawPath;




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
                fragmentDrawPath = new FragmentDrawPath();
                //fragmentDrawPath.getMapAsync(this);
                fragment = fragmentDrawPath;
                break;
            case R.id.nav_controle:
                fragment=new DroneControlFragment();
                break;
            case R.id.nav_image:
                break;
        }
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("detailFragment").commit();
        }
        myDrawer.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }





    public ControlTower getControlTower(){
        return controlTower;
    }

    @Override
    public void onTowerConnected() {
        //on enregistre le drone dans la tower

    }

    public List<LatLng> getArrayPoints(){
        return fragmentDrawPath.getArrayPoints();
    }

    @Override
    public void onTowerDisconnected() {

    }
}
