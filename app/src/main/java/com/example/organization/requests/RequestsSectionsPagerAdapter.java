package com.example.organization.requests;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.organization.R;
import com.example.organization.data.model.Requests;
import com.example.organization.events.ListEventsFragment;
import com.example.organization.events.ListMyEventsFragment;

public class RequestsSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.requests, R.string.my_requests};
    private final Context mContext;

    public RequestsSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return ListRequestsFragment.newInstance(1);
        } else if (position == 1){
            return ListMyRequestsFragment.newInstance(1);
        } else {
            return ListMyRequestsFragment.newInstance(1);
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
