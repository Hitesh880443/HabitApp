package com.android.habitapp.myhabit;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.habitapp.myhabit.view.recycler.NotesViewHolder;

/**
 * Created by Hitesh on 10/29/2016.
 */

public interface MyHabitInterface {
    interface MyHabitViewInterfaces {
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

    interface MyHabitPresenterInterfaces {
        void onDestroy(boolean isChangingConfiguration);

        void setView(MyHabitViewInterfaces view);

        NotesViewHolder createViewHolder(ViewGroup parent, int viewType);

        void clickNewNote(EditText editText);

        void habitlist_success(Cursor data);

        Context getAppContext();

        Context getActivityContext();
    }

    interface MyHabitModelInterfaces {
        void onDestroy(boolean isChangingConfiguration);


        void loadData();

    }
}
