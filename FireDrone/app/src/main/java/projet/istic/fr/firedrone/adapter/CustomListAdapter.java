package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FicheFragment;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.Intervention;

/**
 * Created by tbernard on 18/04/16.
 */
public class CustomListAdapter extends ArrayAdapter<Intervention> {
    private ArrayList<Intervention> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, List<Intervention> listData) {
        super(aContext,0,listData);
        //layoutInflater = LayoutInflater.from(aContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Intervention item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.intervention_infos, null);
            holder = new ViewHolder();
            holder.myId = (TextView) convertView.findViewById(R.id.myId);
            holder.codeSinistre = (TextView) convertView.findViewById(R.id.codeSinistre);
            holder.adress = (TextView) convertView.findViewById(R.id.adress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.myId.setText(item.getId());
        //holder.codeSinistre.setText("By, " + listData.get(position).getCodeSinistre());
        holder.codeSinistre.setText(item.getSinisterCode());
        holder.adress.setText(item.getAddress());
        return convertView;
    }

    static class ViewHolder {
        TextView myId;
        TextView codeSinistre;
        TextView adress;
    }
}