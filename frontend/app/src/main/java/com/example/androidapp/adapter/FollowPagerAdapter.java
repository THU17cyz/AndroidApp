package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.UI.follow.FollowFragmentNav;
import com.example.androidapp.fragment.follow.FollowFragment;

public class FollowPagerAdapter extends FragmentStatePagerAdapter {

  int mNumOfTabs;

  public FollowPagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return new FollowFragment();
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
