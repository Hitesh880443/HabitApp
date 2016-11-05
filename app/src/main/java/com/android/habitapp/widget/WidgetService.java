package com.android.habitapp.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.habitapp.R;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hitesh on 11/5/2016.
 */

public class WidgetService extends Service {

    Context mContext;
    private String convention = null;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildUpdate();

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate() {
        try {
            String lastUpdated = DateFormat.format("MMMM dd, yyyy h:mmaa", new Date()).toString();
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.new_app_widget);
            String habit_id, habit_name = "Hitesh", habit_reason, habit_date, habit_sr_no, habit_time, habit_days_comp;
            String lalest_habit_name = mContext.getResources().getString(R.string.appwidget_text), latest_habit_time = null;
            long minDiff = 86400000;
            int h, m;
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            CharSequence widgetText = mContext.getString(R.string.appwidget_text);
            // Construct the RemoteViews object
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
            Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null,
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

                    int savedHr = Integer.parseInt(toppings[0]);
                    int savedMin = Integer.parseInt(toppings[1]);

                    final Calendar c_single = Calendar.getInstance();
                    c_single.set(Calendar.HOUR_OF_DAY, savedHr);
                    c_single.set(Calendar.MINUTE, savedMin);

                    long diff = c_single.getTimeInMillis() - c.getTimeInMillis();
                    if (diff > 0 && diff < minDiff) {
                        minDiff = diff;
                        lalest_habit_name = habit_name;

                        if (savedHr > 12) {
                            savedHr = savedHr - 12;
                            convention = "PM";
                        } else {
                            convention = "AM";
                        }
                        latest_habit_time = String.valueOf((savedHr < 10) ? "0" + savedHr : savedHr) + ":" + String.valueOf((savedMin < 10) ? "0" + savedMin : savedMin) + " " + convention;
                    }
                }
            }

            view.setTextViewText(R.id.habit_name, lalest_habit_name);
            view.setTextViewText(R.id.habit_time, latest_habit_time);
            ComponentName thisWidget = new ComponentName(this, HabitAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, view);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
