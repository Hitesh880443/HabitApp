package com.android.habitapp.habitstore.view;

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
import com.android.habitapp.addHabit.HabitSettingActivity;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Constants;
import com.android.habitapp.extra.Utils;
import com.android.habitapp.habitstore.beans.HabitSingle;
import com.android.habitapp.habitstore.beans.HabitsAll;
import com.android.habitapp.habitstore.view.recycler.HabitCursorAdapter;
import com.android.habitapp.network.HabitAppNetworkInterFace;
import com.android.habitapp.network.RetrofitAPI;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class AllHabitFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //region Variables
    private static final int LOADER_SEARCH_RESULTS = 1;
    PendingIntent pendingintent;
    private HabitCursorAdapter adapter;
    private Context mContext;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_habit_layout, container, false);
        setupViews(view);
        return view;

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
        if (Utils.isNetworkAvailable(mContext)) {
            getData();
        }
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
                Intent addHabit = new Intent(mContext, HabitSettingActivity.class);
                String rowId = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_SR_NO));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(HabitDb.HABIT_NAME));
                Bundle bundle = new Bundle();
                bundle.putString("rowId", rowId);
                bundle.putString("habitType", "store");
                addHabit.putExtras(bundle);
                mContext.startActivity(addHabit);


                if(Utils.isNetworkAvailable(mContext))
                {
                    Bundle analy = new Bundle();
                    analy.putString(FirebaseAnalytics.Param.ITEM_ID, rowId);
                    analy.putString(FirebaseAnalytics.Param.ITEM_NAME, "habitstore");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analy);
                }

            }
        });
        rv_habit.setAdapter(adapter);

        rv_habit.setLayoutManager(new LinearLayoutManager(getActivity()));
        restratLoader();



    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SEARCH_RESULTS:

                final Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI));
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

    public void getData() {

        Retrofit retrofit = RetrofitAPI.getRetrofitClient(Constants.BASE_URL);
        HabitAppNetworkInterFace service = retrofit.create(HabitAppNetworkInterFace.class);
        int lastID = Utils.getIntData(mContext, Constants.HABIT_LAST_ID);
        Call<HabitsAll> call = service.getHabits(String.valueOf(lastID));
        call.enqueue(new Callback<HabitsAll>() {
            @Override
            public void onResponse(Response<HabitsAll> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    HabitsAll data = response.body();
                    if (data != null && data.getHabitList() != null && data.getHabitList().size() > 0) {

                        ArrayList<HabitSingle> habiitList = data.getHabitList();
                        Log.d("Habit", String.valueOf(data.getHabitList().size()));
                        //deleteRows();
                        for (int i = 0; i < habiitList.size(); i++) {
                            HabitSingle single = habiitList.get(i);

                            ContentValues values = new ContentValues();
                            values.put(HabitDb.HABIT_ID, single.getHabit_id());
                            values.put(HabitDb.HABIT_NAME, single.getHabit_name());
                            values.put(HabitDb.HABIT_DESCIPTION, single.getHabit_desciption());
                            values.put(HabitDb.HABIT_USERS, single.getHabit_users());

                            Log.d("Habit INSERT", String.valueOf(mContext.getContentResolver().insert(HabitContentProvider.CONTENT_URI, values)));
                            if (i == habiitList.size() - 1) {
                                Utils.saveIntdata(mContext, Integer.parseInt(single.getHabit_id()), Constants.HABIT_LAST_ID);
                                Log.d(Constants.HABIT_LAST_ID, String.valueOf(Utils.getIntData(mContext, Constants.HABIT_LAST_ID)));
                            }

                        }
                        Utils.saveBoolean(mContext, Constants.FIRST_RUN, true);
                        //stopProgress();
                        restratLoader();

                    } else {
                        Log.d("Habit", "0");
                        // stopProgress();
                    }
                } else {
                    Log.d("Habit", "0" + response.message());
                    //stopProgress();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Habit", "0" + t.getMessage());
                stopProgress();
            }
        });

    }



    private void stopProgress() {
        if (rl_progressbar.getVisibility() != View.GONE) {
            rl_progressbar.setVisibility(View.GONE);
        }
    }

    public void restratLoader() {
        this.getLoaderManager().restartLoader(LOADER_SEARCH_RESULTS, null, this);
    }




//endregion

}
