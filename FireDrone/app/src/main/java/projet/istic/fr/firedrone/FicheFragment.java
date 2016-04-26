package projet.istic.fr.firedrone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nduquesne on 18/03/16.
 */
public class FicheFragment extends Fragment {

    private static FicheFragment INSTANCE;

    View view = null;

    public static FicheFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new FicheFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){
        
        view = inflater.inflate(R.layout.intervention_main, container, false);

        //Création de la liste et affichage dans la listview
        List<Intervention> image_details = getListData(view);

        final ListView lv1 = (ListView) view.findViewById(R.id.interventionList);

        lv1.setAdapter(new CustomListAdapter(this.getContext(), image_details));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Intervention newsData = (Intervention) o;
                InterventionSingleton.getInstance().setIntervention(newsData);

                DetailsInterventionFragment detailsInterventionFragment = new DetailsInterventionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, detailsInterventionFragment).commit();

            }
        });

        final Button btnAddIntervention = (Button) view.findViewById(R.id.btnAddIntervention);

        btnAddIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateInterventionFragment createInterventionFragment = new CreateInterventionFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, createInterventionFragment).commit();

            }
        });

        return view;
    }

    /**
     *
     * @return list data of in progress interventions
     */
    private List<Intervention> getListData(final View view) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

        final List<Intervention> results = new ArrayList<Intervention>();

        interventionAPI.getIntervention("IN_PROGRESS", new Callback<List<Intervention>>() {

            @Override
            public void success(List<Intervention> interventions, Response response) {
                results.addAll(interventions);
                //Si success, appel de méthode de génération de la map avec la liste des interventions
                GenerateMap(results, view);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return results;
    }

    //Méthode de génération de la google map à la place du FrameLayout de la liste d'intervention
    public void GenerateMap(List<Intervention> listInter, View view){

        FrameLayout frame = (FrameLayout) view.findViewById(R.id.interventionMapAddress);

        //Test de la connexion de la tablette au réseau
        if (isOnline() == true) {
            //Replace de la frame par google map
            MapInterventionFragment mapInterventionFragment = new MapInterventionFragment();

            //On envoi la liste des interventions que l'on a récupérée de la base
            mapInterventionFragment.setListInter(listInter);

            FragmentTransaction transactionMap = getFragmentManager().beginTransaction();
            transactionMap.replace(R.id.interventionMapAddress, mapInterventionFragment).commit();
        }
        //Si pas de connexion wifi
        else {
            TextView notConnected = new TextView(getActivity());
            notConnected.setText("Vous n'avez pas de connexion internet, la map ne peut pas s'afficher");

            frame.addView(notConnected);
        }
    }



    //Méthode de vérification de la connexion wifi
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
