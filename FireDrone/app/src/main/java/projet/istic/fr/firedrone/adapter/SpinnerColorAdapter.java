package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import projet.istic.fr.firedrone.R;

/**
 * Created by tbernard on 24/04/16.
 */
class SpinnerColorAdapter extends BaseAdapter
{
    ArrayList<Integer> colors;
    Context context;

    public SpinnerColorAdapter(Context context)
    {
        this.context=context;
        colors=new ArrayList<Integer>();
        int retrieve []=context.getResources().getIntArray(R.array.androidcolors);
        for(int re:retrieve)
        {
            colors.add(re);
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
        txv.setText(" ");
        return view;
    }

}