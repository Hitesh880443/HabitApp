package com.android.habitapp.motivational_msg.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Hitesh on 10/20/2016.
 */

public class MsgAll implements Parcelable {

    private ArrayList<MsgSingle> habitList;


    public MsgAll() {
    }

    public MsgAll(ArrayList<MsgSingle> habitList) {
        this.habitList = habitList;
    }

    protected MsgAll(Parcel in) {
        habitList = in.createTypedArrayList(MsgSingle.CREATOR);
    }

    public static final Creator<MsgAll> CREATOR = new Creator<MsgAll>() {
        @Override
        public MsgAll createFromParcel(Parcel in) {
            return new MsgAll(in);
        }

        @Override
        public MsgAll[] newArray(int size) {
            return new MsgAll[size];
        }
    };

    public ArrayList<MsgSingle> getHabitList() {
        return habitList;
    }

    public void setHabitList(ArrayList<MsgSingle> habitList) {
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
