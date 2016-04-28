package projet.istic.fr.firedrone.synchro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import projet.istic.fr.firedrone.MainActivity;
import projet.istic.fr.firedrone.R;

/**
 * Created by tbernard on 27/04/16.
 */
public class PushReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String notificationTitle = "Pushy";
        String notificationDesc = "Test notification";

        // Attempt to grab the message property from the payload
        if ( intent.getStringExtra("message") != null )
        {
            notificationDesc = intent.getStringExtra("message");
        }

        System.out.println(notificationDesc);



        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle("Mise à Jour Intervention")
                        .setContentText("Synchronisation ...");
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());






    }
}
