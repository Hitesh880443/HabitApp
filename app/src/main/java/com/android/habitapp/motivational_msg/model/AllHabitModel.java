package com.android.habitapp.motivational_msg.model;


import com.android.habitapp.motivational_msg.AllHabitInterface;

/**
 * Model layer on Model View Presenter Pattern
 *
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class AllHabitModel  implements AllHabitInterface.AllHabitModelInterfaces {

    private static final int LOADER_SEARCH_RESULTS = 0;
    // Presenter reference
    private AllHabitInterface.AllHabitPresenterInterfaces mPresenter;
    // Recycler data

    public AllHabitModel(AllHabitInterface.AllHabitPresenterInterfaces presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;

        }
    }

    @Override
    public void loadData() {

    }



   /* public int getNotePosition(Note note) {
        for (int i=0; i<mNotes.size(); i++){
            if ( note.getId() == mNotes.get(i).getId())
                return i;
        }
        return -1;
    }*/





}
