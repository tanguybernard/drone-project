package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.map.EnumPointType;

/**
 * Created by ramage on 25/04/16.
 */
public class PointListAdapter extends BaseAdapter{

    private Context context;
    private List<EnumPointType> points = new ArrayList<>();

    public PointListAdapter(Context context){
        this.context = context;
        points.addAll(Arrays.asList(EnumPointType.values()));
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return points.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_map_list_moyen, null);
        ImageView imageView=(ImageView)view.findViewById(R.id.item_map_moyen);
        imageView.setImageResource(points.get(position).getResource());
        return view;
    }
}
