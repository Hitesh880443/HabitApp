package com.android.habitapp.motive.view.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.habitapp.R;
import com.android.habitapp.data.HabitDb;
import com.android.habitapp.extra.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hitesh on 10/21/2016.
 */

public class MotiveCursorAdapter extends RecyclerViewCursorAdapter<MotiveCursorAdapter.HabitViewHolder>
        implements View.OnClickListener {
    private final LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public MotiveCursorAdapter(final Context context) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.row_motive, parent, false);
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

        TextView motive_msg_value, motive_msg_author, date;

        public HabitViewHolder(final View itemView) {
            super(itemView);
            motive_msg_value = (TextView) itemView.findViewById(R.id.motive_msg_value);
            motive_msg_author = (TextView) itemView.findViewById(R.id.motive_msg_author);
            date = (TextView) itemView.findViewById(R.id.date);


        }

        public void bindData(final Cursor cursor) {
            final String msg = cursor.getString(cursor.getColumnIndex(HabitDb.MOTIVE_MSG));
            final String author = cursor.getString(cursor.getColumnIndex(HabitDb.MOTIVE_AUTHOR));
            final String date = cursor.getString(cursor.getColumnIndex(HabitDb.MOTIVE_DATE));
            if (msg != null && msg.length() > 0) {
                this.motive_msg_value.setText(Utils.setFirstltrCapitcal(msg));
                this.motive_msg_value.setVisibility(View.VISIBLE);

            }
            else
                this.motive_msg_value.setVisibility(View.GONE);



            if (author != null && author.length() > 0) {
                this.motive_msg_author.setText(Utils.setFirstltrCapitcal(author));
                this.motive_msg_author.setVisibility(View.VISIBLE);

            }
            else
                this.motive_msg_author.setVisibility(View.GONE);



            if (date != null) {
                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                Date dateV = null;
                String newDateStr = null, newTimeStr = null;
                try {
                    dateV = form.parse(date);
                    newDateStr = DateFormat.getDateInstance().format(dateV);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.date.setText(newDateStr);
            }


        }
    }
}
