package com.android.habitapp.myhabit.view.recycler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import com.android.habitapp.data.HabitContentProvider;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Constants;
import com.android.habitapp.extra.Utils;

/**
 * Created by Hitesh on 10/21/2016.
 */

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private Cursor cursor;
    private Context context;

    public void swapCursor(final Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
        //updateDate();
        this.notifyDataSetChanged();
    }

    public void updateDate() {
        try {
            if (getItemCount() > 0) {
                if (Utils.getBoolean(context, Constants.UPDATE_TASK_LIST)) {
                    ContentValues values = new ContentValues();
                    values.put(HabitDb.MY_HABIT_TODAY_STATUS, 0);
                    //id=cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_SR_NO));
                    Uri uri = Uri.parse(String.valueOf(HabitContentProvider.CONTENT_URI2));
                    int result = context.getContentResolver().update(uri, values, null, null);
                    if (result > 0) {
                        Utils.saveBoolean(context, Constants.UPDATE_TASK_LIST, false);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public final void onBindViewHolder(final VH holder, final int position) {
        final Cursor cursor = this.getItem(position);
        this.onBindViewHolder(holder, cursor);
    }

    public abstract void onBindViewHolder(final VH holder, final Cursor cursor);

    @Override
    public int getItemCount() {
        return this.cursor != null
                ? this.cursor.getCount()
                : 0;
    }


    public Cursor getItem(final int position) {
        if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;
    }

    public Cursor getCursor() {
        return this.cursor;
    }
}
