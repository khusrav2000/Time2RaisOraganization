package com.example.organization.events;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;
import com.example.organization.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class EventsSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.events, R.string.my_events};
    private final Context mContext;

    public EventsSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        System.out.println(" -  -- - - - -" );
        System.out.println(position);
        if (position == 0) {
            return ListEventsFragment.newInstance(1);
        } else if (position == 1){
            return ListMyEventsFragment.newInstance(1);
        } else {
            return ListMyEventsFragment.newInstance(1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}