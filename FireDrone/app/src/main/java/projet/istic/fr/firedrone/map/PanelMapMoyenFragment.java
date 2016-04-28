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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.ModelAPI.SIGAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.adapter.MoyenMapPanelListAdapter;
import projet.istic.fr.firedrone.adapter.PointListAdapter;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ramage on 20/04/16.
 */
public class PanelMapMoyenFragment extends Fragment implements Serializable {

    private transient ListView listViewMoyen;
    private transient ListView listViewMoyenAPlacer;
    private transient ListView listViewPoint;

    private transient ArrayAdapter adapter;
    private transient ArrayAdapter adapterAdd;
    private transient PointListAdapter pointListAdapter;

    private transient List<MeansItem> listMoyens;
    private transient List<MeansItem> listMoyensNonPlacer = new ArrayList<>();

    private transient MeansItem itemSelected;
    private transient EnumPointType pointTypeSelected;

    private transient AdapterView<?> adapterViewDefault;
    private transient AdapterView<?> adapterViewAdd;
    private transient AdapterView<?> adapterViewPoint;

    private transient MapMoyenFragment mapMoyenFragment;

    private transient View layoutDemande;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_panel_moyen, container, false);
        if(UserSingleton.getInstance().getUser().getRole().equals("ROLE_SIT")){
            view.findViewById(R.id.panel_map_moyen).setVisibility(View.INVISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        mapMoyenFragment = new MapMoyenFragment();

        Bundle args = new Bundle();
        args.putSerializable("panel", this);
        mapMoyenFragment.setArguments(args);

        getChildFragmentManager().beginTransaction().replace(R.id.content_map_moyen, mapMoyenFragment).commit();

        layoutDemande = view.findViewById(R.id.layout_moyen_already_add);

        listViewMoyenAPlacer = (ListView) view.findViewById(R.id.panel_moyen_already_add);
        listViewMoyen = (ListView) view.findViewById(R.id.panel_moyen_to_add);
        listViewPoint = (ListView) view.findViewById(R.id.panel_point_to_add);


        //BOUCHON
        Intervention intervention = InterventionSingleton.getInstance().getIntervention();
        listMoyensNonPlacer.clear();
        if(adapterAdd != null) {
            adapterAdd.notifyDataSetChanged();
        }

        listMoyens = MeansItemService.getListDefaultMeansItem();
        //parcours de toutes les moyens de l'intervention pour trouver ceux qui ne sont pas encore placés et pas encore libéré
        if(intervention.getWays() != null) {
            for (MeansItem moyenInter : intervention.getWays()) {
                if ((moyenInter.getMsLongitude() == null || moyenInter.getMsLatitude() == null ) && moyenInter.getMsMeanHFree() == null) {
                    listMoyensNonPlacer.add(moyenInter);
                }
            }
        }

        refreshLayoutDemdande();

        if(listMoyensNonPlacer != null && listMoyensNonPlacer.size() > 0){
            adapterAdd = new MoyenMapPanelListAdapter(getContext(),listMoyensNonPlacer);

            listViewMoyenAPlacer.setAdapter(adapterAdd);

            listViewMoyenAPlacer.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            pointTypeSelected = null;
                            adapterViewAdd = parent;
                            if(itemSelected != null && itemSelected == listMoyensNonPlacer.get(position)) {
                                view.setBackgroundColor(Color.TRANSPARENT);
                                itemSelected = null;
                            }else {
                                removeSelectionListView();
                                view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                                itemSelected = listMoyensNonPlacer.get(position);
                            }
                            mapMoyenFragment.setMoyenItemAddSelected(itemSelected);
                        }
                    }
            );
        }
        adapter= new MoyenMapPanelListAdapter(getContext(),listMoyens);

        listViewMoyen.setAdapter(adapter);

        listViewMoyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pointTypeSelected=null;
                adapterViewDefault = parent;
                if (itemSelected != null && itemSelected == listMoyens.get(position)) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    itemSelected = null;
                } else {
                    removeSelectionListView();
                    view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                    itemSelected = listMoyens.get(position);
                }
                mapMoyenFragment.setMoyenItemSelected(itemSelected);
            }
        });

        pointListAdapter = new PointListAdapter(getContext());
        listViewPoint.setAdapter(pointListAdapter);
        listViewPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = null;
                adapterViewPoint = parent;
                if(pointTypeSelected != null && pointTypeSelected == pointListAdapter.getItem(position)) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    pointTypeSelected = null;
                }else{
                    removeSelectionListView();
                    view.setBackgroundColor(getResources().getColor(R.color.lightblue));
                    pointTypeSelected = (EnumPointType) pointListAdapter.getItem(position);
                }
                mapMoyenFragment.setPointAddSelected(pointTypeSelected);
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
            layoutDemande.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,6));
        }else{
            layoutDemande.setVisibility(View.INVISIBLE);
            layoutDemande.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

        if(adapterViewPoint != null){
            for (int j = 0; j < adapterViewPoint.getChildCount(); j++)
                adapterViewPoint.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }
    }

}

