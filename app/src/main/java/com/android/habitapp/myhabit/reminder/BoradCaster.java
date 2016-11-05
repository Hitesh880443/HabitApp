package com.android.habitapp.myhabit.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.android.habitapp.MainActivity;
import com.android.habitapp.R;

/**
 * Created by Hitesh on 10/22/2016.
 */
public class BoradCaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent rIntent = new Intent(context, MainActivity.class);
        rIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, rIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.next)
                .setContentTitle("Hi im hitesh")
                .setContentText("wow")
                .setAutoCancel(true);

        notificationmanager.notify(100, builder.build());


    }
}
