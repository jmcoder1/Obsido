package com.example.jojo.obsido.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jojo.obsido.Partner;
import com.example.jojo.obsido.PartnerAdapter;
import com.example.jojo.obsido.PartnerViewModel;
import com.example.jojo.obsido.R;
import com.example.jojo.obsido.SettingsActivity;
import com.example.jojo.obsido.utils.SharedPreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PartnersListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int ADD_PARTNER_REQUEST = 1;
    public static final int EDIT_PARTNER_REQUEST = 2;

    private PartnerViewModel partnerViewModel;
    private static final String LOG_TAG = "PartnersListActivity".getClass().getSimpleName();

    // UI XML View
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    // Shared Preferences Theme color values
    private int mColorPrimary;
    private int mColorPrimaryAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_list);

        initFab();
        initToolbar();
        initDrawer();
        initRecyclerView();

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

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, "initFab: adding a partner flaoting action button");
                Intent partnerIntent = new Intent(PartnersListActivity.this, AddEditProfileActivity.class);
                startActivityForResult(partnerIntent, ADD_PARTNER_REQUEST);
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(mColorPrimaryAccent));
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
    }

    private void initRecyclerView() {
        RecyclerView partnerRecyclerView = findViewById(R.id.partners_list);
        RelativeLayout partnerEmptyView = findViewById(R.id.empty_view);
        partnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partnerRecyclerView.setHasFixedSize(true);
        partnerRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        final PartnerAdapter partnerAdapter = new PartnerAdapter();
        partnerRecyclerView.setAdapter(partnerAdapter);

        partnerViewModel = ViewModelProviders.of(this).get(PartnerViewModel.class);
        partnerViewModel.getAllPartners().observe(this, new Observer<List<Partner>>() {
            @Override
            public void onChanged(@Nullable List<Partner> partners) {
                partnerAdapter.submitList(partners);

                if (partners.size() <= 0) {
                    partnerRecyclerView.setVisibility(View.GONE);
                    partnerEmptyView.setVisibility(View.VISIBLE);
                } else {
                    partnerRecyclerView.setVisibility(View.VISIBLE);
                    partnerEmptyView.setVisibility(View.GONE);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                partnerViewModel.delete(partnerAdapter.getPartnerAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(partnerRecyclerView);
        partnerAdapter.setOnItemClickListener(new PartnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partner partner) {
                Intent intent = new Intent(PartnersListActivity.this,
                        PartnerProfileActivity.class);
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_NAME, partner.getName());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_DESCRIPTION, partner.getDescription());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_AGE, partner.getAge());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_GENDER, partner.getGender());
                intent.putExtra(AddEditProfileActivity.EXTRA_PARTNER_ID, partner.getId());
                startActivityForResult(intent, EDIT_PARTNER_REQUEST);
            }
        });
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
            case R.id.action_delete_all_entries:
                partnerViewModel.deleteAllNotes();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PARTNER_REQUEST && resultCode == RESULT_OK) {
            String partnerName = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_NAME);
            String partnerDescription = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_DESCRIPTION);
            int partnerAge = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_AGE, 0);
            int partnerGender = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_GENDER, 0);

            Partner partner = new Partner(partnerName, partnerDescription, partnerAge, partnerGender);
            partnerViewModel.insert(partner);

            Snackbar.make(getWindow().getDecorView().getRootView(), "Partner saved",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else if (requestCode == EDIT_PARTNER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_ID, -1);

            if (id == -1) {
                return;
            }

            String partnerName = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_NAME);
            String partnerDescription = data.getStringExtra(AddEditProfileActivity.EXTRA_PARTNER_DESCRIPTION);
            int partnerAge = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_AGE, 0);
            int partnerGender = data.getIntExtra(AddEditProfileActivity.EXTRA_PARTNER_GENDER, 0);

            Partner partner = new Partner(partnerName, partnerDescription, partnerAge, partnerGender);
            partner.setId(id);
            partnerViewModel.update(partner);

        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Partner not saved",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
