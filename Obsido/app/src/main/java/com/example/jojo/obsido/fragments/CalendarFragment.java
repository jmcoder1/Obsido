package com.example.jojo.obsido.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.db.Event;
import com.example.jojo.obsido.utils.EventUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {

    private OnCalendarDatePass calendarDatePasser;
    public interface OnCalendarDatePass {
        void onCalendarMonthPass(Calendar calendarDate);
        void onCalendarDayClicked(Calendar clickedCalendar);
    }

    private CalendarView mCalendarView;
    private boolean mEventsHidden = false;

    private Calendar currentCalendarMonth;
    private Calendar clickedCalendarDay;

    private Toolbar mToolbar;

    public CalendarFragment() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar_today:
                setCalendarToday();
                return  true;

            case R.id.action_hide_events:
                if(mEventsHidden) {
                    mCalendarView.showEvents();
                    item.setTitle(R.string.action_hide_events);
                    mEventsHidden = false;
                } else {
                    mCalendarView.hideEvents();
                    item.setTitle(R.string.action_show_events);
                    mEventsHidden = true;
                }
                return true;

            case R.id.action_settings:
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootViewCalendar = inflater.inflate(R.layout.fragment_calendar, container, false);

        mCalendarView = rootViewCalendar.findViewById(R.id.calendarView);
        mToolbar = getActivity().findViewById(R.id.toolbar);

        initCalendarView();
        return rootViewCalendar;
    }

    private void initCalendarView() {
        mCalendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                if(mToolbar != null) {
                    try {
                        mToolbar.setTitle(mCalendarView.getCalendarTitleDate());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                currentCalendarMonth = mCalendarView.getCurrentPageDate();
                calendarDatePasser.onCalendarMonthPass(currentCalendarMonth);
            }
        });

        mCalendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                if(mToolbar != null) {
                    try {
                        mToolbar.setTitle(mCalendarView.getCalendarTitleDate());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                currentCalendarMonth = mCalendarView.getCurrentPageDate();
                calendarDatePasser.onCalendarMonthPass(currentCalendarMonth);
            }
        });

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                clickedCalendarDay = eventDay.getCalendar();
                calendarDatePasser.onCalendarDayClicked(clickedCalendarDay);
            }
        });

        if (mToolbar != null) {
            try {
                // TODO: Change this a bit
                mToolbar.setTitle(mCalendarView.getCalendarTitleDate());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void setEvents(List<Event> events) {
        List<EventDay> eventDays = mCalendarView.getEventDays();

        if(events != null) {
            for(int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                Calendar calendar = Calendar.getInstance();
                Date eventDate = event.getDate();
                calendar.setTime(eventDate);

                Drawable eventDrawable = EventUtils.getEventDrawable(event, getResources());
                EventDay eventDay = new EventDay(calendar, eventDrawable);
                eventDays.add(eventDay);
            }
        }

        mCalendarView.setEventDays(eventDays);
    }

    private void setCalendarToday() {
        Calendar calToday = Calendar.getInstance();

        try {
            mCalendarView.setDate(calToday);
        } catch(OutOfDateRangeException e) {
            e.printStackTrace();
        }
        currentCalendarMonth = mCalendarView.getCurrentPageDate();
        calendarDatePasser.onCalendarMonthPass(calToday);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarDatePass) {
            calendarDatePasser = (OnCalendarDatePass) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCalendarDatePass");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        calendarDatePasser = null;
    }
}
