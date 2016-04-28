package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;
import projet.istic.fr.firedrone.service.ColorNameService;

/**
 * Created by tbernard on 18/04/16.
 */
public class MoyenListAdapter extends ArrayAdapter<MoyenInterventionItem> {
    private List<MoyenInterventionItem> listData;
    private LayoutInflater layoutInflater;

    public MoyenListAdapter(Context aContext, List<MoyenInterventionItem> listData) {
        super(aContext,0,listData);
        this.listData =listData;
        //layoutInflater = LayoutInflater.from(aContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MoyenInterventionItem item = getItem(position);
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


        final Spinner spinner = (Spinner) convertView.findViewById(R.id.colorMeanSpinner);

        //set EAU, INCENDIE, SAP into spinner
        spinner.setAdapter(new SpinnerColorAdapter(this.getContext(), listData));

            String myString = ColorNameService.getTypeInterventionByColor(item.getColor());

            //Selectionne un role de moyen EAU,INCENDIE, SAP
            for(int i = 0; i < spinner.getCount(); i++) {
                System.out.println(spinner.getItemAtPosition(i).toString());
                if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                    spinner.setSelection(i);
                    break;
                }
            }


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

        MoyenInterventionItem item = getItem(position);

        if(addSub){
            item.setQuantity(item.getQuantity()+1);

        }
        else{
            if(item.getQuantity()>0) {
                item.setQuantity(item.getQuantity()-1);
            }

        }

    }







    static class ViewHolder {
        TextView myName;
        TextView quantity;

    }
}