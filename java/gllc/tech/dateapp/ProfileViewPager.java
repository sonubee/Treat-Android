package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gllc.tech.dateapp.CompletedDates.CompletedDates;
import gllc.tech.dateapp.Loading.Profile;
import gllc.tech.dateapp.SearchDate.Filters;
import gllc.tech.dateapp.UpComingDates.YourDatesFragment;

import static gllc.tech.dateapp.R.id.viewPager;
import static gllc.tech.dateapp.R.id.viewPagerProfile;

/**
 * Created by bhangoo on 1/7/2017.
 */

public class ProfileViewPager extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_viewpager, container, false);

        ViewPager pager = (ViewPager) view.findViewById(viewPagerProfile);
        pager.setAdapter(new ProfileViewPager.MyPagerAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.slidingTabsProfile);
        tabLayout.setupWithViewPager(pager);

        return view;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] { "Profile", "Filters", "Settings"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0: return Profile.newInstance();
                case 1: return Filters.newInstance();
                case 2: return Settings.newInstance();
                default: return Profile.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }


    public void reloadProfileFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }
}
