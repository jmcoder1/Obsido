package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jojo.obsido.db.Event;
import com.example.jojo.obsido.db.EventViewModel;
import com.example.jojo.obsido.fragments.CalendarEventFragment;
import com.example.jojo.obsido.fragments.CalendarFragment;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.example.jojo.obsido.R;

import com.example.jojo.obsido.SettingsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, CalendarFragment.OnCalendarDatePass,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "CalendarActivity".getClass().getSimpleName();

    private CalendarFragment mCalendarFragment;
    private List<Event> mCalendarEvents;
    private Calendar currentCalendarDate;

    @Override
    public void onCalendarMonthPass(Calendar calendarDate) {
        currentCalendarDate = calendarDate;
        mCalendarEvents = new ArrayList<>();
        List<Event> events = eventViewModel.getAllEvents().getValue();
        mCalendarEvents = getEventsByDate(events, calendarDate);

        if(mCalendarEvents != null) {
            mCalendarEventFragment.setEvents(mCalendarEvents);
        }
    }

    @Override
    public void onCalendarDayClicked(Calendar clickedCalendar) {
        List<Event> events = eventViewModel.getAllEvents().getValue();
        mCalendarEvents = new ArrayList<>();
        for(int i = 0; i < events.size(); i++) {
            Event currentEvent = events.get(i);
            Date eventCalendar = currentEvent.getDate();

            if(eventCalendar.equals(clickedCalendar.getTime())) {
                mCalendarEvents.add(currentEvent);
            }
        }

        if(mCalendarEvents != null) {
            mCalendarEventFragment.setEvents(mCalendarEvents);
        }
    }

    private CalendarEventFragment mCalendarEventFragment;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;

    // UI XML Views
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    // Db elements
    private EventViewModel eventViewModel;

    // Theme colors
    private int mColorPrimaryAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);

        initFab();
        initToolbar();
        initDrawer();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mCalendarFragment = new CalendarFragment();
        mCalendarEventFragment = new CalendarEventFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.calendar_container, mCalendarFragment)
                .replace(R.id.calendar_event_list_container, mCalendarEventFragment)
                .commit();

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                mCalendarFragment.setEvents(events);
                if(mCalendarEvents != null) {
                    List<Event> matchingEvents = getEventsByDate(events, currentCalendarDate);
                    mCalendarEventFragment.setEvents(matchingEvents);
                }
            }
        });

    }

    private List<Event> getEventsByDate(List<Event> events, Calendar calendarDate) {
        mCalendarEvents = new ArrayList<>();
        try {
            for(int i = 0; i < events.size(); i++) {
                Event currentEvent = events.get(i);
                Calendar eventCalendar = Calendar.getInstance();
                eventCalendar.setTime(currentEvent.getDate());

                if(eventCalendar.get(Calendar.MONTH) == calendarDate.get(Calendar.MONTH) &&
                        eventCalendar.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR)) {
                    mCalendarEvents.add(currentEvent);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return mCalendarEvents;
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

        mColorPrimaryAccent = SharedPreferenceUtils.getColorAccent(getTheme());
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calendarEventIntent = new Intent(CalendarActivity.this, AddEditEventActivity.class);
                startActivityForResult(calendarEventIntent, ADD_EVENT_REQUEST);
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
                return  false;

            case R.id.action_hide_events:
                return false;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {
            long eventDateLong = data.getLongExtra(AddEditEventActivity.EXTRA_EVENT_DATE, -1);
            Date eventDate = new Date(eventDateLong);

            boolean[] eventActs = data.getBooleanArrayExtra(AddEditEventActivity.EXTRA_EVENT_ACTS);

            String eventComments = data.getStringExtra(AddEditEventActivity.EXTRA_EVENT_COMMENTS);

            Event event = new Event(eventDate, eventComments, eventActs[Event.EVENT_SEX_INDEX],
                    eventActs[Event.EVENT_HANDJOB_INDEX], eventActs[Event.EVENT_BLOWJOB_INDEX],
                    eventActs[Event.EVENT_ANAL_INDEX]);
            eventViewModel.insert(event);


            Snackbar.make(getWindow().getDecorView().getRootView(), "Partner saved",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else if (requestCode == EDIT_EVENT_REQUEST && resultCode == RESULT_OK) {
            /*int id = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_ID, -1);

            if (id == -1) {
                return;
            }

            String partnerName = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_NAME);
            String partnerDescription = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_DESCRIPTION);
            int partnerAge = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_AGE, 0);
            int partnerGender = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_GENDER, 0);

            Partner partner = new Partner(partnerName, partnerDescription, partnerAge, partnerGender);
            partner.setId(id);
            partnerViewModel.update(partner);*/

        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Partner not saved",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
