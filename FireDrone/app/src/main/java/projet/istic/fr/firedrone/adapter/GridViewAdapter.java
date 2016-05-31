package projet.istic.fr.firedrone.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.ImageItem;
import projet.istic.fr.firedrone.service.ImageDownloaderService;

/**
 * Created by tbernard on 26/05/16.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<ImageItem> data = new ArrayList();
    private ImageItem currentItem;

    public GridViewAdapter(Context context, int layoutResourceId, List<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        currentItem = (ImageItem) data.get(position);
        holder.imageTitle.setText(currentItem.getDate());

        //Request image
        new ImageDownloaderService(holder.image).execute(currentItem.getImageUrl());

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }




}
