package projet.istic.fr.firedrone;

/**
 * Created by tbernard on 19/04/16.
 */

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.adapter.MoyenListAdapter;
import projet.istic.fr.firedrone.model.CoordinateItem;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CreateInterventionFragment extends Fragment {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";
    public static final String GEO_API="http://maps.googleapis.com/maps/api/geocode/xml?address=";

    private static FicheFragment INSTANCE;

    public static FicheFragment getInstance() {
        if(INSTANCE == null){
            INSTANCE = new FicheFragment();
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
                System.out.println(text);

                if(text.equals("Incident")){
                    System.out.println("Incident ");
                    ArrayList image_details = getListData();
                    final ListView lv1 = (ListView) view.findViewById(R.id.moyenListView);


                    MoyenListAdapter moyenListAdapter = new MoyenListAdapter(getContext(), image_details);

                    lv1.setAdapter(moyenListAdapter);


                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = lv1.getItemAtPosition(position);
                            MoyenInterventionItem newsData = (MoyenInterventionItem) o;
                        }
                    });
                }

                else if(text.equals("Feu de foret")){
                    ArrayList image_details = getListData();
                    final ListView lv1 = (ListView) view.findViewById(R.id.moyenListView);

                    lv1.setAdapter(new MoyenListAdapter(getContext(), image_details));
                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = lv1.getItemAtPosition(position);
                            MoyenInterventionItem newsData = (MoyenInterventionItem) o;
                        }
                    });
                }

                view.findViewById(R.id.moyenListView).setVisibility(View.VISIBLE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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

            int quantity = Integer.parseInt(quantityStr.getText().toString());

            for(int q=0;q<quantity;q++){
                MeansItem meansItem = new MeansItem();
                meansItem.setName((String) t.getText()+q);
                meansItemList.add(meansItem);
            }

        }
        intervention.setWays(meansItemList);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");//format de la date
        String currentDateandTime = sdf.format(new Date());


        intervention.setSinisterCode(sinisterCode);
        intervention.setDate(currentDateandTime);
        intervention.setAddress(addressInter.getText().toString());
        intervention.setStatus("IN_PROGRESS");
        try {
            CoordinateItem coordinateItem = getLocationFromAddress(addressInter.getText().toString());

            intervention.setLatitude(coordinateItem.getLatitude());
            intervention.setLongitude(coordinateItem.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .build();

        //POST a new intervention
        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);
        interventionAPI.createIntervention(intervention, new Callback<Intervention>() {

            @Override
            public void success(Intervention intervention, Response response) {
                System.out.println("ca fonctionne");
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
    private ArrayList getListData() {
        ArrayList<MoyenInterventionItem> results = new ArrayList<MoyenInterventionItem>();

        String[] values = getResources().getStringArray(R.array.moyens);

        MoyenInterventionItem newsData;

        for (String value :values
             ) {
            newsData = new MoyenInterventionItem();

            newsData.setName(value);
            newsData.setQuantity(2);
            results.add(newsData);

        }


        // Add some more dummy data for testing

        return results;
    }




    public CoordinateItem getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
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

