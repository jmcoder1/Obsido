package com.example.jojo.obsido.form.steps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.shawnlin.numberpicker.NumberPicker;
import com.example.jojo.obsido.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class PartnerAgeStep extends Step<Integer> {

    private static final String LOG_TAG = "PartnerAgeStep".getClass().getSimpleName();

    private static final int DEFAULT_PARTNER_AGE = 18;
    private static final int MAX_PARTNER_AGE = 150;
    private static final int MIN_PARTNER_AGE = 1;

    private NumberPicker mNumberPicker;

    private int mPartnerAge;

    public PartnerAgeStep(String stepTitle) {
        super(stepTitle, "");
        mPartnerAge = DEFAULT_PARTNER_AGE;
    }

    @Override
    protected View createStepContentLayout() {
        // Here we generate the view that will be used by the library as the content of the step.
        // In this case we do it programmatically, but we could also do it by inflating an XML layout.

        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View ageStepContent = inflater.inflate(R.layout.step_partner_age_layout, null, false);
        mNumberPicker = ageStepContent.findViewById(R.id.age_number_picker);
        setupPartnerAge();

        return ageStepContent;
    }

    private void setupPartnerAge() {
        Log.v(LOG_TAG, "setupPartnerAge: called");
        if (mNumberPicker != null) {
            mNumberPicker.setMaxValue(MAX_PARTNER_AGE);
            mNumberPicker.setMinValue(MIN_PARTNER_AGE);

            // possibly change this value the age value stored
            mNumberPicker.setValue(DEFAULT_PARTNER_AGE);

            mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mPartnerAge = newVal;
                }
            });

        }


    }

    @Override
    public Integer getStepData() {
        return mPartnerAge;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        // This string will be displayed in the subtitle of the step whenever the step gets closed.
        return Integer.toString(mNumberPicker.getValue());
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // This will be called automatically whenever the step gets opened.
        Log.v(LOG_TAG, "onStepOpened: called");
        mPartnerAge = mNumberPicker.getValue();
        updateSubtitle(Integer.toString(mPartnerAge), false);
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // This will be called automatically whenever the step gets closed.
        Log.v(LOG_TAG, "onStepClosed: called");
        mPartnerAge = mNumberPicker.getValue();
        updateSubtitle(Integer.toString(mPartnerAge), false);
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
    public void restoreStepData(Integer age) {
        // To restore the step after a configuration change
        mNumberPicker.setValue(age);
        updateSubtitle(Integer.toString(age), false);
        mNumberPicker.setSelectedTextColor(mNumberPicker.getSelectedTextColor());
        markAsCompletedOrUncompleted(true);

    }

    @Override
    protected IsDataValid isStepDataValid(Integer age) {
        return new IsDataValid(true);
    }


    public void setAgePickerDividerColor(int color) {
        mNumberPicker.setDividerColor(color);
    }

    public void setAgePickerSelectedTextColor(int color) {
        mNumberPicker.setSelectedTextColor(color);
    }

}