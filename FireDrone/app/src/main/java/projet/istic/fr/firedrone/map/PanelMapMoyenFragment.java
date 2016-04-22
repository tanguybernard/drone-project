package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.adapter.MoyenMapPanelListAdapter;
import projet.istic.fr.firedrone.model.MeansItem;

/**
 * Created by ramage on 20/04/16.
 */
public class PanelMapMoyenFragment extends Fragment {

    private ListView listViewMoyen;
    private ArrayAdapter adapter;
    private List<MeansItem> listMoyens;

    private static PanelMapMoyenFragment INSTANCE;

    public static PanelMapMoyenFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PanelMapMoyenFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_panel_moyen, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.content_map_moyen, MapMoyenFragment.getInstance()).commit();

        listViewMoyen = (ListView) view.findViewById(R.id.panel_moyen_to_add);

        //BOUCHON
        MeansItem moyenItem = new MeansItem();
        moyenItem.setMsMeanCode("TEST");
        MeansItem moyenItem2 = new MeansItem();
        moyenItem2.setMlMeanId(1);
        moyenItem2.setMsMeanCode("VLC");

        listMoyens = new ArrayList<>( Arrays.asList(moyenItem, moyenItem2));

        adapter= new MoyenMapPanelListAdapter(getContext(),listMoyens);

        listViewMoyen.setAdapter(adapter);
        listViewMoyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                removeSelectionListView(parent);
                view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                MapMoyenFragment.getInstance().setMoyenItemSelected(listMoyens.get(position));
            }
        });

        return view;
    }


    private void removeSelectionListView(AdapterView<?> parent){
        for (int j = 0; j < parent.getChildCount(); j++)
            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
    }

}

