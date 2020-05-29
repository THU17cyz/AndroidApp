package com.example.androidapp.Adapter;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.Fragments.QueryResult.Teacher;
import com.example.androidapp.Fragments.TabFragment1;
import com.example.androidapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAdapter extends MyBaseAdapter {
    private CircleImageView mHead;
    private TextView mName;
    private TextView mAffiliation;
    private Button mWatchBtn;

    public TeacherAdapter(List<Teacher.TeacherProfile> data, Context context){
        super(R.layout.test_layout, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        // 这里大多数情况应该不需要初始化
//        mName = viewHolder.getView(R.id.name);
//        mHead = viewHolder.getView(R.id.profile_image);
//        mAffiliation = viewHolder.getView(R.id.affiliation);
//        mWatchBtn = viewHolder.getView(R.id.watch_btn);
        //
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        Teacher.TeacherProfile data = (Teacher.TeacherProfile) o;
        viewHolder.setText(R.id.name, data.name)
                .setText(R.id.affiliation, data.affiliation)
                .setText(R.id.fan_num, data.fanNum + "人关注");
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        // 注册按钮点击事件
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
