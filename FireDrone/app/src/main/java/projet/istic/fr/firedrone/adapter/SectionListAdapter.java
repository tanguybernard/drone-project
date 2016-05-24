package projet.istic.fr.firedrone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.map.EnumPointType;
import projet.istic.fr.firedrone.model.MeansItem;

/**
 * Created by ramage on 23/05/16.
 */
public class SectionListAdapter extends BaseAdapter {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public SectionListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Object item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addItems(final List<MeansItem> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }


    public void removeSectionHeaderItem(String item,int position){
        mData.remove(item);
        sectionHeader.remove(position);
    }

    public void removeItem(MeansItem meansItem){
        mData.remove(meansItem);
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    public int getPosition(Object object){
       return mData.indexOf(object);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);
        Object object = mData.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.section_item, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);

                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.section_item, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);

                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(object instanceof MeansItem) {
            holder.imageView.setImageBitmap(((MeansItem) object).getBitmap());
            visibilityHolder(holder, TYPE_ITEM);
        }else if(object instanceof EnumPointType){
            holder.imageView.setImageResource(((EnumPointType) object).getResource());
            visibilityHolder(holder,TYPE_ITEM);
        }else{
            holder.textView.setText(object.toString());
            visibilityHolder(holder,TYPE_SEPARATOR);
        }

        return convertView;
    }

    private void visibilityHolder(ViewHolder holder,int type){
        switch(type){
            case TYPE_ITEM : holder.textView.setVisibility(View.INVISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                break;

            case TYPE_SEPARATOR: holder.imageView.setVisibility(View.INVISIBLE);
                holder.textView.setVisibility(View.VISIBLE);
                holder.textView.setBackgroundColor(Color.BLACK);
                break;
        }
    }

    public static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
