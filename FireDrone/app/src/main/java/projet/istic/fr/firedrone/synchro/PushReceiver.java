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
    public void onReceive(Context context, Intent intent) {
        String myMessage = "Test notification";

        String cosIam = null;
        String cosFree = null;
        String idIntervention = null;

        // Attempt to grab the message property from the payload
        if (intent.getStringExtra("message") != null) {
            myMessage = intent.getStringExtra("message");
        }
        if (intent.getStringExtra("cosIam") != null) {
            cosIam = intent.getStringExtra("cosIam");
        }
        if (intent.getStringExtra("cosFree") != null) {
            cosFree = intent.getStringExtra("cosFree");
        }
        if (intent.getStringExtra("idIntervention") != null) {
            idIntervention = intent.getStringExtra("idIntervention");
        }

        Intervention inter = InterventionSingleton.getInstance().getIntervention();
        if (inter != null) {
            if (idIntervention.equals(inter.getId())) {
                if (cosFree != null) {
                    notifyApp(context, "Liberation COS : " + cosFree);
                } else if (cosIam != null) {
                    notifyApp(context, "Affectation COS : " + cosIam);
                } else {
                    notifyApp(context, "Mise à Jour Intervention");
                }
            }
        }
        //notifyApp(context,"Mise à Jour Intervention");//bouchon
    }




    public void notifyApp(final Context context,String message) {

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle(message)
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

                            InterventionSingleton.getInstance().setIntervention(intervention);//maj data intervention
                            MyObservable.getInstance().notifierObservateurs();//notify
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            FiredroneConstante.getToastError(context).show();
                        }
                    });
        }

    }





}
