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
import projet.istic.fr.firedrone.model.InterventionItem;

/**
 * Created by tbernard on 18/04/16.
 */
public class CustomListAdapter extends ArrayAdapter<InterventionItem> {
    private ArrayList<InterventionItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, List<InterventionItem> listData) {
        super(aContext,0,listData);
        //layoutInflater = LayoutInflater.from(aContext);
    }








    public View getView(int position, View convertView, ViewGroup parent) {

        InterventionItem item = getItem(position);
        System.out.println("toto");System.out.println(position);
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


        System.out.println(item.getCodeSinistre());
        holder.myId.setText(item.getMyId());
        //holder.codeSinistre.setText("By, " + listData.get(position).getCodeSinistre());
        holder.codeSinistre.setText(item.getCodeSinistre());
        holder.adress.setText(item.getAdress());
        return convertView;
    }

    static class ViewHolder {
        TextView myId;
        TextView codeSinistre;
        TextView adress;
    }
}