package com.example.jojo.obsido.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.jojo.obsido.R;
import com.example.jojo.obsido.form.steps.PartnerAgeStep;
import com.example.jojo.obsido.form.steps.PartnerDescriptionStep;
import com.example.jojo.obsido.form.steps.PartnerNameStep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class EditProfileActivity extends AppCompatActivity implements StepperFormListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "EditProfileActivity".getClass().getSimpleName();

    private PartnerNameStep mPartnerNameStep;
    private PartnerDescriptionStep mPartnerDescriptionStep;
    // TODO: initialise - private PartnerGenderStep mPartnerGenderStep;
    private PartnerAgeStep mPartnerAgeStep;

    private VerticalStepperFormView verticalStepperForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_form);

        mPartnerNameStep = new PartnerNameStep(getResources()
                .getString(R.string.stepper_add_partner_name));
        mPartnerDescriptionStep = new PartnerDescriptionStep(getResources()
                .getString(R.string.stepper_add_partner_description));
        mPartnerAgeStep = new PartnerAgeStep(getResources()
                .getString(R.string.stepper_add_partner_age));

        verticalStepperForm = findViewById(R.id.stepper_form);
        setUpSharedPreference();

    }


    @Override
    public void onCompletedForm() {
        // This method will be called when the user clicks on the last confirmation button of the
        // form in an attempt to save or send the data.
        Log.v(LOG_TAG, "onCompletedForm: vertical stepper form completed.");

    }

    @Override
    public void onCancelledForm() {
        // This method will be called when the user clicks on the cancel button of the form.
        Log.v(LOG_TAG, "onCancelledForm: vertical stepper form cancelled.");

    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadThemeFromPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadThemeFromPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: load theme from preferences.");

        if(sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
            setTheme(R.style.AppThemeRed);

            verticalStepperForm
                    .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep)
                    .stepNumberColors(getResources().getColor(R.color.colorPrimaryRed),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .nextButtonColors(getResources().getColor(R.color.colorPrimaryRed),
                            getResources().getColor(R.color.colorPrimaryRedDark),
                            getResources().getColor(R.color.verticalStepperTextColor),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryRedDark))
                    .init();

            Log.v(LOG_TAG, "loadThemeFromPreferences: RED themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
            setTheme(R.style.AppThemeBlue);

            verticalStepperForm
                    .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep)
                    .stepNumberColors(getResources().getColor(R.color.colorPrimaryBlue),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .nextButtonColors(getResources().getColor(R.color.colorPrimaryBlue),
                            getResources().getColor(R.color.colorPrimaryBlueDark),
                            getResources().getColor(R.color.verticalStepperTextColor),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryBlueDark))
                    .init();

            Log.v(LOG_TAG, "loadThemeFromPreferences: BLUE themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
            setTheme(R.style.AppThemeGreen);

            verticalStepperForm
                    .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep)
                    .stepNumberColors(getResources().getColor(R.color.colorPrimaryGreen),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .nextButtonColors(getResources().getColor(R.color.colorPrimaryGreen),
                            getResources().getColor(R.color.colorPrimaryGreenDark),
                            getResources().getColor(R.color.verticalStepperTextColor),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryGreenDark))
                    .init();

            Log.v(LOG_TAG, "loadThemeFromPreferences: GREEN themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
            setTheme(R.style.AppThemePink);

            verticalStepperForm
                    .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep)
                    .stepNumberColors(getResources().getColor(R.color.colorPrimaryPink),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .nextButtonColors(getResources().getColor(R.color.colorPrimaryPink),
                            getResources().getColor(R.color.colorPrimaryPinkDark),
                            getResources().getColor(R.color.verticalStepperTextColor),
                            getResources().getColor(R.color.verticalStepperTextColor))
                    .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryPinkDark))
                    .init();

            Log.v(LOG_TAG, "loadThemeFromPreferences: PINK themes from Shared Preferences.");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // TODO: onSharedPreferenceChanged
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
