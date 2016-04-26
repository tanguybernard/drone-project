package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.MoyenInterventionItem;

/**
 * Created by tbernard on 24/04/16.
 */
class SpinnerColorAdapter extends BaseAdapter
{
    List<Integer> colors;
    List<String> names;
    Context context;
    List<MoyenInterventionItem> listData;

    public SpinnerColorAdapter(Context context, List<MoyenInterventionItem> listData)
    {
        this.context=context;
        colors=new ArrayList<Integer>();
        names=new ArrayList<String>();

        int retrieve []=context.getResources().getIntArray(R.array.androidcolors);
        for(int re:retrieve)
        {
            colors.add(re);
        }
        String retrieve2 []=context.getResources().getStringArray(R.array.androidDescription);
        for(String re:retrieve2)
        {
            names.add(re);
        }
    }
    @Override
    public int getCount()
    {
        return colors.size();
    }
    @Override
    public Object getItem(int arg0)
    {
        return colors.get(arg0);
    }
    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
    @Override
    public View getView(int pos, View view, ViewGroup parent)
    {

        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv=(TextView)view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(colors.get(pos));
        txv.setTextSize(20f);

        txv.setText(names.get(pos));
        return view;
    }

}