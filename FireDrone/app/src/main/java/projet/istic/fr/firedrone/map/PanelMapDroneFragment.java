package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;

/**
 * Created by mamadian on 24/05/16.
 */
public class PanelMapDroneFragment extends Fragment {

    private transient ControlDroneFragmentFragment controlDroneFragment;
    private transient MapDroneFragment mapDroneFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_panel_drone, container, false);

        mapDroneFragment = new MapDroneFragment();
        controlDroneFragment = new ControlDroneFragmentFragment();

        Bundle argsMap = new Bundle();
        argsMap.putSerializable("panel", controlDroneFragment);
        mapDroneFragment.setArguments(argsMap);

        Bundle argsPanel = new Bundle();
        argsPanel.putSerializable("map", mapDroneFragment);
        controlDroneFragment.setArguments(argsPanel);

        //**  Add the MAP Fragment  **//
        getChildFragmentManager().beginTransaction().replace(R.id.content_map_drone, mapDroneFragment).commit();
        //**  Non-SIT users don't see the Drone Control Fragment  **//


        if( UserSingleton.getInstance().getUser().isSIT()) {
            getChildFragmentManager().beginTransaction().replace(R.id.panel_map_drone, controlDroneFragment).commit();
        }
        else {
            view.findViewById(R.id.panel_map_drone).setVisibility(View.GONE);
        }

        return view;
    }
}
