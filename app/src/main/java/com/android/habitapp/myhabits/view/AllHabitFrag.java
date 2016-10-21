package com.android.habitapp.myhabits.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.common.StateMaintainer;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;
import com.android.habitapp.extra.Constants;
import com.android.habitapp.extra.Utils;
import com.android.habitapp.myhabits.AllHabitInterface;
import com.android.habitapp.myhabits.beans.HabitSingle;
import com.android.habitapp.myhabits.beans.HabitsAll;
import com.android.habitapp.myhabits.model.AllHabitModel;
import com.android.habitapp.myhabits.presenter.AllHabitPresenter;
import com.android.habitapp.myhabits.view.recycler.HabitCursorAdapter;
import com.android.habitapp.network.HabitAppNetworkInterFace;
import com.android.habitapp.network.RetrofitAPI;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class AllHabitFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //region Variables
    private static final int LOADER_SEARCH_RESULTS = 1;
    private HabitCursorAdapter adapter;
    private Context mContext;
    //endregion

    //region Views

    private RecyclerView rv_habit;
    private RelativeLayout rl_progressbar;


    //endregion


    //region Lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_habit_layout, container, false);
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
        rv_habit.setAdapter(adapter);
        rv_habit.setLayoutManager(new LinearLayoutManager(getActivity()));
        restratLoader();
        if (Utils.isNetworkAvailable(mContext)) {
            if (Utils.getBoolean(mContext, Constants.FIRST_RUN) == false)
                showProgress();
            getData();
        }
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

    //region LocalMethods

    public void getData() {

        Retrofit retrofit = RetrofitAPI.getRetrofitClient(Constants.BASE_URL);
        HabitAppNetworkInterFace service = retrofit.create(HabitAppNetworkInterFace.class);
        Call<HabitsAll> call = service.getHabits();
        call.enqueue(new Callback<HabitsAll>() {
            @Override
            public void onResponse(Response<HabitsAll> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    HabitsAll data = response.body();
                    if (data != null && data.getHabitList() != null && data.getHabitList().size() > 0) {

                        ArrayList<HabitSingle> habiitList = data.getHabitList();
                        Log.d("Habit", String.valueOf(data.getHabitList().size()));
                        deleteRows();
                        for (int i = 0; i < habiitList.size(); i++) {
                            HabitSingle single = habiitList.get(i);

                            ContentValues values = new ContentValues();
                            values.put(HabitDb.HABIT_NAME, single.getHabit_name());
                            values.put(HabitDb.HABIT_DESCIPTION, single.getHabit_desciption());
                            values.put(HabitDb.HABIT_USERS, single.getHabit_users());

                            Log.d("Habit INSERT", String.valueOf(mContext.getContentResolver().insert(HabitContentProvider.CONTENT_URI, values)));

                        }
                        Utils.saveBoolean(mContext, Constants.FIRST_RUN, true);
                        stopProgress();
                        restratLoader();

                    } else {
                        Log.d("Habit", "0");
                        stopProgress();
                    }
                } else {
                    Log.d("Habit", "0" + response.message());
                    stopProgress();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Habit", "0" + t.getMessage());
                stopProgress();
            }
        });

    }

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

    //endregion

}
