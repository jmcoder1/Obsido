package com.example.jojo.obsido.form.steps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.jojo.obsido.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import ernestoyaquello.com.verticalstepperform.Step;

public class EventActsStep extends Step<boolean[]> {

    private static final int NUM_EVENTS = 4;

    private boolean[] eventActs;
    private int mPrimaryColor;

    private View eventStepContent;

    public EventActsStep(String stepTitle) {
        super(stepTitle, "");
    }

    public EventActsStep(String stepTitle, Context context) {
        super(stepTitle, "");
    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        eventStepContent = inflater.inflate(R.layout.step_event_acts_layout, null, false);
        setUpEventActs();

        return eventStepContent;
    }

    private void setUpEventActs() {
        boolean firstSetup = eventActs == null;
        eventActs = firstSetup ? new boolean[NUM_EVENTS] : eventActs;

        final TypedArray eventActsImg = getContext().getResources()
                .obtainTypedArray(R.array.event_acts_img);
        for(int i = 0; i < eventActsImg.length(); i++) {
            final int index = i;
            final View actLayout = getActLayout(index);

            updateActsLayout(index, actLayout, false);

            if (actLayout != null) {
                actLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventActs[index] = !eventActs[index];
                        updateActsLayout(index, actLayout, true);
                        markAsCompletedOrUncompleted(true);
                    }
                });

                final ImageView eventAct = actLayout.findViewById(R.id.eventImage);
                eventAct.setImageResource(eventActsImg.getResourceId(index, -1));
            }
        }
        eventActsImg.recycle();
    }

    private View getActLayout(int i) {
        int id = eventStepContent.getResources().getIdentifier(
                "event_" + (1 +i), "id", getContext().getPackageName());
        return eventStepContent.findViewById(id);
    }

    private void updateActsLayout(int eventIndex, View eventLayout, boolean useAnimations) {
        if (eventActs[eventIndex]) {
            markEventAct(eventIndex, eventLayout, useAnimations);
        } else {
            unmarkEventAct(eventIndex, eventLayout, useAnimations);
        }
    }

    private void markEventAct(int eventIndex, View eventLayout, boolean useAnimations) {
        eventActs[eventIndex] = true;

        if (eventLayout != null) {
            Drawable bg = ContextCompat.getDrawable(getContext(), ernestoyaquello.com.verticalstepperform.R.drawable.circle_step_done);
            bg.setColorFilter(new PorterDuffColorFilter(mPrimaryColor, PorterDuff.Mode.SRC_IN));
            eventLayout.setBackground(bg);

            ImageView eventImg = eventLayout.findViewById(R.id.eventImage);
            eventImg.setColorFilter(Color.rgb(255, 255, 255));
        }
    }

    private void unmarkEventAct(int eventIndex, View eventLayout, boolean useAnimations) {
        eventActs[eventIndex] = false;

        eventLayout.setBackgroundResource(0);

        ImageView eventImg = eventLayout.findViewById(R.id.eventImage);
        eventImg.setColorFilter(mPrimaryColor);
    }

    public void setPrimaryColor(int colorPrimary) {
        mPrimaryColor = colorPrimary;
    }

    @Override
    public boolean[] getStepData() {
        return eventActs;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String[] eventActStrings = getContext().getResources().getStringArray(R.array.event_acts);
        List<String> selectedActsStrings = new ArrayList<>();
        for (int i = 0; i < eventActStrings.length; i++) {
            if (eventActs[i]) {
                selectedActsStrings.add(eventActStrings[i]);
            }
        }

        return TextUtils.join(", ", selectedActsStrings);
    }

    @Override
    protected void onStepOpened(boolean animated) {
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
    public void restoreStepData(boolean[] actData) {
        // To restore the step after a configuration change
        eventActs = actData;
        setUpEventActs();
        markAsCompletedOrUncompleted(true);
    }

    @Override
    protected IsDataValid isStepDataValid(boolean[] actsData) {
        boolean isEventSelected = false;
        for(int i = 0; i < actsData.length && !isEventSelected; i++) {
            if(actsData[i]) {
                isEventSelected = true;
            }
        }

        return isEventSelected
                ? new IsDataValid(true)
                : new IsDataValid(false, getContext().getString(R.string.stepper_error_min_events));
    }

}