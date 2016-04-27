package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;

/**
 * Created by tbernard on 23/04/16.
 */
public class DetailsInterventionFragment extends Fragment {


    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";

    private static DetailsInterventionFragment INSTANCE;

    public static DetailsInterventionFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DetailsInterventionFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle saveInstantState) {

        final Intervention intervention = InterventionSingleton.getInstance().getIntervention();

        final View view = inflater.inflate(R.layout.intervention_details_fragment,container,false);

        fillDataDetails(view,intervention);



        Button buttonliberer = (Button)view.findViewById(R.id.btnlibererCos);
        //Si je suis COS je dois pouvoir voir le boutton [liberer COS]
        if(1==1){
            buttonliberer.setVisibility(View.VISIBLE);
            buttonliberer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    freeCos(view, intervention);
                }
            });
        }
        else{
            buttonliberer.setVisibility(View.GONE);
        }


        Button buttondevenir = (Button)view.findViewById(R.id.btndevenirCos);
        //Si je suis pas COS et Que y'a pas de COS pour l'intervention je dois pouvoir voir le boutton [devenir COS]
        if(1==2){
            buttondevenir.setVisibility(View.VISIBLE);
            buttondevenir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    becomeCos(view, intervention);
                }
            });
        }
        else{
            buttondevenir.setVisibility(View.GONE);
        }

        return view;
    }

    /**
     *
     * @param view
     * @param intervention
     */
    public void becomeCos(View view,Intervention intervention){

    }

    /**
     *
     * @param view
     * @param intervention
     */
    public void freeCos(View view,Intervention intervention){

    }

    /**
     * Fill data intervention into details fragment
     * @param view
     * @param intervention
     */
    public void fillDataDetails(View view,Intervention intervention){

        TextView id = (TextView) view.findViewById(R.id.idText);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView sinisterCode = (TextView) view.findViewById(R.id.sinisterCodeText);
        if(intervention!=null) {
            id.setText(intervention.getId());
            address.setText(intervention.getAddress());
            date.setText(intervention.getDate());
            sinisterCode.setText(intervention.getSinisterCode());
        }

    }

}
