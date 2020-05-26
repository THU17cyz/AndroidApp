package com.example.androidapp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.Fragments.Notification.ChatFragment;
import com.example.androidapp.Fragments.Notification.InfoFragment;

public class NotificationPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public NotificationPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ChatFragment();
            case 1: return new InfoFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
