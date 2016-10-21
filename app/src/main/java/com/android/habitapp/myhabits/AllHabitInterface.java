package com.android.habitapp.myhabits;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.habitapp.beans.Note;
import com.android.habitapp.myhabits.view.recycler.NotesViewHolder;

public interface AllHabitInterface {

    interface AllHabitViewInterfaces {
        Context getAppContext();

        Context getActivityContext();

        void showToast(Toast toast);

        void showProgress();

        void hideProgress();

        void showAlert(AlertDialog dialog);

        void notifyItemRemoved(int position);

        void notifyDataSetChanged();

        void notifyItemInserted(int layoutPosition);

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void clearEditText();

        void habitlist_success(Cursor data);
    }

    interface AllHabitPresenterInterfaces {
        void onDestroy(boolean isChangingConfiguration);

        void setView(AllHabitViewInterfaces view);

        NotesViewHolder createViewHolder(ViewGroup parent, int viewType);

        void clickNewNote(EditText editText);

        void habitlist_success(Cursor data);

        Context getAppContext();

        Context getActivityContext();
    }

    interface AllHabitModelInterfaces {
        void onDestroy(boolean isChangingConfiguration);

        int insertNote(Note note);

        void loadData();

    }
}
