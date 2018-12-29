package com.example.jojo.obsido.form.steps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojo.obsido.R;

import androidx.annotation.NonNull;
import ernestoyaquello.com.verticalstepperform.Step;

public class PartnerGenderStep extends Step<Integer> {
    private static final String LOG_TAG = "PartnerGenderStep".getClass().getSimpleName();

    private static final int GENDER_MALE = 0;
    private static final int GENDER_FEMALE = 1;
    private static final int GENDER_NONE = -1;

    private int partnerGenderEnum = GENDER_NONE;

    private ImageView lastGenderIcon;
    private TextView lastGenderLabel;

    private int mPrimaryColor;

    public PartnerGenderStep(String title) {
        this(title, "");
    }

    public PartnerGenderStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View genderStepContent = inflater.inflate(R.layout.step_genders_layout, null, false);

        setupPartnerGender(genderStepContent);

        return genderStepContent;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepClosed(boolean animated) {
        Log.v(LOG_TAG, "onStepClosed: called");
        updateSubtitle(getStepDataAsHumanReadableString(), false);
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    public Integer getStepData() {
        return partnerGenderEnum;
    }

    @Override
    public String getStepDataAsHumanReadableString() {

        if(partnerGenderEnum == GENDER_MALE) {
            return getContext().getResources().getString(R.string.stepper_add_partner_gender_male);
        } else if(partnerGenderEnum == GENDER_FEMALE) {
            return getContext().getResources().getString(R.string.stepper_add_partner_gender_female);
        }

        return "";
    }

    @Override
    public void restoreStepData(Integer data) {
        partnerGenderEnum = data;
    }

    @Override
    protected IsDataValid isStepDataValid(Integer stepData) {
        boolean isValidGender = false;
        if(partnerGenderEnum == GENDER_MALE || partnerGenderEnum == GENDER_FEMALE) {
            isValidGender = true;
        }
        return new IsDataValid(isValidGender);
    }

    private void setupPartnerGender(View genderStepContent) {
        ImageView maleGenderIcon = genderStepContent.findViewById(R.id.male);
        TextView maleGenderLabel = genderStepContent.findViewById(R.id.maleGenderLabel);

        ImageView femaleGenderIcon = genderStepContent.findViewById(R.id.female);
        TextView femaleGenderLabel = genderStepContent.findViewById(R.id.femaleGenderLabel);

        setGenderClickListener(maleGenderIcon, maleGenderLabel);
        setGenderClickListener(femaleGenderIcon, femaleGenderLabel);
    }

    public void setPrimaryColor(int color) {
        mPrimaryColor = color;
    }

    private void setGenderClickListener(ImageView genderIcon, TextView genderLabel) {
        View.OnClickListener genderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Makes the triple click keeps the selector visible
               if(lastGenderIcon == genderIcon && lastGenderLabel == genderLabel) {
                    return;
               }

               genderIcon.setColorFilter(mPrimaryColor);
               genderLabel.setTextColor(mPrimaryColor);

               // Null check for last gender icon
                if(lastGenderIcon != null && lastGenderLabel != null) {
                    lastGenderIcon.setColorFilter(getContext().getResources().getColor(R.color.textColorPrimary));
                    lastGenderLabel.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));

                }
                lastGenderIcon = genderIcon;
                lastGenderLabel = genderLabel;

                // Stores the current gender enumerated
                if(genderLabel.getText() == getContext().getResources().
                        getString(R.string.stepper_add_partner_gender_female)) {
                    partnerGenderEnum = GENDER_FEMALE;
                } else if(genderLabel.getText() == getContext().getResources()
                        .getString(R.string.stepper_add_partner_gender_male)) {
                    partnerGenderEnum = GENDER_MALE;
                }

                markAsCompletedOrUncompleted(true);

            }
        };

        genderLabel.setOnClickListener(genderClickListener);
        genderIcon.setOnClickListener(genderClickListener);
    }

}
