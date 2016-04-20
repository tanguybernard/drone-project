package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;

import projet.istic.fr.firedrone.R;

/**
 * Created by ramage on 19/04/16.
 */
public class TabMapFragment extends Fragment {


    //Instance
    private static TabMapFragment INSTANCE;
    private PagerAdapterMap pagerAdapter;

    //singleton, une seule instance du fragment controle
    public static TabMapFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TabMapFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_map, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        pagerAdapter = new PagerAdapterMap(getChildFragmentManager());
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    public Collection<LatLng> getListPointForMissionDrone(){
        return pagerAdapter.getListPointMissionDrone();
    }

}
