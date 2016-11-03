package com.android.habitapp.addHabit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;
import com.android.habitapp.extra.CalendarView;
import com.android.habitapp.extra.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalenderViewAct extends AppCompatActivity {
    private String habit_id, habit_name, habit_reason, habit_date, id, habit_time, habit_days_comp;
    private ActionBar actionBar;
    private Toolbar toolbar;
    public String TAG = "Calnder";
    public CalendarView calendar_view;
    public TextView tv_reason, tv_startDate, tv_reminder, tv_days, tv_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);
        setUpView();

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("rowId");
        loadData(id);
    }


    //region setUpView
    private void setUpView() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tv_reason = (TextView) findViewById(R.id.tv_reason);
        tv_startDate = (TextView) findViewById(R.id.tv_startDate);
        tv_reminder = (TextView) findViewById(R.id.tv_reminder);
        tv_days = (TextView) findViewById(R.id.tv_days);
        tv_progress = (TextView) findViewById(R.id.tv_progress);

        calendar_view = (CalendarView) findViewById(R.id.calendar_view);
        final HashSet<Date> events = new HashSet<>();
        Date today = new Date();
        events.add(today);
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        events.add(tomorrow);
        calendar_view.updateCalendar(events);
        calendar_view.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(CalenderViewAct.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void setEvents() {
                calendar_view.updateCalendar(events);
            }
        });


    }

    //endregion

    private void loadData(String id) {

        try {
            String[] projection = {
                    HabitDb.MY_HABIT_ID,
                    HabitDb.MY_HABIT_NAME,
                    HabitDb.MY_HABIT_REASON,
                    HabitDb.MY_HABIT_REMINDER_TIME,
                    HabitDb.MY_HABIT_START_DATE,
                    HabitDb.MY_HABIT_REMINDER_STATUS,
                    HabitDb.MY_HABIT_DAYS_COMPLETED};

            Uri uri = Uri.parse(HabitContentProvider.CONTENT_URI2 + "/" + id);
            Cursor cursor = getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                habit_id = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_ID));
                habit_name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_NAME));
                habit_reason = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REASON));
                habit_date = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_START_DATE));
                habit_time = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REMINDER_TIME));
                habit_days_comp = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_DAYS_COMPLETED));
                actionBar.setTitle(Utils.setFirstltrCapitcal(habit_name));

                String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";

                String outputPattern = "dd-MM-yyyy";

                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date = null;
                String str = null;


                    date = inputFormat.parse(habit_date);
                    str = outputFormat.format(date);

                    Log.i("mini", "Converted Date Today:" + str);

                tv_reason.setText(habit_reason);
                tv_startDate.setText(str);
                tv_reminder.setText(habit_time);
                tv_days.setText(habit_days_comp);
                // tv_progress.setText(habit_days_comp);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
