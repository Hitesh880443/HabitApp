package com.android.habitapp.habitstore.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Hitesh on 10/20/2016.
 */

public class HabitsAll implements Parcelable {

    public static final Creator<HabitsAll> CREATOR = new Creator<HabitsAll>() {
        @Override
        public HabitsAll createFromParcel(Parcel in) {
            return new HabitsAll(in);
        }

        @Override
        public HabitsAll[] newArray(int size) {
            return new HabitsAll[size];
        }
    };
    private ArrayList<HabitSingle> habitList;

    public HabitsAll() {
    }

    public HabitsAll(ArrayList<HabitSingle> habitList) {
        this.habitList = habitList;
    }

    protected HabitsAll(Parcel in) {
        habitList = in.createTypedArrayList(HabitSingle.CREATOR);
    }

    public ArrayList<HabitSingle> getHabitList() {
        return habitList;
    }

    public void setHabitList(ArrayList<HabitSingle> habitList) {
        this.habitList = habitList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(habitList);
    }
}
