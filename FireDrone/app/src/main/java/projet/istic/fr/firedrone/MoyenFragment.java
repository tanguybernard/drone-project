package projet.istic.fr.firedrone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nduquesne on 18/03/16.
 */
public class MoyenFragment extends Fragment {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";

    //Instance
    private static MoyenFragment INSTANCE;

    protected View mView;

    private InterventionSingleton oIntervention = InterventionSingleton.getInstance();


    //singleton, une seule instance du fragment moyen
    public static MoyenFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoyenFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moyen, container, false);
        this.mView = view;
        loadTable();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMeans();

        final Button addMeans = (Button) view.findViewById(R.id.btnAddMean);
        addMeans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MoyenAlertDialog popUp = new MoyenAlertDialog();
                popUp.setMiLine(-1, (TableLayout) mView.findViewById(R.id.tableMeans));
                popUp.show(getFragmentManager(), "");
            }
        });
    }

    private TableRow addRow(String[] plsValues, boolean pbHeader, int piColor) {
        TableRow element = new TableRow(getContext());

        for (int iView = 0; iView < plsValues.length; iView++) {
            TextView tvColumn = new TextView(getContext());
            tvColumn.setText(plsValues[iView]);
            if (iView < getResources().getInteger(R.integer.IDX_NAME)) {
                tvColumn.setVisibility(View.INVISIBLE);
            }
            tvColumn.setGravity(Gravity.CENTER);
            tvColumn.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            if (pbHeader) {
                tvColumn.setBackgroundColor(Color.GRAY);
                tvColumn.setTextColor(Color.WHITE);
            } else {
                tvColumn.setBackgroundColor(Color.LTGRAY);
                if (iView == getResources().getInteger(R.integer.IDX_NAME)) {
                    tvColumn.setTextColor(piColor);
                } else {
                    tvColumn.setTextColor(Color.BLACK);
                }
            }
            element.addView(tvColumn);
        }
        return element;
    }

    private void loadTable () {
        final String [] columnHeaders = {"", "", "Moyens", "Demandé à", "Arrivé à", "Engagé à", "Libéré à"};

        TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnHeaders, true, -1);
        table.addView(element);
        table.setColumnCollapsed(0, true);
        table.setColumnCollapsed(1, true);
    }

    public int getCodeIndex(TableLayout poTable, String psCode) {
        int iResult = 0;
        if (poTable.getChildCount() > 0) {
            for (int iRow = 0; iRow < poTable.getChildCount(); iRow++) {
                TableRow oRow = (TableRow) poTable.getChildAt(iRow);
                String sCode = ((TextView) oRow.getChildAt(getResources().getInteger(R.integer.IDX_CODE))).getText().toString();
                if (psCode.equals(sCode)) {
                    iResult++;
                }
            }
        }
        return iResult;
    }

    public void addMean(String[] psHours, boolean pbSend) {
        for (int idxTime = getResources().getInteger(R.integer.IDX_H_CALL); idxTime < psHours.length; idxTime++) {
            if (psHours[idxTime] != null && !psHours[idxTime].isEmpty()) {
                psHours[idxTime] = psHours[idxTime].split(" ")[1];
            }
        }

        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        int iCodeIndex = getCodeIndex(table, psHours[getResources().getInteger(R.integer.IDX_NAME)]);
        psHours[getResources().getInteger(R.integer.IDX_NAME)] = psHours[getResources().getInteger(R.integer.IDX_NAME)] + String.valueOf(iCodeIndex);
        TableRow element = addRow(psHours, false, Color.BLACK); // TODO Get Mean Color
        final int rowIdx = table.getChildCount();
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoyenAlertDialog popUp = new MoyenAlertDialog();
                popUp.setMiLine(rowIdx, (TableLayout) mView.findViewById(R.id.tableMeans));
                popUp.show(getFragmentManager(), "");
            }
        });
        table.addView(element);
        table.setColumnCollapsed(0, true);
        // send to server
        if (pbSend) {
            MeansItem oNewMean = new MeansItem();
            oNewMean.setMsMeanCode(psHours[getResources().getInteger(R.integer.IDX_CODE)]);
            oNewMean.setMsMeanName(psHours[getResources().getInteger(R.integer.IDX_NAME)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_CALL)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_ARRIV)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_ENGAGED)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_FREE)]);
            MeansItemService.addMean(oNewMean);
        }
    }

    public void editMean(String psHour, int piLineIndex) {
        psHour = psHour.split(" ")[1];
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = (TableRow) table.getChildAt(piLineIndex);
        MeansItem oMean = new MeansItem();
        boolean bEmpty = false;
        for (int colIdx = 0; colIdx < element.getChildCount(); colIdx++) {
            TextView oColValue = (TextView) element.getChildAt(colIdx);
            if (oColValue.getText().equals("") && colIdx > getResources().getInteger(R.integer.IDX_NAME)) {
                oColValue.setText(psHour);
                bEmpty = true;
            }
            if (colIdx == getResources().getInteger(R.integer.IDX_ID)) {
                oMean.setMsMeanId(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_CODE)) {
                oMean.setMsMeanCode(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_NAME)) {
                oMean.setMsMeanName(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_CALL)) {
                oMean.setMsMeanHCall(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_ARRIV)) {
                oMean.setMsMeanHArriv(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_ENGAGED)) {
                oMean.setMsMeanHEngaged(oColValue.getText().toString());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_FREE)) {
                oMean.setMsMeanHFree(oColValue.getText().toString());
            }
            if (bEmpty) {
                colIdx = element.getChildCount() + 1;
            }
        }
        MeansItemService.editMean(oMean);
    }

    public void getMeans() {
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        table.removeAllViews();
        loadTable();
        List<MeansItem> loMeans = oIntervention.getIntervention().getWays();
        if (loMeans != null && loMeans.size() > 0) {
            table.removeAllViews();
            loadTable();
            for (MeansItem oMean : loMeans) {
                String[] sHours = {oMean.getMsMeanId(),
                        oMean.getMsMeanCode(),
                        oMean.getMsMeanName(),
                        oMean.getMsMeanHCall(),
                        oMean.getMsMeanHArriv(),
                        oMean.getMsMeanHEngaged(),
                        oMean.getMsMeanHFree()};
                addMean(sHours, false);
            }
        }
    }
}