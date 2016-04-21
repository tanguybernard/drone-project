package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.InterventionItem;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;

/**
 * Created by tbernard on 18/04/16.
 */
public class MoyenListAdapter extends ArrayAdapter<MoyenInterventionItem> {
    private ArrayList<InterventionItem> listData;
    private LayoutInflater layoutInflater;

    public MoyenListAdapter(Context aContext, List<MoyenInterventionItem> listData) {
        super(aContext,0,listData);
        //layoutInflater = LayoutInflater.from(aContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MoyenInterventionItem item = getItem(position);
        System.out.println("toto");System.out.println(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.moyen_item, null);
            holder = new ViewHolder();
            holder.myName = (TextView) convertView.findViewById(R.id.moyen_name);
            holder.quantity = (TextView) convertView.findViewById(R.id.moyen_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Button subQuantity = (Button) convertView.findViewById(R.id.btn_q_sub);
        final Button addQuantity = (Button) convertView.findViewById(R.id.btn_q_add);



        subQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyQuantity(v,false);
            }
        });

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyQuantity(v,true);
            }
        });



        System.out.println(item.getName());
        holder.myName.setText(item.getName());
        holder.quantity.setText(String.valueOf(item.getQuantity()), TextView.BufferType.EDITABLE);


        return convertView;
    }



    /**
     * Decrease quantity of vehicle
     * @param v
     */
    public void modifyQuantity(View v,boolean addSub){

        View parentRow = (View) v.getParent();

        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        View vv = listView.getChildAt(position);

        TextView tt = (TextView)vv.findViewById(R.id.moyen_quantity);
        int quantity = Integer.parseInt(tt.getText().toString());


        if(addSub){
            tt.setText(Integer.toString(quantity + 1));

        }
        else{
            if(quantity>0) {
                tt.setText(Integer.toString(quantity - 1));
            }

        }

    }







    static class ViewHolder {
        TextView myName;
        TextView quantity;

    }
}