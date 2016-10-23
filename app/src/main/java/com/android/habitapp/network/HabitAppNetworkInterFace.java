package com.android.habitapp.network;

import com.android.habitapp.habitstore.beans.HabitsAll;
import com.android.habitapp.motivational_msg.beans.MsgAll;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Hitesh on 10/21/2016.
 */

public interface HabitAppNetworkInterFace {


    @GET("getHabits")
    Call<HabitsAll> getHabits();

    @GET("getMsg")
    Call<MsgAll> getMsg();


}
