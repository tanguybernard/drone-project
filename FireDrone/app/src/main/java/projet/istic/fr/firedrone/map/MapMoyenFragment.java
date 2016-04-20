package projet.istic.fr.firedrone.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

import projet.istic.fr.firedrone.R;

/**
 * Created by ramage on 20/04/16.
 */
public class MapMoyenFragment extends Fragment {

    private ListView listMoyen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_moyen, container, false);
        getChildFragmentManager().beginTransaction().add(R.id.content_map_moyen,new SupportMapFragment()).commit();
        listMoyen = (ListView) view.findViewById(R.id.panel_moyen);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.drone_36_36);


        return view;
    }


}
