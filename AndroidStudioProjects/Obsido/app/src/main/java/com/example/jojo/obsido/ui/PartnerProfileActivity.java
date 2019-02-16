package com.example.jojo.obsido.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jojo.obsido.PartnerViewModel;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

public class PartnerProfileActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private PartnerViewModel partnerViewModel;
    private static final String LOG_TAG = "PartnerProfileActivity".getClass().getSimpleName();

    // UI XML View
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    // Shared Preferences Theme color values
    private int mColorPrimary;
    private int mColorPrimaryAccent;

    // Partner values
    private String mPartnerName, mPartnerDescription;
    private int mPartnerAge, mPartnerGender;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile);

        initToolbar();
        initDrawer();

        Intent intent = getIntent();

        mPartnerName = intent.getStringExtra(EXTRA_PARTNER_NAME);
        mPartnerDescription = intent.getStringExtra(EXTRA_PARTNER_DESCRIPTION);
        mPartnerAge = intent.getIntExtra(EXTRA_PARTNER_AGE, 0);
        mPartnerGender = intent.getIntExtra(EXTRA_PARTNER_GENDER,0);

        setTitle(mPartnerName);

        TextView text = findViewById(R.id.centreText);
        text.setText("Name: " + mPartnerName + "\n\nDescription: " + mPartnerDescription
            + "\n\nAge: " + Integer.toString(mPartnerAge) + "\n\nGender: " + Integer.toString(mPartnerGender));


    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setThemeFromSharedPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setThemeFromSharedPreferences(SharedPreferences sharedPreferences) {
        String sharedPreferenceTheme = sharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: called.");

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

        // Gets the values from the activity theme
        mColorPrimary = SharedPreferenceUtils.getColorPrimary(getTheme());
        mColorPrimaryAccent = SharedPreferenceUtils.getColorAccent(getTheme());
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initDrawer() {
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            headerView.setBackgroundColor(mColorPrimary);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // TODO: Only run if isCompleted savePartner();
                return true;

            case android.R.id.home:
                // TODO: Change this to remove the navigation dialog
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(PartnerProfileActivity.this);
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
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_partners_profile, menu);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_theme_key))) {
            Intent mIntent = getIntent();
            finish();
            startActivity(mIntent);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_overview:
                Log.v(LOG_TAG, "onNavigationItemSelected: navigation overview item selected");
                Intent overviewIntent = new Intent(this, OverviewActivity.class);
                startActivity(overviewIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.nav_calendar:
                Log.v(LOG_TAG, "onNavigationItemSelected: navigation calendar item selected");
                Intent calendarIntent = new Intent(this, CalendarActivity.class);
                startActivity(calendarIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.nav_partners:
                Log.v(LOG_TAG, "onNavigationItemSelected: navigation partners item selected");
                break;

            default:
                Log.v(LOG_TAG, "onNavigationItemSelected: default item selected");

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpSharedPreference();
    }
}
