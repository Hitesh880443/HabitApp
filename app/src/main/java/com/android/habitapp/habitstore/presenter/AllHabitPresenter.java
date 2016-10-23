package com.android.habitapp.habitstore.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.habitstore.AllHabitInterface;
import com.android.habitapp.habitstore.view.recycler.NotesViewHolder;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Presenter layer on Model View Presenter pattern
 *
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class AllHabitPresenter implements AllHabitInterface.AllHabitPresenterInterfaces {

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<AllHabitInterface.AllHabitViewInterfaces> mView;
    // Model reference
    private AllHabitInterface.AllHabitModelInterfaces mModel;

    /**
     * Presenter Constructor
     * @param view  Secondact
     */
    public AllHabitPresenter(AllHabitInterface.AllHabitViewInterfaces view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by View every time it is destroyed.
     * @param isChangingConfiguration   true: is changing configuration
     *                                  and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is called
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if ( !isChangingConfiguration ) {
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
    private AllHabitInterface.AllHabitViewInterfaces getView() throws NullPointerException {
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     * @param view  Activity instance
     */
    @Override
    public void setView(AllHabitInterface.AllHabitViewInterfaces view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by Activity during MVP setup. Only called once.
     * @param model Model instance
     */
    public void setModel(AllHabitInterface.AllHabitModelInterfaces model) {
        mModel = model;

        // start to load data
        mModel.loadData();
    }

    /**
     * Load data from Model in a AsyncTask
     */
/*
    private void loadData() {
        try {
            getView().showProgress();
            new AsyncTask<Void, Void, void>() {
                @Override
                protected void doInBackground(Void... params) {
                    // Load data from Model

                }

                @Override
                protected void onPostExecute(Boolean result) {
                    try {
                        getView().hideProgress();
                        if (!result) // Loading error
                            getView().showToast(makeToast("Error loading data."));
                        else // success
                            getView().notifyDataSetChanged();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
*/

    /**
     * Creat a Toast object with given message
     * @param msg   Toast message
     * @return      A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * Retrieve total Notes count from Model
     * @return  Notes size
     */

    @Override
    public NotesViewHolder createViewHolder(ViewGroup parent, int viewType) {
        NotesViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewTaskRow = inflater.inflate(R.layout.holder_notes, parent, false);
        viewHolder = new NotesViewHolder(viewTaskRow);

        return viewHolder;
    }

    /**
     * Binds ViewHolder with RecyclerView
     * @param holder    Holder to bind
     * @param position  Position on Recycler adapter
     */


    /**
     * Retrieve Application Context
     * @return  Application context
     */
    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Retrieves Activity context
     * @return  Activity context
     */
    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Called by View when user clicks on new Note btn.
     * Creates a Note with text typed by the user and asks
     * Model to insert in DB.
     * @param editText  EdiText with text typed by user
     */
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

    /**
     * Create a Note object with giver text
     * @param noteText  String with Note text
     * @return  A Note object
     */
  /*  public Note makeNote(String noteText) {
        Note note = new Note();
        note.setText( noteText );
        note.setDate(getDate());
        return note;

    }*/

    /**
     * Get current Date as a String
     * @return  The current date
     */
    private String getDate() {
        return new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }


}
