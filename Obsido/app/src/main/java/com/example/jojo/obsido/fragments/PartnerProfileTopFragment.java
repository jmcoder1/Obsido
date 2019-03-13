package com.example.jojo.obsido.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jojo.obsido.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class PartnerProfileTopFragment extends Fragment {

    private String mPartnerName, mPartnerDescription;
    private String[] tabTitles = new String[]{"Overview", "Agenda", "Health", "Places"};

    private TabLayout mTabLayout;

    public PartnerProfileTopFragment() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_partner_profile_top, container, false);

        TextView partnerNameTextView =  rootView.findViewById(R.id.partner_name);
        TextView partnerDescriptionTextView = rootView.findViewById(R.id.partner_description);
        ImageView partnerProfileImg = rootView.findViewById(R.id.partner_profile_img);
        TextView partnerProfileTextView = rootView.findViewById(R.id.partner_profile_img_text_view);
        mTabLayout = rootView.findViewById(R.id.tabLayout);

        partnerProfileTextView.setVisibility(View.VISIBLE);
        partnerProfileTextView.setText(mPartnerName.substring(0, 1).toUpperCase());
        partnerProfileImg.setVisibility(View.INVISIBLE);

        partnerNameTextView.setText(mPartnerName);
        partnerDescriptionTextView.setText(mPartnerDescription);

        initTabLayout();

        return rootView;
    }

    private void initTabLayout() {

        final ViewPager viewPager = getActivity().findViewById(R.id.viewPager);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(viewPager);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);

        viewPager.setAdapter(new PagerAdapter(getFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        for(int i = 0; i < mTabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext())
                    .inflate(R.layout.nav_tab, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = tab.findViewById(R.id.nav_label);
            ImageView tab_icon =  tab.findViewById(R.id.nav_icon);

            // set the label text by getting the actual string value by its id
            // by getting the actual resource value `getResources().getString(string_id)`
            tab_label.setText(tabTitles[i]);

            // finally publish this custom view to navigation tab
            mTabLayout.getTabAt(i).setCustomView(tab);
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setPartnerName(String name) {
        mPartnerName = name;
    }

    public void setPartnerDescription(String description) {
        mPartnerDescription = description;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new OverviewTabFragment();
                case 1:
                    return new OverviewTabFragment();
                case 2:
                    return new OverviewTabFragment();
                case 3:
                    return new OverviewTabFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
