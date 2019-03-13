package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.fragments.PartnerProfileTopFragment;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.SettingsActivity;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class PartnerProfileActivity extends AppCompatActivity implements
         SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "PartnerProfileActivity".getClass().getSimpleName();

    // Shared Preferences Theme color values
    private int mColorPrimary;
    private int mColorPrimaryDark;
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

        Intent intent = getIntent();

        mPartnerName = intent.getStringExtra(EXTRA_PARTNER_NAME);
        mPartnerDescription = intent.getStringExtra(EXTRA_PARTNER_DESCRIPTION);
        mPartnerAge = intent.getIntExtra(EXTRA_PARTNER_AGE, 0);
        mPartnerGender = intent.getIntExtra(EXTRA_PARTNER_GENDER,0);

        FragmentManager fragmentManager = getSupportFragmentManager();

        PartnerProfileTopFragment topFragment = new PartnerProfileTopFragment();
        topFragment.setPartnerName(mPartnerName);
        topFragment.setPartnerDescription(mPartnerDescription);

        fragmentManager.beginTransaction()
                .add(R.id.partner_profile_top_container, topFragment)
                .commit();

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
        mColorPrimaryDark = SharedPreferenceUtils.getColorPrimaryDark(getTheme());
        mColorPrimaryAccent = SharedPreferenceUtils.getColorAccent(getTheme());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.v(LOG_TAG, "onOptionsItemSelected:: opened Settings activity");
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

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
