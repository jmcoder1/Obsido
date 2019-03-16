package com.example.jojo.obsido.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.R;
import com.example.jojo.obsido.form.steps.PartnerAgeStep;
import com.example.jojo.obsido.form.steps.PartnerDescriptionStep;
import com.example.jojo.obsido.form.steps.PartnerGenderStep;
import com.example.jojo.obsido.form.steps.PartnerNameStep;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class AddEditProfileActivity extends AppCompatActivity implements StepperFormListener {

    private static final String LOG_TAG = "AddEditProfileActivity".getClass().getSimpleName();

    public static final String EXTRA_PARTNER_ID =
            "com.example.jojo.obsido.EXTRA_PARTNER_ID";
    public static final String EXTRA_PARTNER_NAME =
            "com.example.jojo.obsido.EXTRA_PARTNER_NAME";
    public static final String EXTRA_PARTNER_DESCRIPTION =
            "com.example.jojo.obsido.EXTRA_PARTNER_DESCRIPTION";
    public static final String EXTRA_PARTNER_GENDER =
            "com.example.jojo.obsido.EXTRA_PARTNER_GENDER";
    public static final String EXTRA_PARTNER_AGE =
            "com.example.jojo.obsido.EXTRA_PARTNER_AGE";

    // Vertical Stepper form elements
    private PartnerNameStep mPartnerNameStep;
    private PartnerDescriptionStep mPartnerDescriptionStep;
    private PartnerGenderStep mPartnerGenderStep;
    private PartnerAgeStep mPartnerAgeStep;

    // Shared Preferences Theme color values
    private int mColorPrimary;
    private int mColorPrimaryDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_stepper_form);

        initToolbar();

        mPartnerNameStep = new PartnerNameStep(getResources()
                .getString(R.string.stepper_add_partner_name), getApplicationContext());
        mPartnerDescriptionStep = new PartnerDescriptionStep(getResources()
                .getString(R.string.stepper_add_partner_description), getApplicationContext());
        mPartnerAgeStep = new PartnerAgeStep(getResources()
                .getString(R.string.stepper_add_partner_age));
        mPartnerGenderStep = new PartnerGenderStep(getResources()
                .getString(R.string.stepper_add_partner_gender));

        initVerticalStepper();

        Intent intent = getIntent();

        if(!intent.hasExtra(EXTRA_PARTNER_ID)) {
            setTitle(R.string.stepper_add_partner);
        } else {
            setTitle(R .string.stepper_edit_partner);

            mPartnerNameStep.restoreStepData(intent.getStringExtra(EXTRA_PARTNER_NAME));
            mPartnerDescriptionStep.restoreStepData(intent.getStringExtra(EXTRA_PARTNER_DESCRIPTION));
            mPartnerAgeStep.restoreStepData(intent.getIntExtra(EXTRA_PARTNER_AGE, 0));
            mPartnerGenderStep.restoreStepData(intent.getIntExtra(EXTRA_PARTNER_GENDER,0));
        }
    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setThemeFromSharedPreferences(sharedPreferences);
    }

    private void setThemeFromSharedPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));
        Log.v(LOG_TAG, "loadThemeFromPreferences: called.");

        // Sets the activity theme based on the user Shared Preferences choice
        if(sharedPreferenceTheme != null) {
            try {
                if (sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
                    setTheme(R.style.AppThemeRed);
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
                    setTheme(R.style.AppThemeBlue);
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
                    setTheme(R.style.AppThemeGreen);
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
                    setTheme(R.style.AppThemePink);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        mColorPrimary = SharedPreferenceUtils.getColorPrimary(getTheme());
        mColorPrimaryDark = SharedPreferenceUtils.getColorPrimaryDark(getTheme());
    }

    private void savePartner() {
        String partnerName = mPartnerNameStep.getStepData();
        String partnerDescription = mPartnerDescriptionStep.getStepData();
        String partnerAge = mPartnerAgeStep.getStepDataAsHumanReadableString();
        String partnerGender = Integer.toString(mPartnerGenderStep.getStepData());

        Intent data = new Intent();
        data.putExtra(EXTRA_PARTNER_NAME, partnerName);
        data.putExtra(EXTRA_PARTNER_DESCRIPTION, partnerDescription);
        data.putExtra(EXTRA_PARTNER_AGE, Integer.parseInt(partnerAge));
        data.putExtra(EXTRA_PARTNER_GENDER, Integer.parseInt(partnerGender));

        int id = getIntent().getIntExtra(EXTRA_PARTNER_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_PARTNER_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onCompletedForm() {
        // This method will be called when the user clicks on the last confirmation button of the
        // form in an attempt to save or send the data.
        Log.v(LOG_TAG, "onCompletedForm: vertical stepper form completed.");
        savePartner();
    }

    @Override
    public void onCancelledForm() {
        // This method will be called when the user clicks on the cancel button of the form.
        Log.v(LOG_TAG, "onCancelledForm: vertical stepper form cancelled.");

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initVerticalStepper() {
        Log.v(LOG_TAG, "loadVerticalStepperSharedPreferences: called.");
        VerticalStepperFormView verticalStepperForm = findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                .stepNumberColors(mColorPrimary,
                        getResources().getColor(R.color.verticalStepperTextColor))
                .init();

        mPartnerGenderStep.setPrimaryColor(mColorPrimary);

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
                // TODO: Only run if isCompleted savePartner();
                return true;

            case android.R.id.home:
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddEditProfileActivity.this);
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

}
