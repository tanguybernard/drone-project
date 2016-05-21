package projet.istic.fr.firedrone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;

/**
 * Created by ramage on 29/04/16.
 */
public class MeansValidationFragment extends Fragment implements Observateur {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.means_validation_fragment, container, false);


        return view;
    }

    @Override
    public void actualiser(Observable o) {

    }
}
