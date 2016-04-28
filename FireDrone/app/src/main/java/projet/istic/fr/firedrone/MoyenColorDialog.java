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

import projet.istic.fr.firedrone.adapter.MoyenListAdapter;
import projet.istic.fr.firedrone.adapter.SpinnerColorAdapter;
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

    private void updateColor(String psType) {
        ColorNameService oColors = new ColorNameService();
        Map<String, String> mapColors = oColors.getAllType();
        String psColor = mapColors.get(psType);
        TableRow oRow = (TableRow) moEditTable.getChildAt(this.miLine);
        LinearLayout oCellName = (LinearLayout) oRow.getChildAt(getResources().getInteger(R.integer.IDX_NAME));
        TextView oTxtName = (TextView) oCellName.getChildAt(0);
        ImageView oImgColor = (ImageView) oCellName.getChildAt(1);
        LinearLayout oCellId = (LinearLayout) oRow.getChildAt(getResources().getInteger(R.integer.IDX_ID));
        TextView oTxtId = (TextView) oCellId.getChildAt(0);
        Log.d("TYPE", psType);
        Log.d("COLOR", psColor == null ? "NULL" : psColor);
        oTxtName.setTextColor(Color.parseColor(psColor));
        oImgColor.setBackgroundColor(Color.parseColor(psColor));
        MoyenFragment oMoyenFrag = MoyenFragment.getInstance();
        oMoyenFrag.changeColor(oTxtId.getText().toString(), psColor);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.dialView = inflater.inflate(R.layout.means_select_color, null);

        Spinner lsColors = (Spinner) dialView.findViewById(R.id.lstColors);
        lsColors.setAdapter(new SpinnerColorAdapter(this.getContext(), null));

        final Button validMean = (Button) this.dialView.findViewById(R.id.btn_valid_color);
        validMean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner lsColors = (Spinner) dialView.findViewById(R.id.lstColors);
                String slcType = (String) lsColors.getSelectedItem();
                updateColor(slcType);
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
