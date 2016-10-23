package com.android.habitapp.addHabit;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.habitapp.R;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HabitSettingActivity extends AppCompatActivity {

    //region Variables
    private String habit_id, habit_name, habit_desciption, habit_users;
    //endregion

    //region Views
    private Toolbar toolbar;
    private TextView tv_habitdec;
    private EditText et_habit_desc, et_date, et_time;
    private Button btn;
    private ActionBar actionBar;
    //endregion

    //region Lifecycle_and_Android_Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_setting);

        Bundle bundle = this.getIntent().getExtras();
        String id = bundle.getString("rowId");
        setUpView();
        loadData(id);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

//endregion

    //region setViews
    private void setUpView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tv_habitdec = (TextView) findViewById(R.id.tv_habitdec);
        et_habit_desc = (EditText) findViewById(R.id.et_habit_desc);
        et_date = (EditText) findViewById(R.id.et_date);
        et_time = (EditText) findViewById(R.id.et_time);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(et_time);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        et_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new TimePickerFragment(et_time);
                    newFragment.show(getSupportFragmentManager(), "timePicker");

                }
            }
        });
    }


    //endregion

    //region LocalMethods_and_classes
    private void loadData(String id) {
        String[] projection = {
                HabitDb.HABIT_ID,
                HabitDb.HABIT_NAME,
                HabitDb.HABIT_DESCIPTION,
                HabitDb.HABIT_USERS};

        Uri uri = Uri.parse(HabitContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            // String habit_sr_no = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_SR_NO));
            habit_id = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_ID));
            habit_name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_NAME));
            habit_desciption = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_DESCIPTION));
            habit_users = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_USERS));


            actionBar.setTitle(habit_name);
            tv_habitdec.setText(habit_desciption);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        EditText et_time;

        public TimePickerFragment(EditText et_time) {
            this.et_time = et_time;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time, convention;
            int hrs, min;
            String hrsS, minS;

            if (hourOfDay > 12) {
                hrs = hourOfDay - 12;
                convention = "PM";
            } else {
                hrs = hourOfDay;
                convention = "AM";
            }
            hrsS = String.valueOf((hrs < 10) ? "0" + hrs : hrs);
            minS = String.valueOf((minute < 10) ? "0" + minute : minute);
            et_time.setText(hrsS + ":" + minS + " " + convention);
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //endregion
}
