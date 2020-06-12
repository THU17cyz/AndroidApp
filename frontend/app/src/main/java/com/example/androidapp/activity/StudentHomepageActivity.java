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

public class StudentHomepageActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_homepage);

    ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .init();



    Toolbar toolbar = findViewById(R.id.toolbar);
    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapse_toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
    collapsingToolbar.setCollapsedTitleTextColor(Color.TRANSPARENT);


    ExpandableTextView text =  (ExpandableTextView) findViewById(R.id.expand_text_view_info);
    text.setText("中国信通院将基础性、前瞻性、战略性软科学研究作为全院的核心工作之一， 设立了ICT产业领域、两化融合与产业互联网领域、无线移动领域、信息网络领域、先进计算领域、大数据与人工智能领域、数字经济与法律监管领域、网络安全与国际治理领域八大软科学研究领域。形成了跨部门协作、知识共享、点面结合、业务链贯通的科研体系架构，每年完成百余项软科学研究课题。");


    Button button = findViewById(R.id.btn_edit_info);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), StudentEditInfoActivity.class);
        startActivity(intent);
      }
    });

    Button button1 = findViewById(R.id.btn_edit_intention);
    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(StudentHomepageActivity.this,StudentEditIntentionActivity.class);
        startActivity(intent);
      }
    });


  }
}
