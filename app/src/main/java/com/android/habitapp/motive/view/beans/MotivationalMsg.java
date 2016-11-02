package com.android.habitapp.motive.view.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hitesh on 10/31/2016.
 */

public class MotivationalMsg implements Parcelable{

    private String post_id;
    private String post_msg;
    private String post_author;
    private String post_date;


    public MotivationalMsg() {
    }

    protected MotivationalMsg(Parcel in) {
        post_id = in.readString();
        post_msg = in.readString();
        post_author = in.readString();
        post_date = in.readString();
    }

    public static final Creator<MotivationalMsg> CREATOR = new Creator<MotivationalMsg>() {
        @Override
        public MotivationalMsg createFromParcel(Parcel in) {
            return new MotivationalMsg(in);
        }

        @Override
        public MotivationalMsg[] newArray(int size) {
            return new MotivationalMsg[size];
        }
    };

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_msg() {
        return post_msg;
    }

    public void setPost_msg(String post_msg) {
        this.post_msg = post_msg;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(post_id);
        parcel.writeString(post_msg);
        parcel.writeString(post_author);
        parcel.writeString(post_date);
    }
}
