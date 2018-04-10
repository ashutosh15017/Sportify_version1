package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by ishmeetkaur on 10/03/18.
 */

import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter
{

    public SimpleFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        if (position == 0)
        {
            return new Login_Fragment();
        }

        else
        {
            return new SignUp_Fragment();
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
            return "Login";
        } else {
            return "Sign Up";
        }
    }
}