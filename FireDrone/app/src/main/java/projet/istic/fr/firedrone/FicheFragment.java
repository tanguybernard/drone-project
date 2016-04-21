package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.InterventionItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by nduquesne on 18/03/16.
 */
public class FicheFragment extends Fragment {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";

    private static FicheFragment INSTANCE;

    public static FicheFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new FicheFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){



        /**final View view = inflater.inflate(R.layout.fragment_fiche,container,false);

        final Intervention intervention =new Intervention();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .build();
        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);
        interventionAPI.GetIntervention("56eff377b760a2df933ccd61", new Callback<Intervention>() {
            @Override
            public void success(Intervention intervention, Response response) {

                TextView id= (TextView)view.findViewById(R.id.textView);
                TextView content =(TextView)view.findViewById(R.id.textView2);
                TextView date = (TextView)view.findViewById(R.id.textView3);
                TextView type=(TextView)view.findViewById(R.id.textView4);
                TextView author =(TextView)view.findViewById(R.id.textView5);

                id.setText(intervention.id);

           }

            @Override
            public void failure(RetrofitError error) {
                Log.d("==retrofit==", error.toString());
            }
        });*/

        final View view = inflater.inflate(R.layout.intervention_main,container,false);

        //Création de la liste et affichage dans la listview
        ArrayList image_details = getListData();
        //A remplacer par un appel à la base de données pour obtenir les interventions actives

        final ListView lv1 = (ListView) view.findViewById(R.id.interventionList);

        lv1.setAdapter(new CustomListAdapter(this.getContext(), image_details));

        //Faire le replace de la frame par google map avec en attribut de méthode les adresses des interventions
        MapInterventionFragment mapInterventionFragment = new MapInterventionFragment();

        FragmentTransaction transactionMap = getFragmentManager().beginTransaction();
        transactionMap.replace(R.id.interventionMapAddress, mapInterventionFragment).commit();


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                InterventionItem newsData = (InterventionItem) o;
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
     * @return list data of an interventions
     */
    private ArrayList getListData() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);
        interventionAPI.getIntervention("IN_PROGRESS", new Callback<List<Intervention>>() {

            @Override
            public void success(List<Intervention> interventions, Response response) {


                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());


                System.out.println("RESULT");
                System.out.println(bodyString);

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);

            }
        });




        ArrayList<InterventionItem> results = new ArrayList<InterventionItem>();

        InterventionItem newsData = new InterventionItem();
        newsData.setMyId("ecf456");
        newsData.setCodeSinistre("Feu de foret");
        newsData.setAdress("Les gayeulles Rennes");
        results.add(newsData);

        newsData = new InterventionItem();
        newsData.setMyId("ecf4565");
        newsData.setCodeSinistre("Accident");
        newsData.setAdress("Lonchamps rennes");
        results.add(newsData);

        InterventionItem newsData2 = new InterventionItem();
        newsData2.setMyId("ecf45667");
        newsData2.setCodeSinistre("Feu de foret2");
        newsData2.setAdress("Les gayeulles Rennes2");
        results.add(newsData2);

        // Add some more dummy data for testing
        return results;
    }

}
