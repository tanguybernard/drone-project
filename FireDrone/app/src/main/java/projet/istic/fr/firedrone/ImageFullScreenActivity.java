package projet.istic.fr.firedrone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import projet.istic.fr.firedrone.service.ImageDownloaderService;

/**
 * Created by tbernard on 26/05/16.
 */
public class ImageFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullscreen);

        String title = getIntent().getStringExtra("date");
        System.out.println(title);
        String urlImage = getIntent().getStringExtra("imageUrl");
        System.out.println(urlImage);


        TextView titleTextView = (TextView) findViewById(R.id.title);
        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        ImageView imageView = (ImageView) findViewById(R.id.image);
        System.out.println(urlImage);




        if (imageView != null) {

            new ImageDownloaderService(imageView).execute(urlImage);
        }
    }




}
