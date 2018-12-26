package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;

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
    private View mHeaderView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private FloatingActionButton mFab;

    private CalendarView mCalendarView;
    private List<EventDay> mEventDays;
    private boolean mEventsHidden = false;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();
        setContentView(R.layout.activity_calendar);

        super.onCreate(savedInstanceState);

        initActionBar();
        initFab();
        initDrawer();
        initCalendarView();
        initEvents();

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
        if(sharedPreferenceTheme != null) {
            try {
                if (sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
                    setTheme(R.style.AppThemeRed);

                    if (mHeaderView != null) {
                        mHeaderView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                    if (mCalendarView != null) {
                        mCalendarView.setSelectionColor(getResources().getColor(R.color.colorPrimaryRed));
                        mCalendarView.setEventDayColor(getResources().getColor(R.color.colorPrimaryRed));
                        mCalendarView.setWeekDayBarColor(getResources().getColor(R.color.colorPrimaryRed));
                        mCalendarView.setTodayColor(getResources().getColor(R.color.colorRedAccent));
                    }

                    Log.v(LOG_TAG, "RED theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
                    setTheme(R.style.AppThemeBlue);

                    if (mHeaderView != null) {
                        mHeaderView.setBackgroundResource(R.color.colorPrimaryBlue);
                    }

                    if (mCalendarView != null) {
                        mCalendarView.setSelectionColor(getResources().getColor(R.color.colorPrimaryBlue));
                        mCalendarView.setEventDayColor(getResources().getColor(R.color.colorPrimaryBlue));
                        mCalendarView.setWeekDayBarColor(getResources().getColor(R.color.colorPrimaryBlue));
                        mCalendarView.setTodayColor(getResources().getColor(R.color.colorBlueAccent));
                    }

                    Log.v(LOG_TAG, "BLUE theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
                    setTheme(R.style.AppThemeGreen);

                    if (mHeaderView != null) {
                        mHeaderView.setBackgroundResource(R.color.colorPrimaryGreen);
                    }


                    if (mCalendarView != null) {
                        mCalendarView.setSelectionColor(getResources().getColor(R.color.colorPrimaryGreen));
                        mCalendarView.setEventDayColor(getResources().getColor(R.color.colorPrimaryGreen));
                        mCalendarView.setWeekDayBarColor(getResources().getColor(R.color.colorPrimaryGreen));
                        mCalendarView.setTodayColor(getResources().getColor(R.color.colorGreenAccent));
                    }


                    Log.v(LOG_TAG, "GREEN theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
                    setTheme(R.style.AppThemePink);

                    if (mHeaderView != null) {
                        mHeaderView.setBackgroundResource(R.color.colorPrimaryPink);
                    }

                    if (mCalendarView != null) {
                        mCalendarView.setSelectionColor(getResources().getColor(R.color.colorPrimaryPink));
                        mCalendarView.setEventDayColor(getResources().getColor(R.color.colorPrimaryPink));
                        mCalendarView.setWeekDayBarColor(getResources().getColor(R.color.colorPrimaryPink));
                        mCalendarView.setTodayColor(getResources().getColor(R.color.colorPinkAccent));
                    }

                    Log.v(LOG_TAG, "PINK theme from Shared Preferences.");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void initActionBar() {
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
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Calendar View activity FAB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void initDrawer() {
        mDrawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);
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
    }

    private void initEvents() {
        mEventDays = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 12);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 16);

        Drawable draw = getResources().getDrawable(R.drawable.test_event);
        draw.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        mEventDays.add(new EventDay(calendar1, draw));
        mEventDays.add(new EventDay(calendar2, draw));

        mCalendarView.setEvents(mEventDays);
        Log.v(LOG_TAG, "onCreate: number of events added" + mEventDays.size());
    }

    private void loadFabColorFromPreferences() {
        String sharedPreferenceTheme = mSharedPreferences.getString(getString(R.string.pref_theme_key),
                getString(R.string.pref_show_red_theme_label));

        Log.v(LOG_TAG, "loadThemeFromPreferences: load theme from preferences.");
        if(sharedPreferenceTheme != null) {
            try {
                if (sharedPreferenceTheme.equals(getString(R.string.pref_show_red_theme_key))) {
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorRedAccent));

                    Log.v(LOG_TAG, "RED FAB theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_blue_theme_key))) {
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlueAccent));

                    Log.v(LOG_TAG, "BLUE FAB theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_green_theme_key))) {
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreenAccent));

                    Log.v(LOG_TAG, "GREEN FAB theme from Shared Preferences.");
                } else if (sharedPreferenceTheme.equals(getString(R.string.pref_show_pink_theme_key))) {
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPinkAccent));

                    Log.v(LOG_TAG, "PINK FAB theme from Shared Preferences.");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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
            try {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(this, R.string.shared_preferences_theme_changed, Toast.LENGTH_SHORT).show();

                startActivity(i);
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
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
