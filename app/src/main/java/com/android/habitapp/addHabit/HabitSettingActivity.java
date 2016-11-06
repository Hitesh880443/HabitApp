package com.android.habitapp.addHabit;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;

import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HabitSettingActivity extends AppCompatActivity {

    //region Variables
    private String habit_id, habit_name, habit_desciption, habit_reson, id, habitType, reminderTime;
    private String globalTime = null;
    private int hrsOfDay, minOfDay;
    private Context mContext;
    DialogFragment newFragment;
    //endregion

    //region Views
    private Toolbar toolbar;
    private TextView tv_habitdec;
    private EditText et_habit_desc, et_habit, et_date, et_time;
    private Button btn;
    private ActionBar actionBar;
    private LinearLayout ll_own_habit;
    private String habit_users;
    //endregion

    //region Lifecycle_and_Android_Methods


    public HabitSettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_setting);

        try {
            mContext = this;
            Bundle bundle = this.getIntent().getExtras();

            habitType = bundle.getString("habitType");
            if (habitType.equalsIgnoreCase("store")) {
                id = bundle.getString("rowId");
            }
            setUpView();
            if (habitType.equalsIgnoreCase("store")) {
                loadData(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home)
                finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

//endregion

    //region setViews
    private void setUpView() {

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            tv_habitdec = (TextView) findViewById(R.id.tv_habitdec);
            et_habit_desc = (EditText) findViewById(R.id.et_habit_desc);
            et_habit = (EditText) findViewById(R.id.et_habit);
            et_date = (EditText) findViewById(R.id.et_date);
            et_time = (EditText) findViewById(R.id.et_time);
            btn = (Button) findViewById(R.id.button);
            ll_own_habit = (LinearLayout) findViewById(R.id.ll_own_habit);
            if (habitType.equalsIgnoreCase("store")) {
                ll_own_habit.setVisibility(View.GONE);
                tv_habitdec.setVisibility(View.VISIBLE);
            } else {
                ll_own_habit.setVisibility(View.VISIBLE);
                tv_habitdec.setVisibility(View.GONE);
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (!habitType.equalsIgnoreCase("store")) {
                            habit_name = et_habit.getText().toString();
                            habit_id = "-1";
                        }
                        habit_reson = et_habit_desc.getText().toString();
                        reminderTime = et_time.getText().toString();

                        if (!TextUtils.isEmpty(habit_name) && !TextUtils.isEmpty(habit_reson) && !TextUtils.isEmpty(et_time.getText().toString())) {


                            ContentValues values = new ContentValues();
                            values.put(HabitDb.MY_HABIT_ID, habit_id);
                            values.put(HabitDb.MY_HABIT_NAME, habit_name);
                            values.put(HabitDb.MY_HABIT_REASON, habit_reson);
                            values.put(HabitDb.MY_HABIT_DAYS_COMPLETED, 0);
                            values.put(HabitDb.MY_HABIT_START_DATE, String.valueOf(new Date()));
                            values.put(HabitDb.MY_HABIT_REMINDER_TIME, hrsOfDay + ":" + minOfDay);
                            values.put(HabitDb.MY_HABIT_REMINDER_STATUS, "Y");
                            values.put(HabitDb.MY_HABIT_TODAY_STATUS, 0);
                            Log.d("result", String.valueOf(getContentResolver().insert(HabitContentProvider.CONTENT_URI2, values)));
                            Toast.makeText(HabitSettingActivity.this, "Done", Toast.LENGTH_SHORT).show();


                            // NotificationEventReceiver.setupAlarm(getApplicationContext(),hrsOfDay,minOfDay);

                            finish();
                        } else {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.all_mandatrity), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            newFragment = new TimePickerFragment(globalTime, et_time, new TimePickerFragment.GetTime() {
                @Override
                public void getTimeMethod(int finalHours, int final_minute) {

                    hrsOfDay = finalHours;
                    minOfDay = final_minute;
                }
            });

            et_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DialogFragment newFragment = new TimePickerFragment(globalTime, et_time);
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                }
            });

            et_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
//                        DialogFragment newFragment = new TimePickerFragment(globalTime, et_time);
                        newFragment.show(getSupportFragmentManager(), "timePicker");


                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //endregion

    //region LocalMethods_and_classes
    private void loadData(String id) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        EditText et_time;
        String finaltime;
        GetTime listner;

        public TimePickerFragment() {
        }

        public TimePickerFragment(String time, EditText et_time, GetTime listner) {
            this.et_time = et_time;
            this.finaltime = time;
            this.listner = listner;
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
            try {
                listner.getTimeMethod(hourOfDay, minute);
                String time, convention;
                int hrs, min;
                String hrsS, minS;

                finaltime = "00:" + hourOfDay + ":" + minute;

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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public interface GetTime {
            public void getTimeMethod(int final_hours, int final_minute);

        }

    }
    //endregion
}
