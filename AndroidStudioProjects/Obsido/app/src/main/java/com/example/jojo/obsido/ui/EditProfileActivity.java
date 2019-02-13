package com.example.jojo.obsido.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.AppDatabase;
import com.example.jojo.obsido.Partner;
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

import androidx.room.Room;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class EditProfileActivity extends AppCompatActivity implements StepperFormListener {

    private static final String LOG_TAG = "EditProfileActivity".getClass().getSimpleName();
    public static AppDatabase mAppDatabase;

    // Vertical Stepper form elements
    private VerticalStepperFormView verticalStepperForm;
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

        initVerticalStepper();

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

    @Override
    public void onCompletedForm() {
        // This method will be called when the user clicks on the last confirmation button of the
        // form in an attempt to save or send the data.
        Log.v(LOG_TAG, "onCompletedForm: vertical stepper form completed.");

        String partnerName = mPartnerNameStep.getStepData();
        String partnerDescription = mPartnerDescriptionStep.getStepData();
        int partnerAge = mPartnerAgeStep.getStepData();
        int partnerGender = mPartnerGenderStep.getStepData();

        /*Partner partner = new Partner();
        partner.setName(partnerName);
        partner.setDescription(partnerDescription);
        partner.setAge(partnerAge);
        partner.setGender(partnerGender);

        PartnersListActivity.mAppDatabase.partnerDao().insert(partner);*/
        finish();
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
        verticalStepperForm = findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, mPartnerNameStep, mPartnerDescriptionStep, mPartnerAgeStep, mPartnerGenderStep)
                .stepNumberColors(mColorPrimary,
                        getResources().getColor(R.color.verticalStepperTextColor))
                .nextButtonColors(mColorPrimary, mColorPrimaryDark,
                        getResources().getColor(R.color.verticalStepperTextColor),
                        getResources().getColor(R.color.verticalStepperTextColor))
                .errorMessageTextColor(mColorPrimaryDark)
                .init();


        mPartnerAgeStep.setAgePickerDividerColor(mColorPrimary);
        mPartnerAgeStep.setAgePickerSelectedTextColor(mColorPrimary);

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
