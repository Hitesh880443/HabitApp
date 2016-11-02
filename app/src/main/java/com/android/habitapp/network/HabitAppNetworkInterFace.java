package com.android.habitapp.network;

import com.android.habitapp.habitstore.beans.HabitsAll;
import com.android.habitapp.motive.view.beans.MotivationalParent;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Hitesh on 10/21/2016.
 */

public interface HabitAppNetworkInterFace {


    @GET("getHabits/{taskId}")
    Call<HabitsAll> getHabits(@Path("taskId") String taskId);

    @GET("getMotive/{taskId}")
    Call<MotivationalParent> getMotive(@Path("taskId") String taskId);


}
