package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.synchro.MyObservable;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;

/**
 * Created by ramage on 19/04/16.
 */
public class TabMapFragment extends Fragment implements Observateur {

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

        MyObservable p = MyObservable.getInstance();
        p.ajouterObservateur(this);

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

    @Override
    public void actualiser(Observable o) {

        if(o instanceof MyObservable){
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.content_frame, this);
                tr.commit();
        }
    }
}
