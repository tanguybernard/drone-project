package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import projet.istic.fr.firedrone.Interceptor.Interceptor;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tbernard on 23/04/16.
 */
public class DetailsInterventionFragment extends Fragment implements Observateur {

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

        final View view = inflater.inflate(R.layout.intervention_details_fragment, container, false);

        fillDataDetails(view,intervention);

        final Button buttonliberer = (Button)view.findViewById(R.id.btnlibererCos);
        buttonliberer.setVisibility(View.GONE);
        final Button buttondevenir = (Button)view.findViewById(R.id.btndevenirCos);
        buttondevenir.setVisibility(View.GONE);

        /**   Fields details of the Actual COS    **/
        final TextView cosLabel = (TextView) view.findViewById(R.id.cosLabel);
        final TextView cosInformation = (TextView) view.findViewById(R.id.cosInformation);

        /** Set OnClickListener on the two Differents COS buttons **/

        buttondevenir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InterventionAPI interventionAPI = Interceptor.getInstance().getRestAdapter().create(InterventionAPI.class);
                interventionAPI.setInterventionCos(intervention.getId(), "COS", new Callback<Intervention>() {
                    @Override
                    public void success(Intervention intervention, Response response) {
                        buttondevenir.setVisibility(View.GONE);
                        buttonliberer.setVisibility(View.VISIBLE);
                        cosInformation.setText(intervention.getCos().getLastname() + " " + intervention.getCos().getFirstname());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.print("Impossible de devenir Cos:" + error);
                    }
                });
            }
        });

        buttonliberer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InterventionAPI interventionAPI = Interceptor.getInstance().getRestAdapter().create(InterventionAPI.class);
                interventionAPI.deletenterventionCos(intervention.getId(), new Callback<Intervention>() {
                    @Override
                    public void success(Intervention intervention, Response response) {
                        buttondevenir.setVisibility(View.VISIBLE);
                        buttonliberer.setVisibility(View.GONE);
                        cosInformation.setText("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.print("Impossible de devenir Cos:" + error);
                    }
                });
            }
        });

        /**  - - - - - - - - - - - - - - - - - - - -    **/

        if(InterventionSingleton.getInstance().getIntervention().getCos()==null)
        {
            //rendre visible le boutton devenir CoS
            buttondevenir.setVisibility(View.VISIBLE);
        }
        else {
            /**  S'il y a un COS, on affiche ses informations  **/
            cosInformation.setText(intervention.getCos().getLastname() + " " + intervention.getCos().getFirstname());

            if(InterventionSingleton.getInstance().getIntervention().getCos().getLogin().equals(UserSingleton.getInstance().getUser().getLogin())){
                buttonliberer.setVisibility(View.VISIBLE);
            }
            else{
                //masquer tous les deux bouttons
                buttondevenir.setVisibility(View.GONE);
                buttonliberer.setVisibility(View.GONE);
            }
        }
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
        if(intervention!=null) {
            id.setText(intervention.getId());
            address.setText(intervention.getAddress());
            date.setText(intervention.getDate());
            sinisterCode.setText(intervention.getSinisterCode());
        }
    }

    @Override
    public void actualiser(Observable o) {

    }
}
