package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projet.istic.fr.firedrone.R;

/**
 * Created by mamadian on 24/05/16.
 */
public class PanelMapDroneFragment extends Fragment {

    private transient PanelListDroneFragment panelListDroneFragment;
    private transient MapDroneFragment mapDroneFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_panel_drone, container, false);

        mapDroneFragment = new MapDroneFragment();
        panelListDroneFragment = new PanelListDroneFragment();

        Bundle argsMap = new Bundle();
        argsMap.putSerializable("panel", panelListDroneFragment);
        mapDroneFragment.setArguments(argsMap);

        Bundle argsPanel = new Bundle();
        argsPanel.putSerializable("map", mapDroneFragment);
        panelListDroneFragment.setArguments(argsPanel);

        getChildFragmentManager().beginTransaction().replace(R.id.content_map_drone, mapDroneFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.panel_map_drone,panelListDroneFragment ).commit();

        return view;
    }
}
