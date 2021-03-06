package projet.istic.fr.firedrone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;
import java.util.Collection;

import me.pushy.sdk.Pushy;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import projet.istic.fr.firedrone.synchro.MyObservable;
import projet.istic.fr.firedrone.synchro.Observateur;

public class MainActivity extends AppCompatActivity
        implements VisibilityMenu{

    private DrawerLayout myDrawer;

    private TabMapFragment fragmentDrawPath;
    private InterventionsListFragment fragmentFiche;
    private DetailsInterventionFragment detailsFragment;
    private MoyenRequestFragment moyenReqFragment;
    private MoyenFragment fragmentMoyen;
    private ImageFragment imageFragment;
    private   NavigationView navigationView;

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushy.listen(this);

        setContentView(R.layout.activity_main);

        new registerForPushNotificationsAsync().execute();

        MeansItemService.createListDefaultWay(getApplicationContext());

        fragmentFiche = InterventionsListFragment.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentFiche, "InterventionsListFragment").addToBackStack("detailFragment").commit();

        //fragmentMoyen = MoyenFragment.getInstance();

        fragmentDrawPath = TabMapFragment.getInstance();

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        //dont show navigation until choice an intervention
        navigationView.getMenu().setGroupVisible(R.id.group_1, false);
        navigationView.getMenu().setGroupVisible(R.id.group_2, false);
        navigationView.getMenu().setGroupVisible(R.id.group_3, false);

        if(!UserSingleton.getInstance().getUser().getRole().equals(FiredroneConstante.ROLE_CODIS)){
            navigationView.getMenu().findItem(R.id.nav_moyen_req).setEnabled(false);
        }
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
            case R.id.nav_parcours:
                fragmentDrawPath = TabMapFragment.getInstance();
                fragment = fragmentDrawPath;
                break;
            case R.id.nav_moyen_req:
                moyenReqFragment = MoyenRequestFragment.getInstance();
                fragment= moyenReqFragment;
                break;
            case R.id.nav_image:
                imageFragment = ImageFragment.getInstance();
                fragment= imageFragment;
                break;
            case R.id.nav_logout:
                System.out.println("nav_logout");
                goBackToLogin();
                fragment=null;
                break;
        }
        //on remplace ici l'ancien fragment
        if(fragment != null){
            MyObservable.getInstance().setFragment((Observateur) fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("detailFragment").commit();
        }
        myDrawer.closeDrawers();
    }


    /**
     * Logout properly
     */
    public void goBackToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }

    @Override
    public void showMenu() {
        navigationView.setVisibility(View.VISIBLE);
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
