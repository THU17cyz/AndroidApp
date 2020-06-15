package com.example.androidapp.adapter.queryPageAdapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.adapter.MyBaseAdapter;
import com.example.androidapp.entity.StudentProfile;
import com.example.androidapp.entity.TeacherProfile;

import java.util.List;

public class StudentAdapter extends MyBaseAdapter {
    public StudentAdapter(List<StudentProfile> data, Context context){
        super(R.layout.layout_profile_row, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        StudentProfile data = (StudentProfile) o;
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
