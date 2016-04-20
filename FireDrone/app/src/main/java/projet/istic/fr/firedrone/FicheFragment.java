package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        final View view = inflater.inflate(R.layout.fragment_fiche,container,false);

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
        });

        return view;
    }

}
