package com.example.jojo.obsido.form.steps;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import ernestoyaquello.com.verticalstepperform.Step;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PartnerDescriptionStep extends Step<String> {

    private EditText partnerDescriptionView;

    private InputMethodManager im;

    private Context mContext;

    public PartnerDescriptionStep(String stepTitle) {
        super(stepTitle);
    }

    public PartnerDescriptionStep(String stepTitle, Context context) {
        super(stepTitle);
        mContext = context;
    }



    @Override
    protected View createStepContentLayout() {
        // Here we generate the view that will be used by the library as the content of the step.
        // In this case we do it programmatically, but we could also do it by inflating an XML layout.
        partnerDescriptionView = new EditText(getContext());

        if(mContext != null) {
            im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        }

        partnerDescriptionView.setSingleLine(true);
        partnerDescriptionView.setHint("Description");

        partnerDescriptionView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Whenever the user updates the user name text, we update the state of the step.
                // The step will be marked as completed only if its data is valid, which will be
                // checked with a call to isStepDataValid().
                markAsCompletedOrUncompleted(true);
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        return partnerDescriptionView;
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(true);
    }

    @Override
    public String getStepData() {
        // We get the step's data from the value that the user has typed in the EditText view.
        Editable userDescription = partnerDescriptionView.getText();
        return userDescription != null ? userDescription.toString() : "";
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        // Because the step's data is already a human-readable string, we don't need to convert it.
        // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
        // This string will be displayed in the subtitle of the step whenever the step gets closed.
        String userDescription = getStepData();
        return userDescription == null || userDescription.isEmpty() ? "" : userDescription;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // This will be called automatically whenever the step gets opened.
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // This will be called automatically whenever the step gets closed.

        // This closes the soft keyboard
        if(im != null && mContext !=null) {
                im.hideSoftInputFromWindow(partnerDescriptionView.getWindowToken(), 0);
        }
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
    public void restoreStepData(String stepData) {
        // To restore the step after a configuration change, we restore the text of its EditText view.
        if(partnerDescriptionView != null) {
            partnerDescriptionView.setText(stepData);
        }
    }



}