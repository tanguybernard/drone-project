package projet.istic.fr.firedrone;

/**
 * Created by tbernard on 19/04/16.
 */

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

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        content.setText(intervention.content);
        date.setText(intervention.date);
        type.setText(intervention.type);
        author.setText(intervention.author);

        }

        @Override
        public void failure(RetrofitError error) {
        Log.d("==retrofit==", error.toString());
        }
        });*/



        final View view = inflater.inflate(R.layout.intervention_creation,container,false);





        /*ArrayList image_details = getListData();
        final ListView lv1 = (ListView) view.findViewById(R.id.moyenList);

        lv1.setAdapter(new CustomListAdapter(this.getContext(), image_details));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                InterventionItem newsData = (InterventionItem) o;
            }
        });*/


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
                    System.out.println("MOIUAOAAO333");
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

                // your code here
                //if (spinner1x.equals("poison")){
                    //spinner2.setVisibility(View.VISIBLE);
                    //spinner3.setVisibility(View.GONE);
                //}

                System.out.println("dfdsfsdfsdfsdfsd");
                view.findViewById(R.id.moyenListView).setVisibility(View.VISIBLE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        return view;
    }


    public void sendNewIntervention(View view){

        EditText addressInter = (EditText) getView().findViewById(R.id.addressIntervention);

        Spinner spinner = (Spinner) getView().findViewById(R.id.codeSinistreList);

        String sinisterCode = spinner.getSelectedItem().toString();

        System.out.println("CODE SINISTRE");
        System.out.println(sinisterCode);



        ListView listView = (ListView) getView().findViewById(R.id.moyenListView);
       // ListView listView = (ListView) view.findViewById(R.id.moyenListView);

        //int first = listView.getFirstVisiblePosition();
        int count = listView.getChildCount();
        for (int i=0; i<count; i++) {

            TextView t = (TextView)listView.getChildAt(i).findViewById(R.id.moyen_name);
            TextView t1 = (TextView)listView.getChildAt(i).findViewById(R.id.moyen_quantity);

            System.out.println(t.getText());
            System.out.println(t1.getText());


        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());


        //"263 Avenue Général Leclerc, 35000 Rennes"
        final Intervention intervention = new Intervention(sinisterCode,currentDateandTime,addressInter.getText().toString(),"IN_PROGRESS");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .build();

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
        System.out.println("LOL");
        ArrayList<MoyenInterventionItem> results = new ArrayList<MoyenInterventionItem>();

        String[] values = getResources().getStringArray(R.array.moyens);

        MoyenInterventionItem newsData;

        for (String value :values
             ) {
            newsData = new MoyenInterventionItem();

            System.out.println(value);
            newsData.setName(value);
            newsData.setQuantity(2);
            results.add(newsData);

        }


        // Add some more dummy data for testing

        return results;
    }

    public CoordinateItem getCoordinatesByAdress(String adress) throws Exception {
        CoordinateItem coordonne=new CoordinateItem();

        int responseCode = 0;
        String api = GEO_API + URLEncoder.encode(adress, "UTF-8") + "&sensor=true";
        URL url = null;
        try {
            url = new URL(api);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        try {
            httpConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            responseCode = httpConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(responseCode == 200)
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
            Document document = builder.parse(httpConnection.getInputStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/GeocodeResponse/status");
            String status = (String)expr.evaluate(document, XPathConstants.STRING);
            if(status.equals("OK"))
            {
                expr = xpath.compile("//geometry/location/lat");
                String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
                System.out.print("============Coordonnes=============="+"\n");
                coordonne.setLatitude(latitude);
                expr = xpath.compile("//geometry/location/lng");
                String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
                coordonne.setLongitude(longitude);

            }
            else
            {
                throw new Exception("Error from the API - response status: "+status);
            }
        }
        return  coordonne;

    }

}

