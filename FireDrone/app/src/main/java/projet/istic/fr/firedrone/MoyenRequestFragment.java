package projet.istic.fr.firedrone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.adapter.MoyenReqListAdapter;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import projet.istic.fr.firedrone.synchro.PushReceiver;
import projet.istic.fr.firedrone.synchro.MyObservable;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MoyenRequestFragment extends Fragment implements Observateur {

    private static MoyenRequestFragment INSTANCE;

    View view = null;

    public static MoyenRequestFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new MoyenRequestFragment();
        }
        return INSTANCE;
    }

    List<MeansItem> listMoyen;
    ListView lv1 = null;
    ArrayAdapter<MeansItem> listAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){

        MyObservable p = MyObservable.getInstance();
        p.ajouterObservateur(this);

        view = inflater.inflate(R.layout.moyen_list, container, false);


        getListData();

        return view;
    }

    private List<MeansItem> getListData() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MeansAPI meansAPI = restAdapter.create(MeansAPI.class);

        final List<MeansItem> results = new ArrayList<MeansItem>();

        String idInter = InterventionSingleton.getInstance().getIntervention().getId();
        meansAPI.getWayRequested(idInter,"D", new Callback<List<MeansItem>>() {

            @Override
            public void success(List<MeansItem> meansItems, Response response) {
                System.out.println("YOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                results.addAll(meansItems);

                lv1 = (ListView) view.findViewById(R.id.moyenListView2);

                listAdapter = new MoyenReqListAdapter(getContext(), results);

                lv1.setAdapter(listAdapter);
                System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");

            }

            @Override
            public void failure(RetrofitError error) {
                FiredroneConstante.getToastError(getContext()).show();
            }
        });


        return results;
    }




    @Override
    public void actualiser(Observable o) {

        System.out.println("LOLIIDFSDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");

        if(o instanceof MyObservable){

            MoyenRequestFragment myFragment = (MoyenRequestFragment) getFragmentManager().findFragmentById(R.id.content_frame);
            if (myFragment != null && myFragment.isVisible()) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }

        }


    }
}