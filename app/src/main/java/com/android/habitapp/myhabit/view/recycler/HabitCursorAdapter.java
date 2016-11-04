package com.android.habitapp.myhabit.view.recycler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.habitapp.R;
import com.android.habitapp.data.habit.HabitContentProvider;
import com.android.habitapp.data.habit.HabitDb;
import com.android.habitapp.extra.Utils;

/**
 * Created by Hitesh on 10/21/2016.
 */

public class HabitCursorAdapter extends RecyclerViewCursorAdapter<HabitCursorAdapter.HabitViewHolder>
        implements View.OnClickListener {
    private final LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    private Context mContext;


    public HabitCursorAdapter(final Context context) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.row_my_habit, parent, false);
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

        TextView name, txt_days_value, txt_reminder_value, habit_status;
        ImageView iv_taskDone;
        final Context context;
        private String id;

        public HabitViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.habit_name);
            txt_days_value = (TextView) itemView.findViewById(R.id.txt_days_value);
            txt_reminder_value = (TextView) itemView.findViewById(R.id.txt_reminder_value);
            habit_status = (TextView) itemView.findViewById(R.id.habit_status);
            iv_taskDone = (ImageView) itemView.findViewById(R.id.iv_taskDone);
           /* iv_taskDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Wooo", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(HabitDb.MY_HABIT_TODAY_STATUS, 1);
                    Uri uri = Uri.parse(HabitContentProvider.CONTENT_URI2 + "/" + id);
                    Log.d("update result", String.valueOf(context.getContentResolver().update(uri, values, null, null)));

                }
            });*/

        }

        public void bindData(final Cursor cursor) {

            id = cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_SR_NO));
            final String name = cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_NAME));
            final String days_comp = cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_DAYS_COMPLETED));
            final String reminder_time = cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_REMINDER_TIME));
            final int todays_Status = cursor.getInt(cursor.getColumnIndex(HabitDb.MY_HABIT_TODAY_STATUS));
            this.name.setText(Utils.setFirstltrCapitcal(name));
            this.txt_days_value.setText(" " + days_comp);
            this.txt_reminder_value.setText(" " + reminder_time);

            if (todays_Status == 0) {
                habit_status.setText(context.getResources().getString(R.string.task_pending));
                habit_status.setTextColor(context.getResources().getColor(R.color.red));
                iv_taskDone.setVisibility(View.VISIBLE);

            } else {
                habit_status.setText(context.getResources().getString(R.string.task_done));
                habit_status.setTextColor(context.getResources().getColor(R.color.green));
                iv_taskDone.setVisibility(View.INVISIBLE);
            }

            iv_taskDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Wooo", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(HabitDb.MY_HABIT_TODAY_STATUS, 1);
                    //id=cursor.getString(cursor.getColumnIndex(HabitDb.MY_HABIT_SR_NO));
                    Uri uri = Uri.parse(HabitContentProvider.CONTENT_URI2 + "/" + id);
                    int result = context.getContentResolver().update(uri, values, null, null);
                    if (result > 0) {
                        habit_status.setText(context.getResources().getString(R.string.task_done));
                        habit_status.setTextColor(context.getResources().getColor(R.color.green));
                        iv_taskDone.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }
    }
}
