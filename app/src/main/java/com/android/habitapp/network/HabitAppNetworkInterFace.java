package com.android.habitapp.network;

import com.android.habitapp.myhabits.beans.HabitsAll;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Hitesh on 10/21/2016.
 */

public interface HabitAppNetworkInterFace {


    @GET("getHabits")
    Call<HabitsAll> getHabits();


}
