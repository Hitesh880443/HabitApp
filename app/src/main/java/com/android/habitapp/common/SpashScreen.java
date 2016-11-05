package com.android.habitapp.common;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.habitapp.MainActivity;
import com.android.habitapp.R;
import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Constants;
import com.android.habitapp.habitstore.beans.HabitSingle;
import com.android.habitapp.habitstore.beans.HabitsAll;
import com.android.habitapp.network.HabitAppNetworkInterFace;
import com.android.habitapp.network.RetrofitAPI;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SpashScreen extends AppCompatActivity {

    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        mContext = this;

       /* if (Utils.isNetworkAvailable(mContext))
            getData();
        else {*/
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        jumpToMainActivity();
                    }
                }
            };
            timerThread.start();
        //}
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    public void getData() {

        Retrofit retrofit = RetrofitAPI.getRetrofitClient(Constants.BASE_URL);
        HabitAppNetworkInterFace service = retrofit.create(HabitAppNetworkInterFace.class);
        Call<HabitsAll> call = service.getHabits("0");
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
                            values.put(HabitDb.HABIT_ID, single.getHabit_id());
                            values.put(HabitDb.HABIT_NAME, single.getHabit_name());
                            values.put(HabitDb.HABIT_DESCIPTION, single.getHabit_desciption());
                            values.put(HabitDb.HABIT_USERS, single.getHabit_users());

                            Log.d("Habit INSERT", String.valueOf(mContext.getContentResolver().insert(HabitContentProvider.CONTENT_URI, values)));

                        }
                        //Utils.saveBoolean(mContext, Constants.FIRST_RUN, true);
                        jumpToMainActivity();

                    } else {
                        Log.d("Habit", "0");
                        jumpToMainActivity();
                    }
                } else {
                    Log.d("Habit", "0" + response.message());
                    jumpToMainActivity();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Habit", "0" + t.getMessage());
                jumpToMainActivity();
            }
        });

    }

    public void deleteRows() {
        Log.d("Habit delete", String.valueOf(mContext.getContentResolver().delete(HabitContentProvider.CONTENT_URI, null, null)));
    }

    public void jumpToMainActivity() {
        Intent intent = new Intent(SpashScreen.this, MainActivity.class);
        startActivity(intent);
    }


}
