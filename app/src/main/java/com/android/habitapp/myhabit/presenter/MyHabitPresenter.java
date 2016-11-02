package com.android.habitapp.myhabit.presenter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.myhabit.MyHabitInterface;
import com.android.habitapp.myhabit.view.recycler.NotesViewHolder;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyHabitPresenter implements MyHabitInterface.MyHabitPresenterInterfaces {

    private WeakReference<MyHabitInterface.MyHabitViewInterfaces> mView;
    // Model reference
    private MyHabitInterface.MyHabitModelInterfaces mModel;

    public MyHabitPresenter(MyHabitInterface.MyHabitViewInterfaces view) {
        mView = new WeakReference<>(view);
    }

    /**
     * CMyed by View every time it is destroyed.
     *
     * @param isChangingConfiguration true: is changing configuration
     *                                and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is cMyed
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if (!isChangingConfiguration) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @throws NullPointerException when View is unavailable
     */
    private MyHabitInterface.MyHabitViewInterfaces getView() throws NullPointerException {
        if (mView != null)
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * CMyed by View during the reconstruction events
     *
     * @param view Activity instance
     */
    @Override
    public void setView(MyHabitInterface.MyHabitViewInterfaces view) {
        mView = new WeakReference<>(view);
    }

    /**
     * CMyed by Activity during MVP setup. Only cMyed once.
     *
     * @param model Model instance
     */
    public void setModel(MyHabitInterface.MyHabitModelInterfaces model) {
        mModel = model;

        // start to load data
        mModel.loadData();
    }


    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * Retrieve total Notes count from Model
     *
     * @return Notes size
     */

    @Override
    public NotesViewHolder createViewHolder(ViewGroup parent, int viewType) {
        NotesViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewTaskRow = inflater.inflate(R.layout.holder_notes, parent, false);
        viewHolder = new NotesViewHolder(viewTaskRow);

        return viewHolder;
    }


    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }


    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }


    @Override
    public void clickNewNote(final EditText editText) {
        getView().showProgress();
        final String noteText = editText.getText().toString();

        try {
            getView().showToast(makeToast("Cannot add a blank HABIT_NAME!"));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void habitlist_success(Cursor data) {

    }

    private String getDate() {
        return new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }


}
