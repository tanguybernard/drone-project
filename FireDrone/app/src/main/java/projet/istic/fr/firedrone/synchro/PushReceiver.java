package projet.istic.fr.firedrone.synchro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;


import java.util.ArrayList;

import projet.istic.fr.firedrone.FiredroneConstante;
import projet.istic.fr.firedrone.MainActivity;
import projet.istic.fr.firedrone.ModelAPI.InterventionAPI;
import projet.istic.fr.firedrone.R;
import projet.istic.fr.firedrone.model.Intervention;
import projet.istic.fr.firedrone.singleton.InterventionSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tbernard on 27/04/16.
 */
public class PushReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String notificationTitle = "Pushy";
        String myMessage = "Test notification";

        // Attempt to grab the message property from the payload
        if ( intent.getStringExtra("message") != null )
        {
            myMessage = intent.getStringExtra("message");
        }


        /*InterventionSingleton.getInstance().getIntervention().getId()
        if(myMessage.equals()){

            notifyApp(context);

        }
        else{

        }*/


        notifyApp(context);//bouchon

    }


    public void notifyApp(Context context) {


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle("Mise à Jour Intervention")
                        //.setDefaults(Notification.DEFAULT_VIBRATE)

                        .setAutoCancel(true)

                        .setContentText("Synchronisation ...");
        int NOTIFICATION_ID = 12345;


        Intent targetIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(FiredroneConstante.END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        InterventionAPI interventionAPI = restAdapter.create(InterventionAPI.class);

        if (InterventionSingleton.getInstance().getIntervention() != null) {

            interventionAPI.getInterventionById(
                    InterventionSingleton.getInstance().getIntervention().getId(), new Callback<Intervention>() {

                        @Override
                        public void success(Intervention intervention, Response response) {

                            InterventionSingleton.getInstance().setIntervention(intervention);
                            MyObservable.getInstance().notifierObservateurs();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
        }

    }





}
