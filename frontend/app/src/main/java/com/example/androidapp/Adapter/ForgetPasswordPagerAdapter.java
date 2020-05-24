package com.example.androidapp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.Fragments.ForgetPassword.ForgetPasswordFragment1;
import com.example.androidapp.Fragments.ForgetPassword.ForgetPasswordFragment2;
import com.example.androidapp.Fragments.Logon.LogonFragment1;
import com.example.androidapp.Fragments.Logon.LogonFragment2;
import com.example.androidapp.Fragments.Logon.LogonFragment3;

public class ForgetPasswordPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ForgetPasswordPagerAdapter(FragmentManager fm, int NumOfTabs) {
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
            case 0: return new ForgetPasswordFragment1();
            case 1: return new ForgetPasswordFragment2();
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
