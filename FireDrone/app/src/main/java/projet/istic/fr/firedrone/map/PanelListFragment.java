package projet.istic.fr.firedrone.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.adapter.SectionListAdapter;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;

/**
 * Created by ramage on 23/05/16.
 */
public class PanelListFragment extends ListFragment implements AbsListView.OnScrollListener,Serializable {

    private View viewSelected;
    private MapMoyenFragment mapMoyenFragment;
    private List<MeansItem> meansItemListDemande;
    private SectionListAdapter mAdapter;
    private static final String MOYEN_DEMANDE = "Moyen demandé";
    private static final String MOYEN_AJOUTABLE = "Moyen ajoutable";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mapMoyenFragment = (MapMoyenFragment) getArguments().getSerializable("map");
        super.onCreate(savedInstanceState);

        mAdapter= new SectionListAdapter(getContext());
        this.setListAdapter(mAdapter);
        Intervention intervention = InterventionSingleton.getInstance().getIntervention();

        meansItemListDemande = new ArrayList<>();
        if(intervention.getWays() != null) {
            for (MeansItem moyenInter : intervention.getWays()) {
                if ((moyenInter.getMsLongitude() == null || moyenInter.getMsLatitude() == null ) && moyenInter.getMsMeanHFree() == null) {
                    meansItemListDemande.add(moyenInter);
                }
            }

            if( meansItemListDemande.size() > 0){
                mAdapter.addSectionHeaderItem(MOYEN_DEMANDE);
                mAdapter.addItems( meansItemListDemande);
            }
        }



        mAdapter.addSectionHeaderItem(MOYEN_AJOUTABLE);
        List<MeansItem> listMoyens = MeansItemService.getListDefaultMeansItem();
        for(MeansItem meansItem : listMoyens){
            mAdapter.addItem(meansItem);
        }

        mAdapter.addSectionHeaderItem("Ajout point");
        for(EnumPointType pointTYpe: EnumPointType.values()){
            mAdapter.addItem(pointTYpe);
        }


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(!isHeader(position - l.getFirstVisiblePosition())) {
            if (viewSelected == v) {
               getListView().getChildAt(position - l.getFirstVisiblePosition()).setBackgroundColor(Color.TRANSPARENT);
                viewSelected = null;
            } else {
                removeSelectionListView();
                viewSelected = v;
                getListView().getChildAt(position- l.getFirstVisiblePosition()).setBackgroundColor(getResources().getColor(R.color.lightblue));
            }


            mapMoyenFragment.setItemSelected(mAdapter.getItem(position), position<mAdapter.getPosition(MOYEN_AJOUTABLE));
        }
    }

    public void removeItem(MeansItem meansItem){
        mAdapter.removeItem(meansItem);
        if(meansItemListDemande.size() == 0){
            mAdapter.removeSectionHeaderItem(MOYEN_DEMANDE, 0);
        }
        mAdapter.notifyDataSetChanged();
    }

    //méthode qui supprime toute les sélections dans la liste
    private void removeSelectionListView(){
       for(int i = getListView().getFirstVisiblePosition();i<getListView().getChildCount();i++){
           if(!isHeader(i)){
               getListView().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
           }
       }
    }



    private boolean isHeader(int position) {
        View view = ((RelativeLayout)getListView().getChildAt(position)).getChildAt(0);
        return view.getId()==R.id.imageView && view.getVisibility() == View.INVISIBLE ;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        removeSelectionListView();
    }
}
