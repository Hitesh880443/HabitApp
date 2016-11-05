package com.android.habitapp.habitstore.view.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.habitapp.R;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Utils;

/**
 * Created by Hitesh on 10/21/2016.
 */

public class HabitCursorAdapter extends RecyclerViewCursorAdapter<HabitCursorAdapter.HabitViewHolder>
        implements View.OnClickListener {
    private final LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public HabitCursorAdapter(final Context context) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.row_habitstore, parent, false);
//        final View view = this.layoutInflater.inflate(R.layout.row_habit, parent, false);
        view.setOnClickListener(this);

        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HabitViewHolder holder, final Cursor cursor) {
        holder.bindData(cursor);
    }

    @Override
    public void onClick(View view) {
        if (this.onItemClickListener != null) {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);
            if (position != RecyclerView.NO_POSITION) {
                final Cursor cursor = this.getItem(position);
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Cursor cursor);
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView name, habit_desc;
        Button btn_start;

        public HabitViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.habit_name);
            habit_desc = (TextView) itemView.findViewById(R.id.habit_desc);
            btn_start = (Button) itemView.findViewById(R.id.btn_start);

        }

        public void bindData(final Cursor cursor) {
            final String name = cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_NAME));
            final String habit_desc = cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_DESCIPTION));
            this.name.setText(Utils.setFirstltrCapitcal(name));
            this.habit_desc.setText(habit_desc);
            if (cursor.getString(cursor.getColumnIndex(HabitDb.HABIT_ID)).equalsIgnoreCase("5"))
                this.btn_start.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
        }
    }
}
