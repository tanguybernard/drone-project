package projet.istic.fr.firedrone;

import android.content.Intent;
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

    private TableRow addRow(String[] plsValues, boolean pbHeader) {
        TableRow element = new TableRow(getContext());

        TextView tvMeans, tvCalls, tvArrivs, tvEngaged, tvFree;

        tvMeans = new TextView(getContext());
        tvMeans.setText(plsValues[getResources().getInteger(R.integer.IDX_CODE)]);
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
        tvCalls.setText(plsValues[getResources().getInteger(R.integer.IDX_H_CALL)]);
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
        tvArrivs.setText(plsValues[getResources().getInteger(R.integer.IDX_H_ARRIV)]);
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
        tvEngaged.setText(plsValues[getResources().getInteger(R.integer.IDX_H_ENGAGED)]);
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
        tvFree.setText(plsValues[getResources().getInteger(R.integer.IDX_H_FREE)]);
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

    public void addMean(String[] psHours, boolean pbSend) {
        final String[] columnMean = {psHours[getResources().getInteger(R.integer.IDX_CODE)],
                psHours[getResources().getInteger(R.integer.IDX_H_CALL)],
                psHours[getResources().getInteger(R.integer.IDX_H_ARRIV)],
                psHours[getResources().getInteger(R.integer.IDX_H_ENGAGED)],
                psHours[getResources().getInteger(R.integer.IDX_H_FREE)]};
        for (int idxTime = 1; idxTime < columnMean.length; idxTime++) {
            if (columnMean[idxTime] != null && !columnMean[idxTime].isEmpty()) {
                columnMean[idxTime] = columnMean[idxTime].split(" ")[1];
            }
        }
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnMean, false);
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
        // send to server
        if (pbSend) {
            MeansItem oNewMean = new MeansItem();
            oNewMean.setMsMeanCode(psHours[getResources().getInteger(R.integer.IDX_CODE)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_CALL)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_ARRIV)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_ENGAGED)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_FREE)]);
        }
    }

    public void editMean(String psHour, int piLineIndex) {
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = (TableRow) table.getChildAt(piLineIndex);
        for (int colIdx = 0; colIdx < element.getChildCount(); colIdx++) {
            TextView oColValue = (TextView) element.getChildAt(colIdx);
            if (oColValue.getText().equals("")) {
                oColValue.setText(psHour);
                colIdx = element.getChildCount() + 1;
            }
        }
    }

    public void getMeans() {
        final String sIntervId = "14";
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).build();
        final View oView = this.mView;
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.GetMeans(sIntervId, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> loMeans, Response response) {
                final TableLayout table = (TableLayout) oView.findViewById(R.id.tableMeans);
                if (loMeans != null && loMeans.size() > 0) {
                    table.removeAllViews();
                    for (MeansItem oMean : loMeans) {
                        String sCode = oMean.getMsMeanCode();
                        String[] sHours = {sCode, oMean.getMsMeanHCall(), oMean.getMsMeanHArriv(), oMean.getMsMeanHEngaged(), oMean.getMsMeanHFree()};
                        addMean(sHours, false);
                    }
                } else {
                    Log.d("LIST ERROR", "Empty List");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("CONNEXION ERROR", error.getMessage());
            }
        });
    }

    public void sendMean(MeansItem poMean) {
        final String sIntervId = "14";
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(END_POINT).build();
        final View oView = this.mView;
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);
        meansApi.AddMean(sIntervId, poMean, new Callback<MeansItem>() {
            @Override
            public void success(MeansItem poMean, Response response) {
                Log.d("CONNEXION SUCCESS", "Connection OK");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("CONNEXION ERROR", error.getMessage());
            }
        });
    }

}
