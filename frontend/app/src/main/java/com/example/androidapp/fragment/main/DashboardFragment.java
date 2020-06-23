package com.example.androidapp.fragment.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.activity.EditInfoActivity;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.GifSizeFilter;
import com.example.androidapp.util.MyImageLoader;
import com.example.androidapp.util.SizeConverter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * 主界面主页子页
 */
public class DashboardFragment
        extends Fragment {

    private static final int REQUEST_CODE_CHOOSE = 11;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.homepage_name)
    TextView name;

    @BindView(R.id.signature)
    TextView signature;

    @BindView(R.id.num_focus)
    TextView numFocus;

    @BindView(R.id.num_focused)
    TextView numFocused;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_edit)
    Button button;

    @BindView(R.id.visit_homepage_title)
    TextView title;

    @BindView(R.id.visit_homepage_appbar)
    AppBarLayout app_bar;

    public ArrayList<ApplicationInfo> mApplicationList;
    public ArrayList<RecruitmentInfo> mRecruitmentList;
    private HomepagePagerAdapter pagerAdapter;
    private String type;
    private int id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, root);

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        type = BasicInfo.TYPE;
        id = BasicInfo.ID;
        mApplicationList = new ArrayList<>();
        mRecruitmentList = new ArrayList<>();

        pagerAdapter = new HomepagePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), type, -1);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                JCVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        title.setText("我的个人主页");
        name.setText(BasicInfo.mName);

        final int alphaMaxOffset = SizeConverter.dpToPx(150);
        toolbar.getBackground().setAlpha(0);
        title.setAlpha(0);

        app_bar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            // 设置 toolbar 背景
            if (verticalOffset > -alphaMaxOffset) {
                toolbar.getBackground().setAlpha(255 * -verticalOffset / alphaMaxOffset);
                title.setAlpha(1 * -verticalOffset / alphaMaxOffset);
            } else {
                toolbar.getBackground().setAlpha(255);
                title.setAlpha(1);
            }
        });

        getAvatar(null);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        numFocus.setText(String.valueOf(BasicInfo.WATCH_LIST.size()));
        numFocused.setText(String.valueOf(BasicInfo.FAN_LIST.size()));
        signature.setText(BasicInfo.mSignature);
        name.setText(BasicInfo.mName);
    }

    @OnClick(R.id.img_avatar)
    void changeAvatar() {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void getAvatar(String path) {
        if (path == null) {
            GetInfoPictureRequest request;
            if (type.equals("S"))
                request = new GetInfoPictureRequest(type, null, String.valueOf(id));
            else request = new GetInfoPictureRequest(type, String.valueOf(id), null);
            try {
                MyImageLoader.loadImage(imgAvatar, request.getWholeUrl());
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                System.out.println(path);
                MyImageLoader.loadImage(imgAvatar);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
