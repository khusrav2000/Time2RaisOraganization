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
    private static final int[] TAB_TITLES = new int[]{R.string.events, R.string.my_events, R.string.event_offers};
    private final Context mContext;
    ListEventsFragment listEventsFragment;
    ListMyEventsFragment listMyEventsFragment;
    EventOfferFragment eventOfferFragment;

    public EventsSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        listEventsFragment = ListEventsFragment.newInstance(1);
        listMyEventsFragment = ListMyEventsFragment.newInstance(1);
        eventOfferFragment = EventOfferFragment.newInstance(1);
    }



    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        System.out.println(" -  -- - - - ----------- ---------------------  -----" );
        System.out.println(position);
        if (position == 0) {
            System.out.println("Events --------------");
            return listEventsFragment;
        } else if (position == 1){
            System.out.println("My Events --------------------");

            return listMyEventsFragment;
        } else if (position == 2){
            return eventOfferFragment;
        } else {
            System.out.println("Not Anyone --------------");
            return listEventsFragment;
        }

    }


    public void filterItems(String textPattern){
        if (textPattern != null){
            if (listEventsFragment != null){
                listEventsFragment.setFilter(textPattern);
            } if (listMyEventsFragment != null){
                listMyEventsFragment.setFilter(textPattern);
            } if (eventOfferFragment != null){
                eventOfferFragment.setFilter(textPattern);
            }
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}