package com.android.habitapp.myhabit.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.android.habitapp.MainActivity;
import com.android.habitapp.R;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;

import java.util.Calendar;

/**
 * Created by Hitesh on 10/23/2016.
 */

public class NotificationIntentService2 extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private String habit_id, habit_name, habit_reason, habit_date, habit_sr_no, habit_time, habit_days_comp;

    IBinder mBinder;
   /* public NotificationIntentService2() {
        super(NotificationIntentService2.class.getSimpleName());
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
        return START_STICKY;
    }

    public static Intent createIntentStartNotificationService(Context context) {


        Intent intent = new Intent(context, NotificationIntentService2.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService2.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }


    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        try {
            String[] projection = {
                    HabitDb.MY_HABIT_SR_NO,
                    HabitDb.MY_HABIT_ID,
                    HabitDb.MY_HABIT_NAME,
                    HabitDb.MY_HABIT_REASON,
                    HabitDb.MY_HABIT_REMINDER_TIME,
                    HabitDb.MY_HABIT_START_DATE,
                    HabitDb.MY_HABIT_REMINDER_STATUS,
                    HabitDb.MY_HABIT_DAYS_COMPLETED};

            Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI2));
            Cursor cursor = getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    habit_sr_no = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_SR_NO));
                    habit_id = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_ID));
                    habit_name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_NAME));
                    habit_reason = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REASON));
                    habit_date = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_START_DATE));
                    habit_time = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REMINDER_TIME));
                    habit_days_comp = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_DAYS_COMPLETED));
                    Log.d("AAAA", habit_name);
                    String[] toppings = new String[2];
                    toppings = habit_time.split(":");
                    Log.d("AAAA time",hour+":"+minute+" -- "+habit_id+" "+Integer.parseInt(toppings[0])+":"+Integer.parseInt(toppings[1]));

                    if (hour == Integer.parseInt(toppings[0]) && minute == Integer.parseInt(toppings[1])) {
                        Log.d("AAAA","passs");
                        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentTitle("Reminder : "+habit_name)
                                .setAutoCancel(true)
                                .setColor(getResources().getColor(R.color.colorAccent))
                                .setContentText(habit_reason)
                                .setSmallIcon(R.drawable.next);


                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(alarmSound);

                        Intent mainIntent = new Intent(this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                Integer.parseInt(habit_sr_no),
                                mainIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                        //  builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

                        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(Integer.parseInt(habit_sr_no), builder.build());
                    }
                    else{
                        Log.d("AAAA","failed");
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent("android.intent.action.BOOT_COMPLETED");
        sendBroadcast(broadcastIntent);


    }
}
