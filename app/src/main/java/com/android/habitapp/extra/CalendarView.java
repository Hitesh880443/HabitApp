package com.android.habitapp.extra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.habitapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout {

    //region Vaiables
    private static final String LOGTAG = "Calendar View";
    private static final int DAYS_COUNT = 42;
    private static final String DATE_FORMAT = "MMM yyyy";
    private String dateFormat;
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};
    private Calendar currentDate = Calendar.getInstance();
    private EventHandler eventHandler = null;
    int[] rainbow = new int[]{
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };
    //endregion

    //region Views
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    //endregion

    //region Cotructor

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    //endregion

    //region LocalMethos
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
                eventHandler.setEvents();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
                eventHandler.setEvents();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });
    }

    public void updateCalendar() {
        updateCalendar(null);
    }

    public void setEventHandler(EventHandler eventHandler) {
        try {
            this.eventHandler = eventHandler;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //region UPDATE CALENDAR
    public void updateCalendar(HashSet<Date> events) {


        try {
            ArrayList<Date> cells = new ArrayList<>();
            Calendar calendar = (Calendar) currentDate.clone();

            // determine the cell for current month's beginning
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // move calendar backwards to the beginning of the week
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            while (cells.size() < DAYS_COUNT) {
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // update grid
            grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

            // update title
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            txtDate.setText(sdf.format(currentDate.getTime()));

            // set header color according to current season
            int month = currentDate.get(Calendar.MONTH);
            int season = monthSeason[month];
            int color = rainbow[season];
            header.setBackgroundColor(getResources().getColor(color));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //endregion

    //endregion

    //region calendaradapter
    private class CalendarAdapter extends ArrayAdapter<Date> {
        private HashSet<Date> eventDays;
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            try {
                this.eventDays = eventDays;
                inflater = LayoutInflater.from(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            try {
                // day in question
                Date date = getItem(position);
                int day = date.getDate();
                int month = date.getMonth();
                int year = date.getYear();

                // today
                Date today = new Date();

                // inflate item if it does not exist yet
                if (view == null)
                    view = inflater.inflate(R.layout.control_calendar_day, parent, false);

                // if this day has an event, specify event image
                view.setBackgroundResource(0);


                // clear styling
                ((TextView) view).setTypeface(null, Typeface.NORMAL);
                ((TextView) view).setTextColor(Color.BLACK);

                if (month != today.getMonth() || year != today.getYear()) {
                    // if this day is outside current month, grey it out
                    ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
                } else if (day == today.getDate()) {
                    // if it is today, set it to blue/bold
                    ((TextView) view).setTypeface(null, Typeface.BOLD);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.today));
                }

                if (eventDays != null) {
                    for (Date eventDate : eventDays) {
                        if (eventDate.getDate() == day &&
                                eventDate.getMonth() == month &&
                                eventDate.getYear() == year) {
                            // mark this day for event
                            //view.setBackgroundResource(R.drawable.reminder);
                            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.green));
                            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                            break;
                        }
                    }
                }

                // set text
                ((TextView) view).setText(String.valueOf(date.getDate()));
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return view;
        }
    }

    //endregion

    //region Interface
    public interface EventHandler {
        void onDayLongPress(Date date);

        void setEvents();
    }
    //endregion
}
