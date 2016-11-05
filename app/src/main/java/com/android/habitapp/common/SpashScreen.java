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
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }



    public void jumpToMainActivity() {
        Intent intent = new Intent(SpashScreen.this, MainActivity.class);
        startActivity(intent);
    }


}
