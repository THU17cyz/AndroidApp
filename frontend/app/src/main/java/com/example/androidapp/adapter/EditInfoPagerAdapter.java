package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.HomepageEdit.EditApplicationInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditEnrollmentInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditSelfInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditStudyInfoFragment;
import com.example.androidapp.fragment.follow.FollowFragment;
import com.example.androidapp.fragment.homepage.ApplicationInfoFragment;
import com.example.androidapp.fragment.homepage.EnrollmentInfoFragment;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;

public class EditInfoPagerAdapter extends FragmentStatePagerAdapter {
  int mNumOfTabs;

  public EditInfoPagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
//    return new SelfInfoFragment();
//    return new FollowFragment();
    switch (position) {
      case 0: return new EditSelfInfoFragment();
      case 1: return new EditStudyInfoFragment();
      case 2:return new EditEnrollmentInfoFragment();
      case 3:return new EditEnrollmentInfoFragment();
      default: return null;
    }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
