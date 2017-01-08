package gllc.tech.dateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gllc.tech.dateapp.CompletedDates.CompletedDates;
import gllc.tech.dateapp.UpComingDates.YourDatesFragment;

import static gllc.tech.dateapp.R.id.viewPager;

/**
 * Created by bhangoo on 1/6/2017.
 */

public class DisplayBothDates extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_dates_viewpager, container, false);

        ViewPager pager = (ViewPager) view.findViewById(viewPager);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        return view;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] { "Upcoming Dates", "Completed Dates"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                //case 0:return FirstFragment.newInstance("FirstFragment, Instance 1");
                case 0: return YourDatesFragment.newInstance();
                case 1: return CompletedDates.newInstance();
                //case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                //case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                //case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return YourDatesFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }
}

