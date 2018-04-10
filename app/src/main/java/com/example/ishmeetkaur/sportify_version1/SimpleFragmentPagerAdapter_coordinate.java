package com.example.ishmeetkaur.sportify_version1;

import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by shubzz on 11/4/18.
 */

public class SimpleFragmentPagerAdapter_coordinate extends FragmentPagerAdapter
{
    public SimpleFragmentPagerAdapter_coordinate(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        if (position == 0)
        {
            return new C_feeds_fragment();
        }

        else
        {
            return new C_myTeam_fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    //for title of tabs
    @Override
    public CharSequence getPageTitle(int position)
    {
        if (position == 0) {
            return "Feed";
        } else {
            return "My Team";
        }
    }

}
