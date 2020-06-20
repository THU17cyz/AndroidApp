package com.example.androidapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.Conversation.ChatFragment;

public class ConversationPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;


    public ConversationPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ChatFragment(0);
            case 1: return new Fragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
