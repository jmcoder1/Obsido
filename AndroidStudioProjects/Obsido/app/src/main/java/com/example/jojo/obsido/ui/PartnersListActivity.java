package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jojo.obsido.PartnerCursorAdapter;
import com.example.jojo.obsido.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.example.jojo.obsido.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

public class PartnersListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "PartnersListActivity".getClass().getSimpleName();

    // UI XML View
    private Toolbar mToolbar;
    private View mHeaderView;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawer;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_list);

        initFab();
        initToolbar();
        initDrawer();

        PartnerCursorAdapter partnerCursorAdapter = new PartnerCursorAdapter(this, null);
        ListView partnerListView = findViewById(R.id.partners_list);
        partnerListView.setAdapter(partnerCursorAdapter);

        View emptyView = findViewById(R.id.empty_view);
        partnerListView.setEmptyView(emptyView);
        partnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: what should happen when a partner from the list is clicked
            }
        });

        loadFabColorFromPreferences();
    }

    private void setUpSharedPreference() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadThemeFromPreferences();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadThemeFromPreferences() {
        String sharedPreferenceTheme = mSharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: load theme from preferences.");

        if(sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
            setTheme(R.style.AppThemeRed);

            if(mHeaderView != null) {
                mHeaderView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            Log.v(LOG_TAG, "RED theme from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
            setTheme(R.style.AppThemeBlue);

            if(mHeaderView != null) {
                mHeaderView.setBackgroundResource(R.color.colorPrimaryBlue);
            }

            Log.v(LOG_TAG, "BLUE theme from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
            setTheme(R.style.AppThemeGreen);

            if(mHeaderView != null) {
                mHeaderView.setBackgroundResource(R.color.colorPrimaryGreen);
            }

            Log.v(LOG_TAG, "GREEN theme from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
            setTheme(R.style.AppThemePink);

            if(mHeaderView != null) {
                mHeaderView.setBackgroundResource(R.color.colorPrimaryPink);
            }

            Log.v(LOG_TAG, "PINK theme from Shared Preferences.");
        }
    }

    private void initFab() {
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, "initFab: adding a partner flaoting action button");
                Intent addingPartnerIntent = new Intent(PartnersListActivity.this, EditProfileActivity.class);
                startActivity(addingPartnerIntent);
            }
        });

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
        mHeaderView = navigationView.getHeaderView(0);
    }

    private void loadFabColorFromPreferences() {
        String sharedPreferenceTheme = mSharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: load theme from preferences.");

        if(sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedAccent));

            Log.v(LOG_TAG, "loadThemeFromPreferences: RED themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlueAccent));

            Log.v(LOG_TAG, "BLUE themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreenAccent));

            Log.v(LOG_TAG, "loadThemeFromPreferences: GREEN themes from Shared Preferences.");
        } else if(sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPinkAccent));

            Log.v(LOG_TAG, "loadThemeFromPreferences: PINK themes from Shared Preferences.");
        }
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
        getMenuInflater().inflate(R.menu.menu_partners_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.v(LOG_TAG, "onOptionsItemSelected:: opened Settings activity");
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_theme_key))) {
            loadThemeFromPreferences();
            Intent intent = new Intent(this, PartnersListActivity.class);
            startActivity(intent);
            finish();
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
