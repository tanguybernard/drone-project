package projet.istic.fr.firedrone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import projet.istic.fr.firedrone.service.ColorNameService;

/**
 * Created by christophe on 27/04/16.
 */
public class MoyenColorDialog extends DialogFragment {

    private View dialView;

    private int miLine;
    private TableLayout moEditTable;
    private String msColor;

    public void setLine (int piLine, String psColor, TableLayout poTable) {
        this.miLine = piLine;
        this.moEditTable = poTable;
        this.msColor = psColor;
    }

    private void updateColor(String psColor) {
        TableRow oRow = (TableRow) moEditTable.getChildAt(this.miLine);
        LinearLayout oCell = (LinearLayout) oRow.getChildAt(getResources().getInteger(R.integer.IDX_NAME));
        TextView oTxtName = (TextView) oCell.getChildAt(0);
        ImageView oImgColor = (ImageView) oCell.getChildAt(1);
        oTxtName.setTextColor(Color.parseColor(psColor));
        oImgColor.setBackgroundColor(Color.parseColor(psColor));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.dialView = inflater.inflate(R.layout.means_select_color, null);

        ColorNameService oColors = new ColorNameService();
        Map<String, String> mapColors = oColors.getAllType();
        Iterator<Entry<String, String>> ieColors = mapColors.entrySet().iterator();
        Spinner lsColors = (Spinner) dialView.findViewById(R.id.lstColors);

        List<String> lsColorsValues = new ArrayList<String>();
        Set<String> ssColorsKeys = mapColors.keySet();

        /*ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lsColors.setAdapter(spinnerAdapter);
        for (String sKey : ssColorsKeys) {
            spinnerAdapter.add(sKey);
            lsColorsValues.add(mapColors.get(sKey));
        }

        int iSlcIdx = 0;
        for (int iValue = 0; iValue < lsColorsValues.size(); iValue++) {
            lsColors.getChildAt(iValue).setBackgroundColor(Color.parseColor(lsColorsValues.get(iValue)));
            if (this.msColor.equals(lsColorsValues.get(iValue))) {
                iSlcIdx = iValue;
            }
        }*/
        int iSlcIdx = 0;
        while (ieColors.hasNext()) {
            Entry<String, String> eColors = ieColors.next();
            String sText = eColors.getKey();
            String sColor = eColors.getValue();
            TextView oView = new TextView(getContext());
            oView.setText(sText);
            oView.setBackgroundColor(Color.parseColor(sColor));
            lsColors.addView(oView);
            if (!this.msColor.equals(sColor)) {
                iSlcIdx++;
            }
        }

        lsColors.setSelection(iSlcIdx);

        final Button validMean = (Button) this.dialView.findViewById(R.id.btn_valid_color);
        validMean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner lsColors = (Spinner) dialView.findViewById(R.id.lstColors);
                String slcColor = (String) lsColors.getSelectedItem();
                updateColor(slcColor);
                dismiss();
            }
        });

        final Button cancelColor = (Button) this.dialView.findViewById(R.id.btn_cancel_color);
        cancelColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(this.dialView);
        return builder.create();
    }
}
