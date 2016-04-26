package projet.istic.fr.firedrone.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;

/**
 * Created by ramage on 19/04/16.
 */
public class PagerAdapterMap extends FragmentPagerAdapter {

    private MapDroneFragment mapDroneFragment;

    public PagerAdapterMap(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0: mapDroneFragment = new MapDroneFragment();
                return mapDroneFragment;
            case 1: return new PanelMapMoyenFragment();
        }
        return null;

    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:return "Drône";
            case 1: return "Moyen";
        }
        return null;
    }

    public Collection<LatLng> getListPointMissionDrone() {
        return mapDroneFragment.getListMarkers();
    }
}
