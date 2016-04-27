package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.MeansItem;

/**
 * Created by ramage on 20/04/16.
 */
public class MoyenMapPanelListAdapter extends ArrayAdapter<MeansItem> {

    public MoyenMapPanelListAdapter(Context context, List<MeansItem> moyens) {
        super(context, 0, moyens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MeansItem moyen = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_map_list_moyen, parent, false);
        }
        // Lookup view for data population
        ImageView image = (ImageView) convertView.findViewById(R.id.item_map_moyen);
        // Populate the data into the template view using the data object

        //image.setImageResource(moyen.getResource());
        image.setImageBitmap(moyen.getBitmap());

        // Return the completed view to render on screen
        return convertView;
    }
}
