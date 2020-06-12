package com.example.androidapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.Logon.LogonFragment1;
import com.example.androidapp.fragment.Logon.LogonFragment2;
import com.example.androidapp.fragment.Logon.LogonFragment3;

/**
 * Fragment to return the clicked tab.
 */
public class LogonPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public LogonPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        //To do
        //return the corresponded fragment according to position
        //remember that the position can not be out of [0, 2]
        switch (position) {
            case 0: return new LogonFragment1();
            case 1: return new LogonFragment2();
            case 2: return new LogonFragment3();
            default: return null;
        }
        //To do closed
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
