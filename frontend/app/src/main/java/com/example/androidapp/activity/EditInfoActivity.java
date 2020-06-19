package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidapp.R;
import com.example.androidapp.adapter.EditInfoPagerAdapter;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.fragment.HomepageEdit.EditApplicationInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditEnrollmentInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditSelfInfoFragment;
import com.example.androidapp.fragment.HomepageEdit.EditStudyInfoFragment;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfoActivity extends AppCompatActivity {

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;


  @BindView(R.id.toolbar)
  Toolbar toolbar;

  EditInfoPagerAdapter pagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_info);
    ButterKnife.bind(this);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
    tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
    tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    pagerAdapter = new EditInfoPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(pagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });


    setSupportActionBar(toolbar);
    // 标题栏返回
    toolbar.setNavigationOnClickListener(v-> this.finish());


  }

  @OnClick(R.id.edit_info_save)
  void updateEdit() {
    Fragment first_one = pagerAdapter.getRegisteredFragment(0);
    Fragment second_one = pagerAdapter.getRegisteredFragment(1);
    if (first_one != null) ((EditSelfInfoFragment) first_one).update();
    if (second_one != null) ((EditStudyInfoFragment) second_one).update();
    Fragment third_one = pagerAdapter.getRegisteredFragment(2);
    if (third_one != null) {
      if (third_one instanceof EditEnrollmentInfoFragment) {
        ((EditEnrollmentInfoFragment) third_one).update();
      } else {
        ((EditApplicationInfoFragment) third_one).update();
      }
    }


    finish();
  }
}
