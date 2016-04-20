package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.FicheFragment;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.InterventionItem;
import projet.istic.fr.firedrone.model.MoyenItem;

/**
 * Created by tbernard on 18/04/16.
 */
public class MoyenListAdapter extends ArrayAdapter<MoyenItem> {
    private ArrayList<InterventionItem> listData;
    private LayoutInflater layoutInflater;

    public MoyenListAdapter(Context aContext, List<MoyenItem> listData) {
        super(aContext,0,listData);
        //layoutInflater = LayoutInflater.from(aContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MoyenItem item = getItem(position);
        System.out.println("toto");System.out.println(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.moyen_item, null);
            holder = new ViewHolder();
            holder.myName = (TextView) convertView.findViewById(R.id.moyen_name);
            holder.quantity = (EditText) convertView.findViewById(R.id.moyen_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        System.out.println(item.getName());
        holder.myName.setText(item.getName());
        //holder.quantity.setText(item.getQuantity());


        return convertView;
    }

    static class ViewHolder {
        TextView myName;
        EditText quantity;

    }
}