package com.android.habitapp.addHabit;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.CalendarView;
import com.android.habitapp.extra.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyHabitDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //region Vairables

    private static final int LOADER_SEARCH_RESULTS = 3;
    private String habit_id, habit_name, habit_reason, habit_date, habit_sr_no, habit_time, habit_days_comp;
    private ActionBar actionBar;
    public String TAG = "Calnder";
    private Context context;
    Date todatsDate, startdate;
    HashSet<Date> events;

    //endregion

    //region Views

    private Toolbar toolbar;
    public CalendarView calendar_view;
    public TextView tv_reason, tv_startDate, tv_reminder, tv_days, tv_progress;

    //endregion

    //region Lifecyle and Acitivity methods


    public MyHabitDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);

        try {
            context = this;
            setUpView();
            Bundle bundle = this.getIntent().getExtras();
            habit_sr_no = bundle.getString("rowId");
            loadData(habit_sr_no);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
                case R.id.delete:
                    deleteHabit(habit_sr_no);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.individual_habit, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //endregion

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
        events = new HashSet<>();
        /*Date today = new Date();
        events.add(today);
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        events.add(tomorrow);*/

        calendar_view.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MyHabitDetailActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void setEvents() {
                calendar_view.updateCalendar(events);
            }
        });


    }


    //endregion

    //region LocalMeothos
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


                String str = null;


                startdate = inputFormat.parse(habit_date);
                str = outputFormat.format(startdate);

                Log.i("mini", "Converted Date Today:" + str);

                tv_reason.setText(habit_reason);
                tv_startDate.setText(str);

                Log.d("AAAA", habit_name);
                String[] toppings = new String[2];
                toppings = habit_time.split(":");

                int savedHr = Integer.parseInt(toppings[0]);
                int savedMin = Integer.parseInt(toppings[1]);


                String convention = null;
                if (savedHr > 12) {
                    savedHr = savedHr - 12;
                    convention = "PM";
                } else {
                    convention = "AM";
                }
                String latest_habit_time = String.valueOf((savedHr < 10) ? "0" + savedHr : savedHr) + ":" + String.valueOf((savedMin < 10) ? "0" + savedMin : savedMin) + " " + convention;

                tv_reminder.setText(latest_habit_time);
                restratLoader();

            }


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void dailyData(Cursor cursor) {
        try {
            if (cursor != null) {

                tv_days.setText(cursor.getCount() + " days");
                try {
                    long diff = Math.round((new Date().getTime() - startdate.getTime()) / (double) 86400000);
                    float percentage = (cursor.getCount() / diff) * 100;
                    if (percentage > 50) {
                        tv_progress.setText(context.getResources().getString(R.string.peroforamance_good));
                        tv_progress.setTextColor(context.getResources().getColor(R.color.green));

                    } else {
                        tv_progress.setText(context.getResources().getString(R.string.peroforamance_bad));
                        tv_progress.setTextColor(context.getResources().getColor(R.color.red));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                while (cursor.moveToNext()) {
                    final String dateString = cursor.getString(cursor.getColumnIndex(HabitDb.MY_DAILY__DATE));
                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = (Date) formatter.parse(dateString);
                    events.add(date);

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // cursor.close();
            calendar_view.updateCalendar(events);
        }
    }

    public void restratLoader() {
        getSupportLoaderManager().initLoader(LOADER_SEARCH_RESULTS, null, this);
    }

    public void deleteHabit(final String id) {

        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyHabitDetailActivity.this);
            alertDialog.setTitle("Confirm Delete...");
            alertDialog.setMessage("Are you sure you want delete " + habit_name + " habit ?");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(HabitContentProvider.CONTENT_URI2 + "/" + id);
                    long result = getContentResolver().delete(uri, null, null);
                    Log.d("Result", String.valueOf(result));

                    Uri uri2 = Uri.parse(HabitContentProvider.CONTENT_URI4 + "/" + id);
                    long result2 = getContentResolver().delete(uri, null, null);
                    Log.d("Result", String.valueOf(result));
                    Toast.makeText(getApplicationContext(), habit_name + " deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //endregion

    //region Loader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SEARCH_RESULTS:

                final Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI4 + "/" + habit_sr_no));
                return new CursorLoader(context, uri, null, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                dailyData(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                dailyData(null);
                break;
        }
    }

    //endregion
}
