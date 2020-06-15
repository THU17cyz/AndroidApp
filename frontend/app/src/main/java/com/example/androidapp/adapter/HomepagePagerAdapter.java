package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.Conversation.ChatFragment;
import com.example.androidapp.fragment.follow.FollowFragment;

public class HomepagePagerAdapter extends FragmentStatePagerAdapter {
  int mNumOfTabs;


  public HomepagePagerAdapter(FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @Override
  public Fragment getItem(int position) {
    return new FollowFragment();
//        switch (position) {
//
//          default: return null;
//      }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
