package projet.istic.fr.firedrone;

/**
 * Created by tbernard on 19/04/16.
 */

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import projet.istic.fr.firedrone.ModelAPI.DefaultWaysSinisterApi;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.MoyenListAdapter;
import projet.istic.fr.firedrone.map.MapMoyenFragment;
import projet.istic.fr.firedrone.map.TabMapFragment;
import projet.istic.fr.firedrone.model.CoordinateItem;
import projet.istic.fr.firedrone.model.DefaultSinister;
import projet.istic.fr.firedrone.model.DefaultSinisterGroupWays;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;
import projet.istic.fr.firedrone.service.ColorNameService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class CreateInterventionFragment extends Fragment {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";
    public static final String GEO_API="http://maps.googleapis.com/maps/api/geocode/xml?address=";

    private static InterventionsListFragment INSTANCE;

    public static InterventionsListFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new InterventionsListFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState){



        final View view = inflater.inflate(R.layout.intervention_creation,container,false);

        final Button btnSaveIntervention = (Button) view.findViewById(R.id.btnSaveIntervention);

        btnSaveIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewIntervention(v);
            }
        });


        final Spinner codeSinistreList = (Spinner) view.findViewById(R.id.codeSinistreList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.code_sinistre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeSinistreList.setAdapter(adapter);

        /**
         * Select a code sinister => get default ways
         */
        codeSinistreList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Spinner spinner = (Spinner)view.findViewById(R.id.codeSinistreList);
                String text = spinner.getSelectedItem().toString();

                getListData(view,text);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                System.out.println("nothing selected");
            }

        });

        return view;
    }


    /**
     * Put a new intervention in database
     * @param view
     */
    public void sendNewIntervention(View view){

        EditText addressInter = (EditText) getView().findViewById(R.id.addressIntervention);

        Spinner spinner = (Spinner) getView().findViewById(R.id.codeSinistreList);

        String sinisterCode = spinner.getSelectedItem().toString();

        ListView listView = (ListView) getView().findViewById(R.id.moyenListView);

        final Intervention intervention = new Intervention();

        List<MeansItem> meansItemList = new ArrayList<MeansItem>();
        int count = listView.getChildCount();

        for (int i=0; i<count; i++) {

            TextView t = (TextView)listView.getChildAt(i).findViewById(R.id.moyen_name);
            TextView quantityStr = (TextView)listView.getChildAt(i).findViewById(R.id.moyen_quantity);

            Spinner spinner1=(Spinner)listView.getChildAt(i).findViewById(R.id.colorMeanSpinner);
            String text = spinner1.getSelectedItem().toString();
            System.out.println("JOJO");
            System.out.println(text);

            int quantity = Integer.parseInt(quantityStr.getText().toString());

            for(int q=0;q<quantity;q++){
                MeansItem meansItem = new MeansItem();
                meansItem.setMsMeanName((String) t.getText()+q);
                meansItem.setMsColor(ColorNameService.getColor(spinner1.getSelectedItem().toString()));
                meansItemList.add(meansItem);
            }

        }
        intervention.setWays(meansItemList);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");//format de la date
        String currentDateandTime = sdf.format(new Date());


        intervention.setSinisterCode(sinisterCode);
        intervention.setDate(currentDateandTime);
        intervention.setAddress(addressInter.getText().toString());
        intervention.setStatus("IN_PROGRESS");// put status of intervention
        try {
            CoordinateItem coordinateItem = getLocationFromAddress(addressInter.getText().toString());

            if(coordinateItem!=null){
                intervention.setLatitude(coordinateItem.getLatitude());
                intervention.setLongitude(coordinateItem.getLongitude());
                requestNewIntervention(intervention);
            }
            else{
                System.out.println("adresse invalide");
                Toast.makeText(getContext(),"Adresse invalide",Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    /**
     * Request new intervention with Retrofit
     * @param intervention
     */
    public void requestNewIntervention(Intervention intervention){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .build();

        //POST a new intervention
        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);
        interventionAPI.createIntervention(intervention, new Callback<Intervention>() {

            @Override
            public void success(Intervention intervention, Response response) {
                System.out.println("ca fonctionne");

                InterventionSingleton.getInstance().setIntervention(intervention);

                TabMapFragment tabMapFragment = new TabMapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, tabMapFragment).commit();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);

            }
        });

    }




    /**
     *
     * @return list data of an interventions
     */
    private ArrayList<MoyenInterventionItem> getListData(final View view, final String sinisterCode) {

        final ArrayList<MoyenInterventionItem> results = new ArrayList<MoyenInterventionItem>();



        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        DefaultWaysSinisterApi sinisterApi = restAdapter.create(DefaultWaysSinisterApi.class);




        sinisterApi.getSinisters(new Callback<List<DefaultSinister>>() {

            @Override
            public void success(List<DefaultSinister> sinisters, Response response) {

                MoyenInterventionItem newsData;
                for (DefaultSinister sinister :sinisters
                     ) {
                    if (sinisterCode.equals(sinister.getCode())){


                        String[] moyens = getResources().getStringArray(R.array.moyens);
                        for (String a:moyens
                             ) {
                            boolean find = false;
                            for (DefaultSinisterGroupWays sinisterGroup: sinister.getGroupWays()) {

                                System.out.println(a);
                                if (a.equals(sinisterGroup.getAcronym())) {
                                    find=true;
                                    newsData = new MoyenInterventionItem();

                                    newsData.setName(sinisterGroup.getAcronym());
                                    newsData.setQuantity(sinisterGroup.getCount());
                                    newsData.setColor(sinisterGroup.getColor());
                                    results.add(newsData);
                                }



                            }
                            if(!find){
                                newsData = new MoyenInterventionItem();

                                newsData.setName(a);
                                newsData.setQuantity(0);
                                newsData.setColor("#0FFF");
                                results.add(newsData);

                            }


                        }


                    }

                }

                final ListView lv1 = (ListView) view.findViewById(R.id.moyenListView);


                MoyenListAdapter moyenListAdapter = new MoyenListAdapter(getContext(), results);

                lv1.setAdapter(moyenListAdapter);
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = lv1.getItemAtPosition(position);
                        MoyenInterventionItem newsData = (MoyenInterventionItem) o;
                    }
                });


            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error.getMessage());
            }
        });



        // Add some more dummy data for testing

        return results;
    }


    /**
     * get location from address
     * @param strAddress
     * @return coordinate of location
     */
    public CoordinateItem getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            if(address.size()<=0){
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            CoordinateItem coordinateItem = new CoordinateItem();
            coordinateItem.setLatitude(String.valueOf(location.getLatitude()));
            coordinateItem.setLongitude(String.valueOf(location.getLongitude()));

            return coordinateItem;



        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




}

