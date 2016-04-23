package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        Intervention intervention = InterventionSingleton.getInstance().getIntervention();

        final View view = inflater.inflate(R.layout.intervention_details_fragment,container,false);

        fillDataDetails(view,intervention);

        return view;

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
        id.setText(intervention.getId());
        address.setText(intervention.getAddress());
        date.setText(intervention.getDate());
        sinisterCode.setText(intervention.getSinisterCode());

    }


}
