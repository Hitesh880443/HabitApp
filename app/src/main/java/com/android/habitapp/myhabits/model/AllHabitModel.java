package com.android.habitapp.myhabits.model;



import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.android.habitapp.beans.Note;
import com.android.habitapp.data.DAO;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.myhabits.AllHabitInterface;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

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
public class AllHabitModel extends LoaderManager implements AllHabitInterface.AllHabitModelInterfaces ,LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_SEARCH_RESULTS = 0;
    // Presenter reference
    private AllHabitInterface.AllHabitPresenterInterfaces mPresenter;
    private DAO mDAO;
    // Recycler data
    public ArrayList<Note> mNotes;

    public AllHabitModel(AllHabitInterface.AllHabitPresenterInterfaces presenter) {
        this.mPresenter = presenter;
        mDAO = new DAO( mPresenter.getAppContext() );
    }
    public AllHabitModel(AllHabitInterface.AllHabitPresenterInterfaces presenter, DAO dao) {
        this.mPresenter = presenter;
        mDAO = dao;
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
            mDAO = null;
            mNotes = null;
        }
    }

    @Override
    public void loadData() {
        mNotes = mDAO.getAllNotes();
        restartLoader(LOADER_SEARCH_RESULTS, null, this);

       // return mNotes != null;
    }



    public int getNotePosition(Note note) {
        for (int i=0; i<mNotes.size(); i++){
            if ( note.getId() == mNotes.get(i).getId())
                return i;
        }
        return -1;
    }

    @Override
    public int insertNote(Note note) {
        Note insertedNote = mDAO.insertNote(note);
        if ( insertedNote != null ) {
            loadData();
            return getNotePosition(insertedNote);
        }
        return -1;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SEARCH_RESULTS:

                final Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI));
                return new CursorLoader(mPresenter.getAppContext(), uri, null, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                mPresenter.habitlist_success(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_SEARCH_RESULTS:
                mPresenter.habitlist_success(null);
                break;
        }
    }

    @Override
    public <D> Loader<D> initLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        return null;
    }

    @Override
    public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        return null;
    }

    @Override
    public void destroyLoader(int id) {

    }

    @Override
    public <D> Loader<D> getLoader(int id) {
        return null;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

    }
}
