package com.example.jojo.obsido.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.R;
import com.example.jojo.obsido.form.steps.PartnerAgeStep;
import com.example.jojo.obsido.form.steps.PartnerDescriptionStep;
import com.example.jojo.obsido.form.steps.PartnerGenderStep;
import com.example.jojo.obsido.form.steps.PartnerNameStep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class EditProfileActivity extends AppCompatActivity implements StepperFormListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "EditProfileActivity".getClass().getSimpleName();

    private PartnerNameStep mPartnerNameStep;
    private PartnerDescriptionStep mPartnerDescriptionStep;
    private PartnerGenderStep mPartnerGenderStep;
    private PartnerAgeStep mPartnerAgeStep;

    // UI XML Views
    private VerticalStepperFormView verticalStepperForm;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_form);

        initToolbar();

        mPartnerNameStep = new PartnerNameStep(getResources()
                .getString(R.string.stepper_add_partner_name), getApplicationContext());
        mPartnerDescriptionStep = new PartnerDescriptionStep(getResources()
                .getString(R.string.stepper_add_partner_description), getApplicationContext());
        mPartnerAgeStep = new PartnerAgeStep(getResources()
                .getString(R.string.stepper_add_partner_age));
        mPartnerGenderStep = new PartnerGenderStep(getResources()
                .getString(R.string.stepper_add_partner_gender));


        verticalStepperForm = findViewById(R.id.stepper_form);

        loadVerticalStepperSharedPreferences(mSharedPreferences);

        // TODO: Change this when data syncing is updated to ( SQL --> Firebase && Firebase --> SQL)
        Intent intent = getIntent();
        Uri partnerUri = intent.getData();

        if(partnerUri == null) {
            setTitle(R.string.stepper_add_partner);
        } else {
            setTitle(R .string.stepper_edit_partner);
        }
    }

    private void setUpSharedPreference() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadThemeFromPreferences(mSharedPreferences);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadThemeFromPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: load theme from preferences.");
        if(sharedPreferenceTheme != null) {
            try {
                if (sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
                    setTheme(R.style.AppThemeRed);

                    Log.v(LOG_TAG, "RED theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
                    setTheme(R.style.AppThemeBlue);

                    Log.v(LOG_TAG, "BLUE theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
                    setTheme(R.style.AppThemeGreen);

                    Log.v(LOG_TAG, "GREEN theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
                    setTheme(R.style.AppThemePink);

                    Log.v(LOG_TAG, "PINK theme from Shared Preferences.");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
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

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void loadVerticalStepperSharedPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadVerticalStepperSharedPreferences: load theme from preferences.");

        if(sharedPreferenceTheme != null) {
            try {
                if (sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
                    verticalStepperForm
                            .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                            .stepNumberColors(getResources().getColor(R.color.colorPrimaryRed),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .nextButtonColors(getResources().getColor(R.color.colorPrimaryRed),
                                    getResources().getColor(R.color.colorPrimaryRedDark),
                                    getResources().getColor(R.color.verticalStepperTextColor),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryRedDark))
                            .init();


                    mPartnerAgeStep.setAgePickerDividerColor(getResources().getColor(R.color.colorPrimaryRed));
                    mPartnerAgeStep.setAgePickerSelectedTextColor(getResources().getColor(R.color.colorPrimaryRed));

                    mPartnerGenderStep.setPrimaryColor(getResources().getColor(R.color.colorPrimaryRed));

                    Log.v(LOG_TAG, "RED theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
                    verticalStepperForm
                            .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                            .stepNumberColors(getResources().getColor(R.color.colorPrimaryBlue),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .nextButtonColors(getResources().getColor(R.color.colorPrimaryBlue),
                                    getResources().getColor(R.color.colorPrimaryBlueDark),
                                    getResources().getColor(R.color.verticalStepperTextColor),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryBlueDark))
                            .init();

                    mPartnerAgeStep.setAgePickerDividerColor(getResources().getColor(R.color.colorPrimaryBlue));
                    mPartnerAgeStep.setAgePickerSelectedTextColor(getResources().getColor(R.color.colorPrimaryBlue));

                    mPartnerGenderStep.setPrimaryColor(getResources().getColor(R.color.colorPrimaryBlue));


                    Log.v(LOG_TAG, "BLUE theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
                    verticalStepperForm
                            .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                            .stepNumberColors(getResources().getColor(R.color.colorPrimaryGreen),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .nextButtonColors(getResources().getColor(R.color.colorPrimaryGreen),
                                    getResources().getColor(R.color.colorPrimaryGreenDark),
                                    getResources().getColor(R.color.verticalStepperTextColor),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryGreenDark))
                            .init();

                    mPartnerAgeStep.setAgePickerDividerColor(getResources().getColor(R.color.colorPrimaryGreen));
                    mPartnerAgeStep.setAgePickerSelectedTextColor(getResources().getColor(R.color.colorPrimaryGreen));

                    mPartnerGenderStep.setPrimaryColor(getResources().getColor(R.color.colorPrimaryGreen));


                    Log.v(LOG_TAG, "GREEN theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
                    verticalStepperForm
                            .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                            .stepNumberColors(getResources().getColor(R.color.colorPrimaryPink),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .nextButtonColors(getResources().getColor(R.color.colorPrimaryPink),
                                    getResources().getColor(R.color.colorPrimaryPinkDark),
                                    getResources().getColor(R.color.verticalStepperTextColor),
                                    getResources().getColor(R.color.verticalStepperTextColor))
                            .errorMessageTextColor(getResources().getColor(R.color.colorPrimaryPinkDark))
                            .init();

                    mPartnerAgeStep.setAgePickerDividerColor(getResources().getColor(R.color.colorPrimaryPink));
                    mPartnerAgeStep.setAgePickerSelectedTextColor(getResources().getColor(R.color.colorPrimaryPink));

                    mPartnerGenderStep.setPrimaryColor(getResources().getColor(R.color.colorPrimaryPink));


                    Log.v(LOG_TAG, "PINK theme from Shared Preferences.");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getSupportActionBar() != null) {
            try {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        getMenuInflater().inflate(R.menu.menu_partners_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // TODO: What to do when the form is completed
                return true;

            case android.R.id.home:
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditProfileActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.action_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.action_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the partner.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onBackPressed() {
        // If the partner hasn't changed, continue with handling back button press
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
