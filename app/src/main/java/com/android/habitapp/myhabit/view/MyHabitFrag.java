package com.android.habitapp.myhabit.view;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
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
import com.android.habitapp.addHabit.MyHabitDetailActivity;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Constants;
import com.android.habitapp.extra.Utils;
import com.android.habitapp.myhabit.view.recycler.HabitCursorAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyHabitFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //region Variables
    private static final int LOADER_SEARCH_RESULTS = 2;
    PendingIntent pendingintent;
    private HabitCursorAdapter adapter;
    private Context mContext;
    private Date currDate, saveDate;
    private Date todaysDate;
    private TextView tv_todays_date;
    private AdView mAdView;
    //endregion

    //region Views
    private RecyclerView rv_habit;
    private RelativeLayout rl_progressbar;


    //endregion


    //region Lifecycle_and_Android_Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {


            mContext = getActivity();
            currDate = Utils.truncateToDay(new Date());
            Log.d("Date Current", currDate.toString());


            if (Utils.getStringData(mContext, Constants.TODAYS_DATE) != null) {
                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date date = (Date) formatter.parse(Utils.getStringData(mContext, Constants.TODAYS_DATE));
                date = Utils.truncateToDay(date);
                Log.d("Date Current", date.toString());
                if (currDate.after(date) || currDate.before(date)) {
                    Log.d("Yo", "After");
                    Utils.saveStringdata(mContext, String.valueOf(currDate.toString()), Constants.TODAYS_DATE);
                    Utils.saveBoolean(mContext, Constants.UPDATE_TASK_LIST, true);
                    updateDate();
                }


            } else {
                Log.d("Saved  Date", "First Launch");
                Utils.saveStringdata(mContext, String.valueOf(currDate.toString()), Constants.TODAYS_DATE);
                Utils.saveBoolean(mContext, Constants.UPDATE_TASK_LIST, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        restratLoader();
        Log.d("Resume call", "My Habit");
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    //endregion

    //region MVP

    //endregion

    //region setViews
    private void setupViews(View view) {
        if(Utils.isNetworkAvailable(mContext)) {
            MobileAds.initialize(mContext, mContext.getResources().getString(R.string.banner_home_footer));
            mAdView = (AdView) view.findViewById(R.id.ad_view);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        rl_progressbar = (RelativeLayout) view.findViewById(R.id.rl_progressbar);
        tv_todays_date = (TextView) view.findViewById(R.id.tv_todays_date);
        rv_habit = (RecyclerView) view.findViewById(R.id.rv_habit);
        rv_habit.setHasFixedSize(true);
        adapter = new HabitCursorAdapter(getActivity());
        adapter.setOnItemClickListener(new HabitCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor) {
                Intent addHabit = new Intent(mContext, MyHabitDetailActivity.class);
                String rowId = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.MY_HABIT_SR_NO));
                Bundle bundle = new Bundle();
                bundle.putString("rowId", rowId);
                addHabit.putExtras(bundle);
                mContext.startActivity(addHabit);


            }
        });
        rv_habit.setAdapter(adapter);

        rv_habit.setLayoutManager(new LinearLayoutManager(getActivity()));

        Date date = new Date();
        if (date != null) {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
            Date dateV = null;
            String newDateStr = null, newTimeStr = null;
            try {
                newDateStr = DateFormat.getDateInstance().format(date);

            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_todays_date.setText(newDateStr);
        }

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
                adapter.swapCursor(data, mContext);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                adapter.swapCursor(null, mContext);
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


    public void updateDate() {
        try {
            //if (getItemCount() > 0) {
            if (Utils.getBoolean(mContext, Constants.UPDATE_TASK_LIST)) {
                ContentValues values = new ContentValues();
                values.put(HabitDb.MY_HABIT_TODAY_STATUS, 0);
                //id=cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_SR_NO));
                Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI2));
                int result = mContext.getContentResolver().update(uri, values, null, null);
                if (result > 0) {
                    Utils.saveBoolean(mContext, Constants.UPDATE_TASK_LIST, false);
                }
            }
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//endregion

}
