package com.example.jojo.obsido.form.steps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;
import com.example.jojo.obsido.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class PartnerAgeStep extends Step<PartnerAgeStep.AgeHolder> {

    private static final String LOG_TAG = "PartnerAgeStep".getClass().getSimpleName();

    private static final int DEFAULT_PARTNER_AGE = 18;
    private static final int MAX_PARTNER_AGE = 999;
    private static final int MIN_PARTNER_AGE = 0;


    private NumberPicker mNumberPicker;

    private int mPartnerAge;



    public PartnerAgeStep(String stepTitle) {
        super(stepTitle);

        mPartnerAge = DEFAULT_PARTNER_AGE;
    }

    @Override
    protected View createStepContentLayout() {
        // Here we generate the view that will be used by the library as the content of the step.
        // In this case we do it programmatically, but we could also do it by inflating an XML layout.

        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View ageStepContent = inflater.inflate(R.layout.step_age_layout, null, false);
        mNumberPicker = ageStepContent.findViewById(R.id.age_number_picker);
        setupPartnerAge();

        return ageStepContent;
    }

    private void setupPartnerAge() {
        if (mNumberPicker != null) {
            mNumberPicker.setMaxValue(MAX_PARTNER_AGE);
            mNumberPicker.setMinValue(MIN_PARTNER_AGE);
            mNumberPicker.setValue(DEFAULT_PARTNER_AGE); // possibly change this value the age value stored
        }

    }

    @Override
    public AgeHolder getStepData() {
        // We get the step's data from the user value
        return new AgeHolder(mPartnerAge);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        // Because the step's data is already a human-readable string, we don't need to convert it.
        // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
        // This string will be displayed in the subtitle of the step whenever the step gets closed.
        String partnerAge = Integer.toString(getStepData().age);
        return partnerAge;
    }


    @Override
    protected void onStepOpened(boolean animated) {
        // This will be called automatically whenever the step gets opened.
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // This will be called automatically whenever the step gets closed.
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as completed.
        updateTitle(Integer.toString(mPartnerAge), true);
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as uncompleted.
    }


    @Override
    public void restoreStepData(AgeHolder data) {
        // To restore the step after a configuration change, we restore the age TextView view
        mPartnerAge = data.age;

        mNumberPicker.setValue(data.age);// TODO: alarmTimePicker.updateTime(alarmTimeHour, alarmTimeMinutes);
    }

    @Override
    protected IsDataValid isStepDataValid(AgeHolder stepData) {

        return new IsDataValid(true);
    }


    /**
     * Age holder class for the age.
     *
     * Requirements:
     *  Should not allow negative values
     */
    public static class AgeHolder {

        public int age;

        public AgeHolder(int age) {
            this.age = age;
        }
    }
}