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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import projet.istic.fr.firedrone.model.MeansItem;

/**
 * Created by nduquesne on 18/03/16.
 */
public class MoyenFragment extends Fragment {

    private static final int IDX_CODE = 0;
    private static final int IDX_H_CALL = 1;
    private static final int IDX_H_ARRIV = 2;
    private static final int IDX_H_ENGAGED = 3;
    private static final int IDX_H_FREE = 4;

    //Instance
    private static MoyenFragment INSTANCE;

    protected View mView;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button addMeans = (Button) view.findViewById(R.id.btnAddMean);
        addMeans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MoyenAlertDialog popUp = new MoyenAlertDialog();
                popUp.show(getFragmentManager(), "");
            }
        });

    }

    private TableRow addRow(String[] plsValues, boolean pbHeader) {
        TableRow element = new TableRow(getContext());

        TextView tvMeans, tvCalls, tvArrivs, tvEngaged, tvFree;

        tvMeans = new TextView(getContext());
        tvMeans.setText(plsValues[IDX_CODE]);
        tvMeans.setGravity(Gravity.CENTER);
        tvMeans.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if (pbHeader) {
            tvMeans.setBackgroundColor(Color.GRAY);
            tvMeans.setTextColor(Color.WHITE);
        } else {
            tvMeans.setBackgroundColor(Color.LTGRAY);
            tvMeans.setTextColor(Color.BLACK);
        }

        tvCalls = new TextView(getContext());
        tvCalls.setText(plsValues[IDX_H_CALL]);
        tvCalls.setGravity(Gravity.CENTER);
        tvCalls.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if (pbHeader) {
            tvCalls.setBackgroundColor(Color.GRAY);
            tvCalls.setTextColor(Color.WHITE);
        } else {
            tvCalls.setBackgroundColor(Color.LTGRAY);
            tvCalls.setTextColor(Color.BLACK);
        }

        tvArrivs = new TextView(getContext());
        tvArrivs.setText(plsValues[IDX_H_ARRIV]);
        tvArrivs.setGravity(Gravity.CENTER);
        tvArrivs.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if (pbHeader) {
            tvArrivs.setBackgroundColor(Color.GRAY);
            tvArrivs.setTextColor(Color.WHITE);
        } else {
            tvArrivs.setBackgroundColor(Color.LTGRAY);
            tvArrivs.setTextColor(Color.BLACK);
        }

        tvEngaged = new TextView(getContext());
        tvEngaged.setText(plsValues[IDX_H_ENGAGED]);
        tvEngaged.setGravity(Gravity.CENTER);
        tvEngaged.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if (pbHeader) {
            tvEngaged.setBackgroundColor(Color.GRAY);
            tvEngaged.setTextColor(Color.WHITE);
        } else {
            tvEngaged.setBackgroundColor(Color.LTGRAY);
            tvEngaged.setTextColor(Color.BLACK);
        }

        tvFree = new TextView(getContext());
        tvFree.setText(plsValues[IDX_H_FREE]);
        tvFree.setGravity(Gravity.CENTER);
        tvFree.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        if (pbHeader) {
            tvFree.setBackgroundColor(Color.GRAY);
            tvFree.setTextColor(Color.WHITE);
        } else {
            tvFree.setBackgroundColor(Color.LTGRAY);
            tvFree.setTextColor(Color.BLACK);
        }

        element.addView(tvMeans);
        element.addView(tvCalls);
        element.addView(tvArrivs);
        element.addView(tvEngaged);
        element.addView(tvFree);

        return element;
    }

    private void loadTable () {
        final String [] columnHeaders = {"Moyens", "Demandé à", "Arrivé à", "Engagé à", "Libéré à"};
        final String [] columnButton = {""};

        TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnHeaders, true);
        table.addView(element);
    }

    public void addMean(String psCode, String psHCall) {
        final String [] columnMean = {psCode, psHCall, "", "", ""};
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnMean, false);
        final int rowIdx = table.getChildCount();
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMean(v, rowIdx);
            }
        });
        table.addView(element);
        // send to server
        MeansItem oNewMean = new MeansItem();
        oNewMean.setMsMeanCode(psCode);
        oNewMean.setMsMeanHCall(psHCall);
    }

    public void editMean(View poView, int piLineIndex) {
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = (TableRow) table.getChildAt(piLineIndex);
        for (int colIdx = 0; colIdx < element.getChildCount(); colIdx++) {
            TextView oColValue = (TextView) element.getChildAt(colIdx);
            if (oColValue.getText() == "") {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                Date hDate = c.getTime();
                SimpleDateFormat hFormat = new SimpleDateFormat("HHmm");
                oColValue.setText(hFormat.format(hDate));
                colIdx = element.getChildCount() + 1;
            }
        }
    }
}
