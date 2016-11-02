package com.android.habitapp.motive.view.recycler;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Hitesh on 10/21/2016.
 */

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private Cursor cursor;

    public void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
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
