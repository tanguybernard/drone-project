package projet.istic.fr.firedrone.map;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by ramage on 19/04/16.
 */
public class PagerAdapterMap extends FragmentPagerAdapter {

    private PanelMapDroneFragment mapDroneFragment;

    public PagerAdapterMap(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 1: mapDroneFragment = new PanelMapDroneFragment();
                return mapDroneFragment;
            case 0: return new PanelMapMoyenFragment();
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
            case 1:return "Drône";
            case 0: return "Moyen";
        }
        return null;
    }

    public Collection<LatLng> getListPointMissionDrone() {
        return new Collection<LatLng>() {
            @Override
            public boolean add(LatLng object) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends LatLng> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<LatLng> iterator() {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };
        //return mapDroneFragment.getListMarkers();
    }
}
