package com.example.jojo.obsido.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.R;
import com.example.jojo.obsido.form.steps.EventActsStep;
import com.example.jojo.obsido.form.steps.EventCommentsStep;
import com.example.jojo.obsido.form.steps.EventDateStep;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class AddEditEventActivity extends AppCompatActivity implements StepperFormListener {

    private static final String LOG_TAG = "AddEditEventActivity".getClass().getSimpleName();

    public static final String EXTRA_EVENT_ID =
            "com.example.jojo.obsido.EXTRA_EVENT_ID";
    public static final String EXTRA_EVENT_DATE =
            "com.example.jojo.obsido.EXTRA_EVENT_DATE";
    public static final String EXTRA_EVENT_POSITIONS =
            "com.example.jojo.obsido.EXTRA_EVENT_POSITIONS";
    public static final String EXTRA_EVENT_RATING =
            "com.example.jojo.obsido.EXTRA_EVENT_RATING";
    public static final String EXTRA_EVENT_COMMENTS =
            "com.example.jojo.obsido.EXTRA_EVENT_COMMENTS";

    // Vertical Stepper form element
    private EventCommentsStep mEventCommentsStep;
    private EventDateStep mEventDateStep;
    private EventActsStep mEventActsStep;

    // Shared Preferences Theme color values
    private int mColorPrimary;
    private int mColorPrimaryDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_stepper_form);

        initToolbar();

        mEventDateStep = new EventDateStep(getResources()
                .getString(R.string.stepper_add_event_date), getApplicationContext());
        mEventCommentsStep = new EventCommentsStep(getResources()
                .getString(R.string.stepper_add_event_comments), getApplicationContext());
        mEventActsStep = new EventActsStep(getResources()
                .getString(R.string.stepper_add_event_acts) , getApplicationContext());
        mEventActsStep.setPrimaryColor(mColorPrimary);
        
        initVerticalStepper();

        Intent intent = getIntent();

        if(!intent.hasExtra(EXTRA_EVENT_ID)) {
            setTitle(R.string.stepper_add_event);
        } else {
            setTitle(R.string.stepper_edit_event);

            //mEventDateStep.restoreStepData(intent.getStringExtra(EXTRA_EVENT_DATE));
            mEventCommentsStep.restoreStepData(intent.getStringExtra(EXTRA_EVENT_COMMENTS));
            //mEventActsStep.restoreStepData(intent.getStringExtra(EXTRA_EVENT_COMMENTS));

        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    private void saveEvent() {
        String eventDate = mEventDateStep.getStepDataAsHumanReadableString();
        String eventComments = mEventCommentsStep.getStepData();
        //boolean[] eventActs = mPartnerAgeStep.getStepDataAsHumanReadableString();
        //String partnerGender = Integer.toString(mPartnerGenderStep.getStepData());

        Intent data = new Intent();
        data.putExtra(EXTRA_EVENT_DATE, eventDate);
        data.putExtra(EXTRA_EVENT_COMMENTS, eventComments);
        //data.putExtra(EXTRA_PARTNER_AGE, Integer.parseInt(partnerAge));
        //data.putExtra(EXTRA_PARTNER_GENDER, Integer.parseInt(partnerGender));

        int id = getIntent().getIntExtra(EXTRA_EVENT_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_EVENT_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onCompletedForm() {
        // This method will be called when the user clicks on the last confirmation button of the
        // form in an attempt to save or send the data.
        Log.v(LOG_TAG, "onCompletedForm: vertical stepper form completed.");
        saveEvent();
    }

    @Override
    public void onCancelledForm() {
        // This method will be called when the user clicks on the cancel button of the form.
        Log.v(LOG_TAG, "onCancelledForm: vertical stepper form cancelled.");

    }

    private void initVerticalStepper() {
        Log.v(LOG_TAG, "loadVerticalStepperSharedPreferences: called.");
        VerticalStepperFormView verticalStepperForm = findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, mEventDateStep, mEventActsStep, mEventCommentsStep)
                .stepNumberColors(mColorPrimary,
                        getResources().getColor(R.color.verticalStepperTextColor))
                .init();
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

        getMenuInflater().inflate(R.menu.menu_event_editor, menu);
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
                                NavUtils.navigateUpFromSameTask(AddEditEventActivity.this);
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
