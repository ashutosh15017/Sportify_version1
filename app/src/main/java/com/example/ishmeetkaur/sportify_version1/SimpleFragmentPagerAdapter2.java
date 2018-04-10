package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by ishmeetkaur on 10/03/18.
 */
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter2 extends FragmentPagerAdapter
{

    public SimpleFragmentPagerAdapter2(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        if (position == 0)
        {
            return new Fragment_feeds();
        }

        else
        {
            return new Fragment_mysports();
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
            return "My Sports";
        }
    }
}