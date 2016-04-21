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

import org.json.JSONArray;
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

        final ArrayList<InterventionItem> results = new ArrayList<InterventionItem>();

        interventionAPI.getIntervention("IN_PROGRESS", new Callback<List<Intervention>>() {

            @Override
            public void success(List<Intervention> interventions, Response response) {


                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());

                //[{"id":"1","sinisterCode":null,"date":"20/03/2016","address":"31 avenue du Professeur Charles Foulon","latitude":"42","longitude":"42","status":"IN_PROGRESS","ways":null},{"id":"571889ecb7604711a85a6b1b","sinisterCode":null,"date":"20160421_100433","address":null,"latitude":null,"longitude":null,"status":"IN_PROGRESS","ways":null},{"id":"5718c26eb760d213a96a67b8","sinisterCode":null,"date":"21/04/2016","address":"dummy adress","latitude":null,"longitude":null,"status":"IN_PROGRESS","ways":null},{"id":"5718cc44b7601ac3c46c43a0","sinisterCode":"Malaise","date":"21/04/2016","address":"teste adress z","latitude":null,"longitude":null,"status":"IN_PROGRESS","ways":null},{"id":"5718d351b7607eb45fba6e64","sinisterCode":null,"date":"20/03/2016","address":"31 avenue du Professeur Charles Foulon","latitude":"42","longitude":"42","status":"IN_PROGRESS","ways":null}]




                JSONArray reader = null;
                try {
                    reader = new JSONArray(bodyString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                InterventionItem newsData;
                for (int i = 0; i < reader.length(); i++) {
                    try {
                        JSONObject elt = (JSONObject) reader.get(i);

                        newsData = new InterventionItem();
                        String id = (elt.get("id")!=null) ? elt.get("id").toString() : "";
                        newsData.setMyId(id);
                        String sinisterCode = (elt.get("sinisterCode")!=null) ? elt.get("sinisterCode").toString() : "";
                        newsData.setCodeSinistre(sinisterCode);
                        String address = (elt.get("address")!=null) ? elt.get("address").toString() : "";
                        newsData.setAdress(address);

                        results.add(newsData);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);

            }
        });



        return results;
    }

}
