package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;

/**
 * Created by ramage on 20/04/16.
 */
public class PanelMapMoyenFragment extends Fragment {

    private transient MapMoyenFragment mapMoyenFragment;
    private transient PanelListFragment panelListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_panel_moyen, container, false);
        if(!InterventionSingleton.getInstance().getIntervention().getCos().getId().equals(UserSingleton.getInstance().getUser().getId())){
            view.findViewById(R.id.panel_map_moyen).setVisibility(View.INVISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        mapMoyenFragment = new MapMoyenFragment();
        panelListFragment = new PanelListFragment();

        Bundle argsMap = new Bundle();
        argsMap.putSerializable("panel", panelListFragment);
        mapMoyenFragment.setArguments(argsMap);

        Bundle argsPanel = new Bundle();
        argsPanel.putSerializable("map", mapMoyenFragment);
        panelListFragment.setArguments(argsPanel);

        getChildFragmentManager().beginTransaction().replace(R.id.content_map_moyen, mapMoyenFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.panel_map_moyen,panelListFragment ).commit();

        return view;
    }
}

