package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;

import com.example.jojo.obsido.utils.SharedPreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.example.jojo.obsido.R;

import com.example.jojo.obsido.SettingsActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "CalendarActivity".getClass().getSimpleName();

    // UI XML Views
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private CalendarView mCalendarView;
    private boolean mEventsHidden = false;

    // Theme colors
    private int mColorPrimary;
    private int mColorPrimaryDark;
    private int mColorPrimaryAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);

        initFab();
        initToolbar();
        initDrawer();
        initCalendarView();
        
        initEvents();
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
        mColorPrimaryAccent = SharedPreferenceUtils.getColorAccent(getTheme());
        mColorPrimaryDark = SharedPreferenceUtils.getColorPrimaryDark(getTheme());
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            try {
                getSupportActionBar().setTitle(mCalendarView.getCalendarTitleDate());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Calendar View activity FAB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(mColorPrimaryAccent));
    }

    private void initDrawer() {
        mDrawer =  findViewById(R.id.drawer_layout);
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

    private void initCalendarView() {
        mCalendarView = findViewById(R.id.calendarView);

        mCalendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Log.v(LOG_TAG, "initCalendarView: calendarView moved backwards");
                if(getSupportActionBar() != null) {
                    try {
                        getSupportActionBar().setTitle(mCalendarView.getCalendarTitleDate());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mCalendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Log.v(LOG_TAG, "initCalendarView: calendarView moved forward");
                if(getSupportActionBar() != null) {
                    try {
                        getSupportActionBar().setTitle(mCalendarView.getCalendarTitleDate());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Changes the Calendar View colors
        if (mCalendarView != null) {
            mCalendarView.setSelectionColor(mColorPrimary);
            mCalendarView.setEventDayColor(mColorPrimary);
            mCalendarView.setWeekDayBarColor(mColorPrimary);
            mCalendarView.setTodayColor(mColorPrimaryAccent);
            mCalendarView.setEventIconColor(mColorPrimaryDark);
        }
    }

    private void initEvents() {
        List<EventDay> eventDays = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 12);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 16);

        Drawable draw = getResources().getDrawable(R.drawable.test_event);

        eventDays.add(new EventDay(calendar1, draw));
        eventDays.add(new EventDay(calendar2, draw));

        mCalendarView.setEvents(eventDays);
        Log.v(LOG_TAG, "onCreate: number of events added" + eventDays.size());
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
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_calendar_today:
                Log.v(LOG_TAG, "onOptionsItemSelected: clicked the calendar today icon.");
                setCalendarToday();
                return true;

            case R.id.action_hide_events:
                Log.v(LOG_TAG, "onOptionsItemSelected: clicked the hide events tab.");
                if(mEventsHidden) {
                    mCalendarView.showCalendarEvents();
                    item.setTitle(R.string.action_hide_events);
                    mEventsHidden = false;
                } else {
                    mCalendarView.hideCalendarEvents();
                    item.setTitle(R.string.action_show_events);
                    mEventsHidden = true;
                }
                return true;

            case R.id.action_settings:
                Log.v(LOG_TAG, "onOptionsItemSelected:: opened Settings activity");
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCalendarToday() {
        Calendar calToday = Calendar.getInstance();

        try {
            mCalendarView.setDate(calToday);
        } catch(OutOfDateRangeException e) {
            e.printStackTrace();
        }
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
                break;

            case R.id.nav_partners:
                Log.v(LOG_TAG, "onNavigationItemSelected: navigation partners item selected");
                Intent partnersListIntent = new Intent(this, PartnersListActivity.class);
                startActivity(partnersListIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                Log.v(LOG_TAG, "onNavigationItemSelected: default item selected");
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
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
