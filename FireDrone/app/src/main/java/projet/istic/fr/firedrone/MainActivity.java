package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private DrawerLayout myDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView nvDrawer = (NavigationView) findViewById(R.id.drawer_layout);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
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
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;

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
                fragmentClass = FragmentParcours.class;
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
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);
        setTitle(item.getTitle());
        myDrawer.closeDrawers();
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
}
