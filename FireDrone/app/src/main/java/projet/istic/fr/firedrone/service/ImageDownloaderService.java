package projet.istic.fr.firedrone.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FilterInputStream;
import java.io.IOException;
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

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;

            return BitmapFactory.decodeStream(new FlushedInputStream(is), null, options);

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


    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}