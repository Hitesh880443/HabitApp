package com.android.habitapp.motive.view.beans;

import java.util.ArrayList;

/**
 * Created by Hitesh on 10/31/2016.
 */

public class MotivationalParent {

    ArrayList<MotivationalMsg> motiveList;


    public MotivationalParent() {
    }

    public MotivationalParent(ArrayList<MotivationalMsg> motiveList) {
        this.motiveList = motiveList;
    }

    public ArrayList<MotivationalMsg> getMotiveList() {
        return motiveList;
    }

    public void setMotiveList(ArrayList<MotivationalMsg> motiveList) {
        this.motiveList = motiveList;
    }
}
