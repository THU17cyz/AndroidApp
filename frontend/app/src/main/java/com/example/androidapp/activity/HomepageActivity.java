package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.util.BasicInfo;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomepageActivity extends AppCompatActivity {

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @BindView(R.id.btn_return)
  ImageView btn_return;

  @BindView(R.id.btn_focus)
  Button btn_focus;

  @BindView(R.id.btn_chat)
  Button btn_chat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_homepage);
    ButterKnife.bind(this);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();

    tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
    tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
    tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    HomepagePagerAdapter pagerAdapter = new HomepagePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), BasicInfo.TYPE, -1);
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

    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(HomepageActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });

    btn_chat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(HomepageActivity.this, ChatActivity.class);
        startActivity(intent);
      }
    });

  }
}
