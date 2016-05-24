package projet.istic.fr.firedrone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.service.MeansItemService;

/**
 * Created by christophe on 20/04/16.
 */
public class MoyenEditDialog extends DialogFragment {

    private static final String NO_ERROR = "";
    private static final String ERROR_BAD_LENGTH = "L'heure doit être composée de 4 chiffres";
    private static final String ERROR_BAD_HOUR = "L'heure doit être comprise entre 00 et 23";
    private static final String ERROR_BAD_MIN = "Les minutes doivent être comprises entre 00 et 59";
    private static final String ERROR_BAD_ORDER = "L'heure doit être supérieure aux heures précédentes";
    private static final String ERROR_NAN = "L'heure ne doit être composée que de chiffres";

    private int miLine = -1;
    private int miType = 0;
    private String msCode = "";
    private TableLayout moTable;

    private View dialView;

    public void setMiLine(int piLine, TableLayout poTable) {
        this.miLine = piLine;
        this.moTable = poTable;
    }

    private String getTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Date hDate = c.getTime();
        SimpleDateFormat hFormat = new SimpleDateFormat("HHmm");
        return hFormat.format(hDate);
    }

    private String checkTime(String psTime) {
        String error = NO_ERROR;
        try {
            Integer.parseInt(psTime);
        } catch (NumberFormatException e) {
            error = ERROR_NAN;
        }
        if (error.equals(NO_ERROR) && psTime.length() == 4) {
            String sHour = psTime.substring(0, 2);
            String sMinute = psTime.substring(2, 4);
            if (Integer.parseInt(sHour) < 0 || Integer.parseInt(sHour) > 23) {
                error = ERROR_BAD_HOUR;
            }
            if (Integer.parseInt(sMinute) < 0 || Integer.parseInt(sMinute) > 59) {
                error = ERROR_BAD_MIN;
            }
            TableRow element = (TableRow) this.moTable.getChildAt(this.miLine);
            for (int colIdx = getResources().getInteger(R.integer.IDX_H_CALL); colIdx < this.miType; colIdx++) {
                LinearLayout cellLayout = ((LinearLayout) (element.getChildAt(colIdx)));
                TextView oColValue = (TextView) cellLayout.getChildAt(0);
                if (!oColValue.getText().equals("")) {
                    if (Integer.parseInt(oColValue.getText().toString()) > Integer.parseInt(psTime)) {
                        error = ERROR_BAD_ORDER;
                    }
                }
            }
        } else {
            error = ERROR_BAD_LENGTH;
        }
        return error;
    }

    public void setDialogType() {
        if (miLine > -1) {
            TableRow element = (TableRow) this.moTable.getChildAt(this.miLine);
            LinearLayout cellLayout = ((LinearLayout) (element.getChildAt(getResources().getInteger(R.integer.IDX_CODE))));
            this.msCode = ((TextView) cellLayout.getChildAt(0)).getText().toString();
            for (int colIdx = getResources().getInteger(R.integer.IDX_H_CALL); colIdx < element.getChildCount(); colIdx++) {
                cellLayout = ((LinearLayout) (element.getChildAt(colIdx)));
                TextView oColValue = (TextView) cellLayout.getChildAt(0);
                if (oColValue.getText() == "") {
                    this.miType = colIdx;
                    colIdx = element.getChildCount() + 1;
                }
            }
        }
        TextView oLblTitle = ((TextView) this.dialView.findViewById(R.id.lblPopupMeans));
        TextView oLblHour = ((TextView) this.dialView.findViewById(R.id.lblTxtHour));
        TextView oTxtHour = ((TextView) this.dialView.findViewById(R.id.txtEditHour));
        oTxtHour.setText(getTime());
        if (this.miLine == -1) {
            oLblTitle.setText(getResources().getText(R.string.lbl_add_mean));
        } else {
            oLblTitle.setText(getResources().getText(R.string.lbl_edit_mean));
            Spinner lstMeans = (Spinner) this.dialView.findViewById(R.id.lstMeans);
            int iIdxSlc = 0;
            for (int spIdx = 0; spIdx < lstMeans.getCount(); spIdx++) {
                String sTmpValue = lstMeans.getItemAtPosition(spIdx).toString();
                if (this.msCode.equals(sTmpValue)) {
                    iIdxSlc = spIdx;
                }
            }
            lstMeans.setSelection(iIdxSlc);
            lstMeans.setEnabled(false);
            if (this.miType == getResources().getInteger(R.integer.IDX_H_CALL)) {
                oLblHour.setText(getResources().getText(R.string.lbl_hour_call));
            } else if (this.miType == getResources().getInteger(R.integer.IDX_H_ARRIV)) {
                oLblHour.setText(getResources().getText(R.string.lbl_hour_arriv));
            } else if (this.miType == getResources().getInteger(R.integer.IDX_H_ENGAGED)) {
                oLblHour.setText(getResources().getText(R.string.lbl_hour_engaged));
            } else if (this.miType == getResources().getInteger(R.integer.IDX_H_FREE)) {
                oLblHour.setText(getResources().getText(R.string.lbl_hour_free));
            }
        }
    }

    public int getCodeIndex(TableLayout poTable, String psCode) {
        int iResult = 0;
        if (poTable.getChildCount() > 1) {
            for (int iRow = 1; iRow < poTable.getChildCount(); iRow++) {
                TableRow oRow = (TableRow) poTable.getChildAt(iRow);
                LinearLayout cellLayout = ((LinearLayout) (oRow.getChildAt(getResources().getInteger(R.integer.IDX_CODE))));
                String sCode = ((TextView) cellLayout.getChildAt(0)).getText().toString();
                if (psCode.equals(sCode)) {
                    iResult++;
                }
            }
        }
        return iResult;
    }

    private String getMeanColor(String psCode) {
        String sColor = "#000000";
        MeansItemService oMeansService = new MeansItemService();
        List<MeansItem> loDefMeans = oMeansService.getListDefaultMeansItem();
        if (loDefMeans != null && loDefMeans.size() > 0) {
            for (MeansItem oMean : loDefMeans) {
                if (psCode.equals(oMean.getMsMeanCode())) {
                    sColor = oMean.getMsColor();
                }
            }
        }
        return sColor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.dialView = inflater.inflate(R.layout.means_popup, null);
        setDialogType();

        final Button validMean = (Button) this.dialView.findViewById(R.id.btnValid);
        validMean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner lstMeans = (Spinner) dialView.findViewById(R.id.lstMeans);
                String sNewMean = lstMeans.getSelectedItem().toString();
                String sNewHour = ((EditText) dialView.findViewById(R.id.txtEditHour)).getText().toString();
                TextView txtErr = (TextView) dialView.findViewById(R.id.lblHourError);
                Log.d("HOUR", sNewHour);
                String sError = checkTime(sNewHour);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                Date hDate = c.getTime();
                SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");

                if (sError.equals(NO_ERROR)) {
                    MoyenFragment moyen = MoyenFragment.getInstance();
                    String[] tsHours = new String[getResources().getInteger(R.integer.IDX_H_FREE) + 1];
                    tsHours[getResources().getInteger(R.integer.IDX_CODE)] = sNewMean;
                    int iCodeIndex = getCodeIndex(moTable, sNewMean);
                    String iMeanColor = getMeanColor(sNewMean);
                    sNewMean = sNewMean + String.valueOf(iCodeIndex);
                    tsHours[getResources().getInteger(R.integer.IDX_NAME)] = sNewMean;
                    if (miLine == -1) {
                        miType = getResources().getInteger(R.integer.IDX_H_CALL);
                    }
                    sNewHour = dFormat.format(hDate) + " " + sNewHour;
                    tsHours[miType] = sNewHour;
                    if (miLine == -1) {
                        moyen.addMean(tsHours, true, iMeanColor, FiredroneConstante.MEAN_DEMANDED);
                    } else {
                        moyen.editMean(sNewHour, miLine);
                    }
                    txtErr.setText("");
                    dismiss();
                } else {
                    txtErr.setText("Erreur : " + sError);
                    txtErr.setTextColor(Color.RED);
                }
            }
        });

        final Button cancelMean = (Button) this.dialView.findViewById(R.id.btnCancel);
        cancelMean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(this.dialView);
        return builder.create();
    }
}