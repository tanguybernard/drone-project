package projet.istic.fr.firedrone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.o3dr.android.client.ControlTower;
import com.o3dr.android.client.Drone;
import com.o3dr.android.client.interfaces.TowerListener;

import java.net.URL;
import java.util.Collection;

import me.pushy.sdk.Pushy;
import projet.istic.fr.firedrone.listener.DroneListenerEvent;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;

public class MainActivity extends AppCompatActivity
        implements TowerListener, Observable{

    //tower pour se connecter au drone et recevoir les évènements du drone
    private ControlTower controlTower;

    private DrawerLayout myDrawer;

    //listener qui va écouter tout les évènements envoyés par le drone
    private DroneListenerEvent droneListenerEvent;

    private TabMapFragment fragmentDrawPath;
    private InterventionsListFragment fragmentFiche;
    private DetailsInterventionFragment detailsFragment;

    private MoyenFragment fragmentMoyen;

    //fragment pour contrôler le drône
    private PanelControleDroneFragment droneControlFragment;

    @Override
    protected void onStart() {
        super.onStart();
        this.controlTower.connect(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( ControleFragment.getInstance().getDrone().isConnected()) {
            ControleFragment.getInstance().getDrone().disconnect();
        }
        //on supprime le drône de la tower
        controlTower.unregisterDrone(ControleFragment.getInstance().getDrone());
        //on se déconnecte de la tower
        controlTower.disconnect();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);


        new registerForPushNotificationsAsync().execute();


        MeansItemService.createListDefaultWay(getApplicationContext());

        fragmentFiche = InterventionsListFragment.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentFiche).addToBackStack("detailFragment").commit();



        //instanciation du fragment de contrôle du drône
        droneControlFragment = PanelControleDroneFragment.getInstance();
        //on crée le drône içi
        ControleFragment.getInstance().setDrone(new Drone(getApplicationContext()));;
        //création du listener qui écoute le drône
        droneListenerEvent = new DroneListenerEvent( ControleFragment.getInstance());
        //fragmentMoyen = MoyenFragment.getInstance();

        //instanciation du contrôle tower
        this.controlTower = new ControlTower(getApplicationContext());

        fragmentDrawPath = TabMapFragment.getInstance();

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
            case R.id.nav_liste:
                fragmentFiche = InterventionsListFragment.getInstance();
                fragment = fragmentFiche;
                break;
            case R.id.nav_details:
                detailsFragment = DetailsInterventionFragment.getInstance();
                fragment = detailsFragment;
                break;
            case R.id.nav_moyen:
                fragmentMoyen = MoyenFragment.getInstance();
                Log.d("TAG", "selectDrawerItem: ");
                fragment = fragmentMoyen;
                break;
            case R.id.nav_directive:
                break;
            case R.id.nav_parcours:
                fragmentDrawPath = TabMapFragment.getInstance();

                fragment = fragmentDrawPath;
                break;
            case R.id.nav_controle:
                droneControlFragment = PanelControleDroneFragment.getInstance();
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
        controlTower.registerDrone(ControleFragment.getInstance().getDrone(), ControleFragment.getInstance().getHandler());
    }

    public Collection<LatLng> getArrayPointsForMission(){
        if(fragmentDrawPath == null){
            return null;
        }
        return droneControlFragment.getMapDrone().getListMarkers();
    }

    @Override
    public void onTowerDisconnected() {

    }

    //------------------------------------- OBSERVABLE PART ------------------------------------------------

    @Override
    public void ajouterObservateur(Observateur o) {

    }

    @Override
    public void supprimerObservateur(Observateur o) {

    }

    @Override
    public void notifierObservateurs() {

    }

    //--------------------------------------------PUSHY----------------------------------------------------


    private class registerForPushNotificationsAsync extends AsyncTask<Void, Void, Exception>
    {
        protected Exception doInBackground(Void... params)
        {
            try
            {
                // Acquire a unique registration ID for this device
                String registrationId = Pushy.register(getApplicationContext());

                // Send the registration ID to your backend server and store it for later
                sendRegistrationIdToBackendServer(registrationId);
            }
            catch( Exception exc )
            {
                // Return exc to onPostExecute
                return exc;
            }

            // We're good
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc)
        {
            // Failed?
            if ( exc != null )
            {
                // Show error as toast message
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // Succeeded, do something to alert the user
        }

        // Example implementation
        void sendRegistrationIdToBackendServer(String registrationId) throws Exception
        {
            // The URL to the function in your backend API that stores registration IDs
            URL sendRegIdRequest = new URL("https://{YOUR_API_HOSTNAME}/register/device?registration_id=" + registrationId);

            // Send the registration ID by executing the GET request
            sendRegIdRequest.openConnection();
        }
    }


}
