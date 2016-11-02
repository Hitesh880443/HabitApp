package com.android.habitapp.addHabit;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;
import com.android.habitapp.extra.CalendarView;
import com.android.habitapp.extra.Utils;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class CalenderViewAct extends AppCompatActivity {
    private String habit_id, habit_name, habit_desciption, habit_date, id, habit_time, habit_days_comp;
    private ActionBar actionBar;
    private Toolbar toolbar;
    public String TAG="Calnder";
    public CalendarView calendar_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);
        setUpView();

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("rowId");
        loadData(id);
    }

    private void setUpView() {

        calendar_view = (CalendarView) findViewById(R.id.calendar_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        final HashSet<Date> events = new HashSet<>();
        events.add(new Date());



        calendar_view.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
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

    private void loadData(String id) {
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
            // String habit_sr_no = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_SR_NO));
            habit_id = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_ID));
            habit_name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_NAME));
            habit_desciption = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REASON));
            habit_date = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_START_DATE));
            habit_time = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_REMINDER_TIME));
            habit_days_comp = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_DAYS_COMPLETED));
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
            Date dateV = null;
            String newDateStr = null, newTimeStr = null;
            try {
                dateV = form.parse(habit_date);
              /*  Event ev1 = new Event(Color.GREEN, dateV.getTime(), "Some extra data that I want to store.");
                compactcalendar_view.addEvent(ev1);

                // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
                Event ev2 = new Event(Color.GREEN, new Date().getTime());
                compactcalendar_view.addEvent(ev2);*/
            } catch (Exception e) {
                e.printStackTrace();
            }

            actionBar.setTitle(Utils.setFirstltrCapitcal(habit_name));
/*
            compactcalendar_view.setListener(new CompactCalendarView.CompactCalendarViewListener() {


                @Override
                public void onDayClick(Date dateClicked) {
                    List<Event> events = compactcalendar_view.getEvents(dateClicked);
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                }
            });
*/
        }
    }

    private class DisabledColorDecorator implements DayDecorator {

        @Override
        public void decorate(DayView dayView) {
           // if (CalendarUtils.(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
           // }

        }
    }

}
