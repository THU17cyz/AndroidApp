package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.follow.FollowListFragment;

public class FollowPagerAdapter extends FragmentStatePagerAdapter {

  int mNumOfTabs;

  public FollowPagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return new FollowListFragment();
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
