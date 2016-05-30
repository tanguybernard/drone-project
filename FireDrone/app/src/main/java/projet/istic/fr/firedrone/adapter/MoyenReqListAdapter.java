package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.ModelAPI.MeansAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MeansItem;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;
import projet.istic.fr.firedrone.service.ColorNameService;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by tbernard on 18/04/16.
 */
public class MoyenReqListAdapter extends ArrayAdapter<MeansItem> {
    private List<MeansItem> listData;
    private LayoutInflater layoutInflater;
    private HashMap hashIdIntervention;
    private HashMap hashAddress;

    public MoyenReqListAdapter(Context aContext, List<MeansItem> listData,HashMap hashIdIntervention,HashMap hashAddress) {
        super(aContext,0,listData);
        this.listData =listData;
        this.hashAddress = hashAddress;
        this.hashIdIntervention = hashIdIntervention;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MeansItem item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.moyen_request_item, null);
            holder = new ViewHolder();
            holder.myName = (TextView) convertView.findViewById(R.id.moyen_name2);
            holder.myInterventionId = (TextView) convertView.findViewById(R.id.moyen_inervention_id);
            holder.myInterventionAddress = (TextView) convertView.findViewById(R.id.moyen_inervention_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Button refuseButton = (Button) convertView.findViewById(R.id.btn_req_refuse);
        final Button validButton = (Button) convertView.findViewById(R.id.btn_req_valid);




        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validWay(v,false);
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validWay(v,true);
            }
        });


        holder.myName.setText(item.getMsMeanName());
        holder.myInterventionAddress.setText((CharSequence) hashAddress.get(item.getMsMeanId()));
        holder.myInterventionId.setText((CharSequence) hashIdIntervention.get(item.getMsMeanId()));



        return convertView;
    }



    /**
     * Decrease quantity of vehicle
     * @param v
     */
    public void validWay(View v, Boolean valid) {

        View parentRow = (View) v.getParent();

        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        View vv = listView.getChildAt(position);

        Object o = listView.getItemAtPosition(position);
        MeansItem meansItem = (MeansItem) o;

        if(vv!=null) {
            TextView tt = (TextView) vv.findViewById(R.id.moyen_name2);
            if(meansItem!=null) {
                if (valid) {

                    editStatus("V",meansItem);

                } else {
                    editStatus("R",meansItem);
                }
            }
        }
    }


    public void editStatus(String status, MeansItem meansItem){

        String sIntervId = InterventionSingleton.getInstance().getIntervention().id;
        meansItem.setStatus(status);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient())
                .build();
        MeansAPI meansApi = restAdapter.create(MeansAPI.class);

        meansApi.EditMean(sIntervId, meansItem, new Callback<List<MeansItem>>() {
            @Override
            public void success(List<MeansItem> ploMeans, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    static class ViewHolder {
        TextView myName;
        TextView myInterventionId;
        TextView myInterventionAddress;
    }
}