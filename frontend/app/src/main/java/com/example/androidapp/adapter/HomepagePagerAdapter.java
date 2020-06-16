package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.follow.FollowFragment;
import com.example.androidapp.fragment.homepage.ApplicationInfoFragment;
import com.example.androidapp.fragment.homepage.EnrollmentInfoFragment;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;

public class HomepagePagerAdapter extends FragmentStatePagerAdapter {
  int mNumOfTabs;

  public HomepagePagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
//    return new SelfInfoFragment();
//    return new FollowFragment();
    switch (position) {
      case 0: return new SelfInfoFragment();
      case 1: return new StudyInfoFragment();
      case 2:return new EnrollmentInfoFragment();
      case 3:return new FollowFragment();
      default: return null;
    }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
