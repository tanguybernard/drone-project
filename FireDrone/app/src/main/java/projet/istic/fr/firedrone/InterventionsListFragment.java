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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import projet.istic.fr.firedrone.synchro.PushReceiver;
import projet.istic.fr.firedrone.synchro.MyObservable;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nduquesne on 18/03/16.
 */
public class InterventionsListFragment extends Fragment implements Observateur {

    private static InterventionsListFragment INSTANCE;

    View view = null;

    public static InterventionsListFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new InterventionsListFragment();
        }
        return INSTANCE;
    }
    List<Intervention> listIntervention;
    ListView lv1 = null;
    ArrayAdapter<Intervention> listAdapter;
    //Map<String, Intervention> mapIntervention;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){

        MyObservable p = MyObservable.getInstance();
        p.ajouterObservateur(this);
        
        view = inflater.inflate(R.layout.intervention_main, container, false);

        //Création de la liste et affichage dans la listview
        listIntervention = getListData(view);

        lv1 = (ListView) view.findViewById(R.id.interventionList);

        listAdapter = new CustomListAdapter(this.getContext(), listIntervention);

        lv1.setAdapter(listAdapter);

        //Un clic sur une intervention de la liste permet de la sélectionner
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Intervention newsData = (Intervention) o;
                InterventionSingleton.getInstance().setIntervention(newsData);

                TabMapFragment tabMapFragment = new TabMapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, tabMapFragment).commit();
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

            View layoutId = view.findViewById(R.id.interventionMapAddress);

            if (layoutId.getVisibility() == View.VISIBLE) {
                // Its visible
                FragmentTransaction transactionMap = getFragmentManager().beginTransaction();
                transactionMap.replace(R.id.interventionMapAddress, mapInterventionFragment).commit();
            } else {
                // Either gone or invisible

            }



        }
        //Si pas de connexion wifi
        else {
            TextView notConnected = new TextView(getActivity());
            notConnected.setText("Vous n'avez pas de connexion internet, la map ne peut pas s'afficher");

            frame.addView(notConnected);
        }
    }

    //Méthode de focus sur l'item de la liste quand on clique sur un marker de la map
    public void UpdateList(String idInter) {

        int positionList = 0;

        for (int i = 0; i < listIntervention.size(); i++){
            if (idInter.equals(listIntervention.get(i).getId())){
                positionList = i;
            }
        }

        if(lv1 != null){
            lv1.setSelection(positionList);
            lv1.requestFocus();
        }
    }


    //Méthode de vérification de la connexion wifi
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void actualiser(Observable o) {

        System.out.println("TEST synchro");
        if(o instanceof MyObservable){


            Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);


            InterventionsListFragment myFragment = (InterventionsListFragment)getFragmentManager().findFragmentByTag("InterventionsListFragment");
            if (myFragment != null && myFragment.isVisible()) {

                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.content_frame, this);
                tr.commit();
            }






        }


    }
}
