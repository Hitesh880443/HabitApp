package com.android.habitapp.myhabit.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.addHabit.CalenderViewAct;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;
import com.android.habitapp.extra.AlarmReciver;
import com.android.habitapp.myhabit.view.recycler.HabitCursorAdapter;

import java.util.Calendar;


public class MyHabitFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //region Variables
    private static final int LOADER_SEARCH_RESULTS = 2;
    PendingIntent pendingintent;
    private HabitCursorAdapter adapter;
    private Context mContext;

    //endregion

    //region Views
    private RecyclerView rv_habit;
    private RelativeLayout rl_progressbar;


    //endregion


    //region Lifecycle_and_Android_Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_habit_layout, container, false);
        setupViews(view);
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        restratLoader();
        Log.d("Resume call", "My Habit");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //endregion


    //region MVP

    //endregion

    //region setViews
    private void setupViews(View view) {
        rl_progressbar = (RelativeLayout) view.findViewById(R.id.rl_progressbar);
        rv_habit = (RecyclerView) view.findViewById(R.id.rv_habit);
        rv_habit.setHasFixedSize(true);
        adapter = new HabitCursorAdapter(getActivity());
        adapter.setOnItemClickListener(new HabitCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor) {
                Intent addHabit = new Intent(mContext, CalenderViewAct.class);
                String rowId = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_SR_NO));
                Bundle bundle = new Bundle();
                bundle.putString("rowId", rowId);
                addHabit.putExtras(bundle);
                mContext.startActivity(addHabit);


            }
        });
        rv_habit.setAdapter(adapter);

        rv_habit.setLayoutManager(new LinearLayoutManager(getActivity()));

       /* if (Utils.isNetworkAvailable(mContext)) {
            if (Utils.getBoolean(mContext, Constants.FIRST_RUN) == false)
                showProgress();
            getData();
        }*/


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SEARCH_RESULTS:

                final Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI2));
                return new CursorLoader(mContext, uri, null, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                adapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                adapter.swapCursor(null);
                break;
        }
    }


    //endregion

    //region LocalMethods_and_classes


    private void showProgress() {
        if (rl_progressbar.getVisibility() != View.VISIBLE) {
            rl_progressbar.setVisibility(View.VISIBLE);
        }
    }

    private void stopProgress() {
        if (rl_progressbar.getVisibility() != View.GONE) {
            rl_progressbar.setVisibility(View.GONE);
        }
    }

    public void restratLoader() {
        this.getLoaderManager().restartLoader(LOADER_SEARCH_RESULTS, null, this);
    }

    public void deleteRows() {
        Log.d("Habit delete", String.valueOf(mContext.getContentResolver().delete(HabitContentProvider.CONTENT_URI, null, null)));
    }

    public void showHabit(Cursor cursor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //before
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape);
        dialog.setContentView(R.layout.habit_show_layout);
        dialog.setTitle(cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_NAME)));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // set the custom dialog components - text, image and button
        TextView dailog_habit_desc = (TextView) dialog.findViewById(R.id.dailog_habit_desc);
        TextView dailog_habit_name = (TextView) dialog.findViewById(R.id.dailog_habit_name);

        dailog_habit_desc.setText(cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_DESCIPTION)));
        dailog_habit_name.setText(cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_NAME)));

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog

        final Intent intent = new Intent(mContext, AlarmReciver.class);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.dismiss();


                AlarmManager alarmmanager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                Calendar calendar = Calendar.getInstance();
                int hrs, min;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    hrs = timePicker.getHour();
                    min = timePicker.getMinute();

                } else {
                    hrs = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }
                calendar.set(Calendar.HOUR_OF_DAY, hrs);
                calendar.set(Calendar.MINUTE, min);

                pendingintent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                intent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
                alarmmanager.cancel(pendingintent);

                Calendar now = Calendar.getInstance();
                if (now.after(calendar)) {
                    Log.d("Hey", "Added a day");
                    calendar.add(Calendar.DATE, 1);
                }
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingintent);
                Toast.makeText(mContext, hrs + " : " + min, Toast.LENGTH_SHORT).show();


            }
        });

        dialog.show();
    }


//endregion

}
