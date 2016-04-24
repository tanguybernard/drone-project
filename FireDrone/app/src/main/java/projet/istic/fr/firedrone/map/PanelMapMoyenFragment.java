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
    private ListView listViewMoyenAPlacer;

    private ArrayAdapter adapter;
    private ArrayAdapter adapterAdd;

    private List<MeansItem> listMoyens;
    private List<MeansItem> listMoyensNonPlacer;

    private MeansItem itemSelected;

    private AdapterView<?> adapterViewDefault;
    private AdapterView<?> adapterViewAdd;

    private View layoutDemande;

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

        layoutDemande = view.findViewById(R.id.layout_moyen_already_add);

        listViewMoyenAPlacer = (ListView) view.findViewById(R.id.panel_moyen_already_add);
        listViewMoyen = (ListView) view.findViewById(R.id.panel_moyen_to_add);

        //BOUCHON
        MeansItem moyenItem = new MeansItem();
        moyenItem.setMsMeanCode("TEST");
        MeansItem moyenItem2 = new MeansItem();
        moyenItem2.setMsMeanId("1");
        moyenItem2.setMsMeanCode("VLC");

        listMoyens = new ArrayList<>( Arrays.asList(moyenItem, moyenItem2));
        listMoyensNonPlacer = new ArrayList<>( Arrays.asList(moyenItem, moyenItem2));

        refreshLayoutDemdande();

        if(listMoyensNonPlacer != null && listMoyensNonPlacer.size() > 0){
            adapterAdd = new MoyenMapPanelListAdapter(getContext(),listMoyensNonPlacer);

            listViewMoyenAPlacer.setAdapter(adapterAdd);

            listViewMoyenAPlacer.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapterViewAdd = parent;
                            if(itemSelected != null && itemSelected == listMoyensNonPlacer.get(position)) {
                                view.setBackgroundColor(Color.TRANSPARENT);
                                itemSelected = null;
                            }else {
                                removeSelectionListView();
                                view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                                itemSelected = listMoyensNonPlacer.get(position);
                            }
                            MapMoyenFragment.getInstance().setMoyenItemAddSelected(itemSelected);
                        }
                    }
            );
        }
        adapter= new MoyenMapPanelListAdapter(getContext(),listMoyens);

        listViewMoyen.setAdapter(adapter);

        listViewMoyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterViewDefault = parent;
                if(itemSelected != null && itemSelected == listMoyens.get(position)) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    itemSelected = null;
                }else {
                    removeSelectionListView();
                    view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                    itemSelected = listMoyens.get(position);
                }
                MapMoyenFragment.getInstance().setMoyenItemSelected(itemSelected);
            }
        });

        return view;
    }

    //cette méthode supprime un moyen de la liste des moyen demandé et met à jour le panel
    public void removeItem(MeansItem moyenItemSelected){
        listMoyensNonPlacer.remove(moyenItemSelected);
        adapterAdd.notifyDataSetChanged();
        //rend le layout demandé visible ou invisible
        refreshLayoutDemdande();
        //supprime la sélection
        removeSelectionListView();
    }

    private void refreshLayoutDemdande(){
        if(listMoyensNonPlacer != null && listMoyensNonPlacer.size() > 0){
            layoutDemande.setVisibility(View.VISIBLE);
        }else{
            layoutDemande.setVisibility(View.INVISIBLE);
        }
    }


    private void removeSelectionListView(){
        if(adapterViewAdd  != null) {
            for (int j = 0; j < adapterViewAdd.getChildCount(); j++)
                adapterViewAdd.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }

        if(adapterViewDefault != null){
            for (int j = 0; j < adapterViewDefault.getChildCount(); j++)
                adapterViewDefault.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }
    }

}

