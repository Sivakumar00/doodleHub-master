package com.doodleHub.io;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by student on 24/5/18.
 */


class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {

           case 0: UpcomingEventsFragment upcomingEventsFragment=new UpcomingEventsFragment();
                return upcomingEventsFragment;
            case 1: MyEventsFragment myEventsFragment=new MyEventsFragment();
                return myEventsFragment;

            default:return  null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Upcoming Events";
            case 1:
                return "My Events";
            default: return null;

        }

    }
}