package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.Adapter.ForgetPasswordPagerAdapter;
import com.example.androidapp.Adapter.LogonPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.forgetPwdTabLayout)
    TabLayout tabLayout;

    @BindView(R.id.forgetPwdPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("安全验证"));
        tabLayout.addTab(tabLayout.newTab().setText("重置密码"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ForgetPasswordPagerAdapter pagerAdapter = new ForgetPasswordPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

    }

    //按钮点击事件处理
    @OnClick(R.id.forgetPwdReturnButton)
    public void returnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void nextPage() {
        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition() + 1);
    }
}
