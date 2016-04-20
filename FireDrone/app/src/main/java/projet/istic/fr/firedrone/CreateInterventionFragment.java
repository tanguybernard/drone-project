package projet.istic.fr.firedrone;

/**
 * Created by tbernard on 19/04/16.
 */

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
import android.widget.Spinner;

import java.util.ArrayList;

import projet.istic.fr.firedrone.adapter.CustomListAdapter;
import projet.istic.fr.firedrone.adapter.MoyenListAdapter;
import projet.istic.fr.firedrone.model.InterventionItem;
import projet.istic.fr.firedrone.model.MoyenItem;


public class CreateInterventionFragment extends Fragment {

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
                    System.out.println("MOIUAOAAO");
                    ArrayList image_details = getListData();
                    final ListView lv1 = (ListView) view.findViewById(R.id.moyenListView);

                    lv1.setAdapter(new MoyenListAdapter(getContext(), image_details));
                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = lv1.getItemAtPosition(position);
                            MoyenItem newsData = (MoyenItem) o;
                        }
                    });
                }

                else if(text.equals("Feu de foret")){
                    System.out.println("MOIUAOAAO");
                    //Button bttt = (Button)view.findViewById(R.id.moyen_name);
                    //bttt.setText("LOLILO");
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


    /**
     *
     * @return list data of an interventions
     */
    private ArrayList getListData() {
        ArrayList<MoyenItem> results = new ArrayList<MoyenItem>();

        String[] values = getResources().getStringArray(R.array.moyens);

        MoyenItem newsData = new MoyenItem();

        for (String value :values
             ) {

            System.out.println(value);
            newsData.setName(value);
            newsData.setQuantity(2);
            results.add(newsData);

        }



        // Add some more dummy data for testing

        return results;
    }

}

