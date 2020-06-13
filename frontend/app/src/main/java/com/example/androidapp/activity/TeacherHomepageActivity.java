package com.example.androidapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.androidapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;


import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class TeacherHomepageActivity extends BaseActivity {

    private Button btn_focus;
    private Button btn_chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_homepage);

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        Toolbar toolbar = findViewById(R.id.toolbar);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapse_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbar.setCollapsedTitleTextColor(Color.TRANSPARENT);

        TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagContainerLayout);
        List<String> tags = new ArrayList<>();
        tags.add("计算机图形学");
        tags.add("物联网");
        tags.add("物联网");
        tags.add("物联网");
        tags.add("物联网");
        tags.add("物联网");
        tags.add("物联网");
        mTagContainerLayout.setTags(tags);
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                mTagContainerLayout.removeTag(position);
            }
        });

        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "项目展示");
//        jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse());


        // sample code snippet to set the text content on the ExpandableTextView
//        ExpandableTextView expTv1 = findViewById(R.id.expandable_text).findViewById(R.id.expand_text_view);
        ExpandableTextView text =  (ExpandableTextView) findViewById(R.id.expand_text_view);

// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        text.setText("中国信通院将基础性、前瞻性、战略性软科学研究作为全院的核心工作之一， 设立了ICT产业领域、两化融合与产业互联网领域、无线移动领域、信息网络领域、先进计算领域、大数据与人工智能领域、数字经济与法律监管领域、网络安全与国际治理领域八大软科学研究领域。形成了跨部门协作、知识共享、点面结合、业务链贯通的科研体系架构，每年完成百余项软科学研究课题。");



        Button button = findViewById(R.id.btn_edit_info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherHomepageActivity.this,TeacherEditInfoActivity.class);
                startActivity(intent);
            }
        });

        Button button1 = findViewById(R.id.btn_edit_intention);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherHomepageActivity.this,TeacherEditIntentionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


}
