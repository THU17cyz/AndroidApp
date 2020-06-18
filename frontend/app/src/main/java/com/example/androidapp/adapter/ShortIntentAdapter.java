package com.example.androidapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortIntent;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.util.StringCutter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ShortIntentAdapter<T> extends MyBaseAdapter {


    public ShortIntentAdapter(List<T> data, Context context){
        super(R.layout.layout_intent_row, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {

    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        ShortIntent data = (ShortIntent) o;
        viewHolder.setText(R.id.intent_user_name, data.name)
                .setText(R.id.intent_user_affiliation, StringCutter.cutter(data.school + data.department, 20));

        if (data.isTeacher) {
            viewHolder.setText(R.id.intent_info,  data.target);
        }



    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        // 注册按钮点击事件
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
