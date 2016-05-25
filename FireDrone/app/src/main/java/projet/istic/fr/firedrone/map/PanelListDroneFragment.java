package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.io.Serializable;

import projet.istic.fr.firedrone.R;

/**
 * Created by mamadian on 24/05/16.
 */
public class PanelListDroneFragment extends Fragment implements AbsListView.OnScrollListener,Serializable {


    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){
        final View view = inflater.inflate(R.layout.fragment_list_panel_drone, container, false);
        return  view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
