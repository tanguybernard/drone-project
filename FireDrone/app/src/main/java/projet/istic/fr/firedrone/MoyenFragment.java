package projet.istic.fr.firedrone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.MeansItemStatus;
import projet.istic.fr.firedrone.service.MeansItemService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import projet.istic.fr.firedrone.singleton.UserSingleton;
import projet.istic.fr.firedrone.synchro.Observable;
import projet.istic.fr.firedrone.synchro.Observateur;

/**
 * Created by nduquesne on 18/03/16.
 */
public class MoyenFragment extends Fragment implements Observateur {

    public static final String END_POINT = "http://m2gla-drone.istic.univ-rennes1.fr:8080";

    //Instance
    private static MoyenFragment INSTANCE;

    protected View mView;

    private InterventionSingleton oIntervention = InterventionSingleton.getInstance();

    private UserSingleton oUser = UserSingleton.getInstance();


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

        if (oUser.getUser().getRole().equals(FiredroneConstante.ROLE_COS)) {
            final Button addMeans = (Button) view.findViewById(R.id.btnAddMean);
            addMeans.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MoyenEditDialog popUp = new MoyenEditDialog();
                    popUp.setMiLine(-1, (TableLayout) mView.findViewById(R.id.tableMeans));
                    popUp.show(getFragmentManager(), "");
                }
            });
        }
    }

    private TableRow addRow(String[] plsValues, boolean pbHeader, final String psColor, String psStatus) {
        TableRow element = new TableRow(getContext());

        final int iRowIdx = ((TableLayout) mView.findViewById(R.id.tableMeans)).getChildCount();

        for (int iView = 0; iView < plsValues.length; iView++) {
            LinearLayout outerLayout = new LinearLayout(getContext());
            TextView tvColumn = new TextView(getContext());
            ImageView picture = new ImageView(getContext());
            tvColumn.setText(plsValues[iView]);
            if (iView < getResources().getInteger(R.integer.IDX_NAME)) {
                tvColumn.setVisibility(View.INVISIBLE);
            }
            tvColumn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams textLayoutParams = new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            textLayoutParams.setMargins(2, 5, 2, 5);
            tvColumn.setLayoutParams(textLayoutParams);
            if (!pbHeader) {
                if (iView == getResources().getInteger(R.integer.IDX_NAME)) {
                    picture.setMinimumHeight(30);
                    picture.setMaxHeight(30);
                    picture.setAdjustViewBounds(true);
                    picture.setBackgroundColor(Color.parseColor(psColor));
                    LinearLayout.LayoutParams imgLayoutParams = new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    imgLayoutParams.setMargins(0, 5, 3, 5);
                    picture.setLayoutParams(imgLayoutParams);
                    if (oUser.getUser().getRole().equals(FiredroneConstante.ROLE_COS)) {
                        picture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MoyenColorDialog colorPopup = new MoyenColorDialog();
                                colorPopup.setLine(iRowIdx, psColor, (TableLayout) mView.findViewById(R.id.tableMeans));
                                colorPopup.show(getFragmentManager(), "");
                            }
                        });
                    }
                } else if (iView == getResources().getInteger(R.integer.IDX_H_CALL) && !psStatus.isEmpty()) {
                    picture.setMinimumHeight(30);
                    picture.setMaxHeight(30);
                    picture.setAdjustViewBounds(true);
                    if (MeansItemStatus.STATUS_DEMANDE.state().equals(psStatus)) {
                        picture.setImageResource(R.drawable.icon_dmd);
                    } else if (MeansItemStatus.STATUS_REFUSE.state().equals(psStatus)) {
                        picture.setImageResource(R.drawable.icon_ref);
                    } else {
                        picture.setImageResource(R.drawable.icon_val);
                    }
                    LinearLayout.LayoutParams imgLayoutParams = new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    imgLayoutParams.setMargins(0, 5, 3, 5);
                    picture.setLayoutParams(imgLayoutParams);
                }
            }
            if (pbHeader) {
                tvColumn.setBackgroundColor(Color.GRAY);
                tvColumn.setTextColor(Color.WHITE);
            } else {
                tvColumn.setBackgroundColor(Color.LTGRAY);
                if (iView == getResources().getInteger(R.integer.IDX_NAME)) {
                    tvColumn.setTextColor(Color.parseColor(psColor));
                } else {
                    tvColumn.setTextColor(Color.BLACK);
                }
            }
            tvColumn.setWidth(200);
            tvColumn.setHeight(30);
            outerLayout.addView(tvColumn);
            outerLayout.addView(picture);
            element.addView(outerLayout);
        }
        return element;
    }

    private void loadTable () {
        final String [] columnHeaders = {"", "", "Moyens", "Demandé à", "Arrivé à", "Engagé à", "Libéré à"};

        TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(columnHeaders, true, "#OOOOOO", "");
        table.addView(element);
        table.setColumnCollapsed(0, true);
        table.setColumnCollapsed(1, true);
    }

    public void addMean(String[] psHours, boolean pbSend, String psColor, String psStatus) {
        if (psColor == null || psColor.isEmpty()) {
            psColor = "#000000";
        }
        for (int idxTime = getResources().getInteger(R.integer.IDX_H_CALL); idxTime < psHours.length; idxTime++) {
            if (psHours[idxTime] != null && !psHours[idxTime].isEmpty() && psHours[idxTime].contains(" ")) {
                psHours[idxTime] = psHours[idxTime].split(" ")[1];
            }
        }

        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = addRow(psHours, false, psColor, psStatus);
        final int rowIdx = table.getChildCount();

        LinearLayout cellLayout = ((LinearLayout) (element.getChildAt(getResources().getInteger(R.integer.IDX_H_FREE))));
        TextView oLastTxt = (TextView) cellLayout.getChildAt(0);
        if (oLastTxt.getText().toString().isEmpty()
                && oUser.getUser().getRole().equals(FiredroneConstante.ROLE_COS)
                && (MeansItemStatus.STATUS_VALIDE.state().equals(psStatus)
                    || MeansItemStatus.STATUS_ARRIVE.state().equals(psStatus)
                    || MeansItemStatus.STATUS_ENGAGE.state().equals(psStatus)
                    || MeansItemStatus.STATUS_ENTRANSIT.state().equals(psStatus))) {
            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoyenEditDialog popUp = new MoyenEditDialog();
                    popUp.setMiLine(rowIdx, (TableLayout) mView.findViewById(R.id.tableMeans));
                    popUp.show(getFragmentManager(), "");
                }
            });
        }
        table.addView(element);
        table.setColumnCollapsed(0, true);
        // send to server
        if (pbSend) {
            MeansItem oNewMean = new MeansItem();
            oNewMean.setMsMeanCode(psHours[getResources().getInteger(R.integer.IDX_CODE)]);
            oNewMean.setMsMeanName(psHours[getResources().getInteger(R.integer.IDX_NAME)]);
            oNewMean.setMsMeanHCall(psHours[getResources().getInteger(R.integer.IDX_H_CALL)]);
            oNewMean.setMsMeanHArriv(psHours[getResources().getInteger(R.integer.IDX_H_ARRIV)]);
            oNewMean.setMsMeanHEngaged(psHours[getResources().getInteger(R.integer.IDX_H_ENGAGED)]);
            oNewMean.setMsMeanHFree(psHours[getResources().getInteger(R.integer.IDX_H_FREE)]);
            oNewMean.setMsColor(psColor);
            oNewMean.setStatus(MeansItemStatus.STATUS_DEMANDE.state());
            MeansItemService.addMean(oNewMean,getContext(),true);
        }
    }

    public void editMean(String psHour, int piLineIndex) {
        psHour = psHour.split(" ")[1];
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        TableRow element = (TableRow) table.getChildAt(piLineIndex);
        MeansItem oMean = new MeansItem();
        boolean bEmpty = false;
        for (int colIdx = 0; colIdx < element.getChildCount(); colIdx++) {
            LinearLayout cellLayout = ((LinearLayout) (element.getChildAt(colIdx)));
            TextView oColValue = (TextView) cellLayout.getChildAt(0);
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
                oMean.setStatus(MeansItemStatus.STATUS_DEMANDE.state());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_ARRIV)) {
                oMean.setMsMeanHArriv(oColValue.getText().toString());
                oMean.setStatus(MeansItemStatus.STATUS_ARRIVE.state());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_ENGAGED)) {
                oMean.setMsMeanHEngaged(oColValue.getText().toString());
                oMean.setStatus(MeansItemStatus.STATUS_ENGAGE.state());
            } else if (colIdx == getResources().getInteger(R.integer.IDX_H_FREE)) {
                oMean.setMsMeanHFree(oColValue.getText().toString());
                oMean.setStatus(MeansItemStatus.STATUS_LIBERE.state());
            }
            if (bEmpty) {
                colIdx = element.getChildCount() + 1;
            }
        }
        MeansItemService.editMean(oMean, getContext());
    }

    public void changeColor(String psId, String psColor) {
        MeansItem oMean = new MeansItem();
        oMean.setMsMeanId(psId);
        oMean.setMsColor(psColor);
        MeansItemService.editMean(oMean, getContext());
    }

    public void getMeans() {
        final TableLayout table = (TableLayout) this.mView.findViewById(R.id.tableMeans);
        table.removeAllViews();
        loadTable();

        if (oIntervention.getIntervention() != null) {
            List<MeansItem> loMeans = oIntervention.getIntervention().getWays();
            if (loMeans != null && loMeans.size() > 0) {
                table.removeAllViews();
                loadTable();
                for (MeansItem oMean : loMeans) {
                    String sStatus = oMean.getStatus().state();
                    String[] sHours = {oMean.getMsMeanId(),
                            oMean.getMsMeanCode(),
                            oMean.getMsMeanName(),
                            oMean.getMsMeanHCall(),
                            oMean.getMsMeanHArriv(),
                            oMean.getMsMeanHEngaged(),
                            oMean.getMsMeanHFree()};
                    addMean(sHours, false, oMean.getMsColor(), sStatus);
                }
            }
        }
    }

    @Override
    public void actualiser(Observable o) {
        MoyenFragment myFragment = (MoyenFragment) getFragmentManager().findFragmentById(R.id.content_frame);
        if (myFragment != null && myFragment.isVisible()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}