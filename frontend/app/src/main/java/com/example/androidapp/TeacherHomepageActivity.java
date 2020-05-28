package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherHomepageActivity extends AppCompatActivity {

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

//        ImageView imageView = findViewById(R.id.background_image);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


//        collapsingToolbar.setTitle("个人主页");
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




    }


}
