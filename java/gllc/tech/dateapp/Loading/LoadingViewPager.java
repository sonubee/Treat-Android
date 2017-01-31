package gllc.tech.dateapp.Loading;

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

import gllc.tech.dateapp.R;
import gllc.tech.dateapp.SearchDate.Filters;

import static gllc.tech.dateapp.R.id.loadingPagerProfile;

/**
 * Created by bhangoo on 1/29/2017.
 */

public class LoadingViewPager extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_viewpager, container, false);

        ViewPager pager = (ViewPager) view.findViewById(loadingPagerProfile);
        pager.setAdapter(new LoadingViewPager.MyPagerAdapter(getChildFragmentManager()));

        return view;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

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

    }
}
