package com.example.jojo.obsido.form.steps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.jojo.obsido.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.Step;

public class EventDateStep extends Step<Date> {

    private static final String LOG_TAG = "EventDateStep".getClass().getSimpleName();
    private static final SimpleDateFormat stringDateFormat = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.UK);

    private DatePicker mDatePicker;

    private Date mEventDate;
    private int dayDate, monthDate, yearDate;

    public EventDateStep(String stepTitle) {
        super(stepTitle, "");
    }

    public EventDateStep(String stepTitle, Context context) {
        super(stepTitle, "");
    }

    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dateStepContent = inflater.inflate(R.layout.step_event_date_layout, null, false);
        mDatePicker = dateStepContent.findViewById(R.id.date_picker);
        setupEventDate();

        return dateStepContent;
    }

    private void setupEventDate() {
        Log.v(LOG_TAG, "setupEventDate: called");
        if (mDatePicker != null) {

            Calendar todayDate = Calendar.getInstance();
            dayDate = todayDate.get(Calendar.DAY_OF_MONTH);
            monthDate = todayDate.get(Calendar.MONTH);
            yearDate = todayDate.get(Calendar.YEAR);

            mEventDate = getDateFromInt(dayDate, monthDate, yearDate);
            mDatePicker.init(yearDate, monthDate, dayDate, new DatePicker.OnDateChangedListener() {

                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    dayDate = dayOfMonth;
                    monthDate = month;
                    yearDate = year;

                    mEventDate = getDateFromInt(dayDate, monthDate, yearDate);
                    updateSubtitle(stringDateFormat.format(mEventDate), false);

                }
            });
        }
    }

    private Date getDateFromInt(int day, int month, int year) {
        Calendar calDate = Calendar.getInstance();
        calDate.set(Calendar.YEAR, year);
        calDate.set(Calendar.MONTH, month);
        calDate.set(Calendar.DAY_OF_MONTH, day);

        return calDate.getTime();
    }

    @Override
    public Date getStepData() {
        return mEventDate;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return stringDateFormat.format(mEventDate);
    }

    @Override
    protected void onStepOpened(boolean animated) {
        updateSubtitle(stringDateFormat.format(mEventDate), false);
    }

    @Override
    protected void onStepClosed(boolean animated) {
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as completed.
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as uncompleted.
    }

    @Override
    public void restoreStepData(Date date) {
        // To restore the step after a configuration change
        mDatePicker.updateDate(yearDate, monthDate, dayDate);
        updateSubtitle(stringDateFormat.format(mEventDate), false);
        markAsCompletedOrUncompleted(true);

    }

    @Override
    protected IsDataValid isStepDataValid(Date age) {
        return new IsDataValid(true);
    }

}