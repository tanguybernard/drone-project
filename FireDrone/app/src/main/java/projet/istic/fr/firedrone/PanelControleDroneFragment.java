package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projet.istic.fr.firedrone.map.MapDroneFragment;

/**
 * Created by ramage on 26/04/16.
 */
public class PanelControleDroneFragment extends Fragment {

    //Instance
    private static PanelControleDroneFragment INSTANCE;

    private MapDroneFragment mapDrone;

    //singleton, une seule instance du fragment controle
    public static PanelControleDroneFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PanelControleDroneFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_controle_drone, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.controle_map,ControleFragment.getInstance()).addToBackStack("controle").commit();
        mapDrone = new MapDroneFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.content_map, mapDrone).addToBackStack("mapDrone").commit();
        return view;
    }

    public MapDroneFragment getMapDrone(){
        return mapDrone;
    }
}
