package projet.istic.fr.firedrone;

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

/**
 * Created by nduquesne on 18/03/16.
 */
public class MoyenFragment extends Fragment {

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
                addMean(v);
            }
        });

    }

    private TableRow addRow(String[] plsValues) {
        TableRow element = new TableRow(getContext());

        TextView tvMeans, tvCalls, tvArrivs, tvEngaged, tvFree;

        tvMeans = new TextView(getContext());
        tvMeans.setText(plsValues[0]);
        tvMeans.setGravity(Gravity.CENTER);
        tvMeans.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );

        tvCalls = new TextView(getContext());
        tvCalls.setText(plsValues[1]);
        tvCalls.setGravity(Gravity.CENTER);
        tvCalls.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        tvArrivs = new TextView(getContext());
        tvArrivs.setText(plsValues[2]);
        tvArrivs.setGravity(Gravity.CENTER);
        tvArrivs.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        tvEngaged = new TextView(getContext());
        tvEngaged.setText(plsValues[3]);
        tvEngaged.setGravity(Gravity.CENTER);
        tvEngaged.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        tvFree = new TextView(getContext());
        tvFree.setText(plsValues[4]);
        tvFree.setGravity(Gravity.CENTER);
        tvFree.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

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
        TableRow element = addRow(columnHeaders);
        table.addView(element);
    }

    public void addMean(View poView) {
        final String [] columnMean = {"VSAV", "", "", "", ""};
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnMean);
        final int rowIdx = table.getChildCount();
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMean(v, rowIdx);
            }
        });
        table.addView(element);
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
                SimpleDateFormat hFormat = new SimpleDateFormat("HH:mm");
                oColValue.setText(hFormat.format(hDate));
                colIdx = element.getChildCount() + 1;
            }
        }
    }
}
