package projet.istic.fr.firedrone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by christophe on 20/04/16.
 */
public class MoyenAlertDialog extends DialogFragment {

    private int miLine = 0;
    private int miType = 0;

    public void setDialogType(int piType, View poView) {
        TextView oLblTitle = ((TextView) poView.findViewById(R.id.lblPopupMeans));
        TextView oLblHour = ((TextView) poView.findViewById(R.id.lblTxtHour));
        if (piType == 0) {
            oLblTitle.setText(getResources().getText(R.string.lbl_add_mean));
        } else {
            oLblTitle.setText(getResources().getText(R.string.lbl_edit_mean));
            switch (piType) {
                case 1:
                    oLblHour.setText(getResources().getText(R.string.lbl_hour_call));
                    break;
                case 2:
                    oLblHour.setText(getResources().getText(R.string.lbl_hour_arriv));
                    break;
                case 3:
                    oLblHour.setText(getResources().getText(R.string.lbl_hour_engaged));
                    break;
                case 4:
                    oLblHour.setText(getResources().getText(R.string.lbl_hour_free));
                    break;
            }
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.means_popup, null);

//getResources().getStringArray(R.array.moyen_array);

        builder.setView(dialogView)
                .setPositiveButton(R.string.btn_valid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MoyenFragment moyen = MoyenFragment.getInstance();
                        Spinner lstMeans = (Spinner) dialogView.findViewById(R.id.lstMeans);
                        String sNewMean = lstMeans.getSelectedItem().toString();
                        String sNewHour = ((EditText) dialogView.findViewById(R.id.txtEditHour)).getText().toString();
                        moyen.addMean(miType, sNewMean, sNewHour, miLine);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing to do
                    }
                });
        return builder.create();

    }


}