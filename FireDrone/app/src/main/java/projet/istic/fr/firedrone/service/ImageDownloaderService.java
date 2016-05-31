package projet.istic.fr.firedrone.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tbernard on 27/05/16.
 */

public class ImageDownloaderService extends AsyncTask<String, Integer, Bitmap>{

    private ImageView bmImage;
    HttpURLConnection httpCon;

    public ImageDownloaderService(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute(){
        //Setup is done here
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try{
            URL url = new URL(params[0]);
            httpCon =
                    (HttpURLConnection) url.openConnection();

            if(httpCon.getResponseCode() != 200)
                throw new Exception("Failed to connect");

            InputStream is = httpCon.getInputStream();
            return BitmapFactory.decodeStream(is);

        }catch(Exception e){
            Log.e("Image","Failed to load image",e);
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... params){
        //Update a progress bar here, or ignore it, it's up to you
    }
    @Override
    protected void onPostExecute(Bitmap img){
        if(bmImage!=null && img!=null){
            bmImage.setImageBitmap(img);
            //currentItem.setImageUrl();

        }
        httpCon.disconnect();
    }

    @Override
    protected void onCancelled(){
        // Handle what you want to do if you cancel this task
    }
}