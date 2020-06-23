package com.example.androidapp.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ShortIntent;
import com.example.androidapp.util.StringCutter;

import java.util.List;


public class ShortIntentAdapter<T> extends MyBaseAdapter {


    public ShortIntentAdapter(List<T> data, Context context) {
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
                .setText(R.id.intent_user_affiliation, StringCutter.cutter(data.school + " " + data.department, 20));

        viewHolder.setText(R.id.intent_info, data.target);

        if (data.isTeacher) {
            String type_;
            if (data.recruitType.equals("UG")) type_ = "本科生";
            else if (data.recruitType.equals("MT")) type_ = "硕士生";
            else type_ = "博士生";
            viewHolder.setText(R.id.intent_type, "招收" + type_ + " " + data.recruitNum + "人");
        } else {
            viewHolder.getView(R.id.intent_type).setVisibility(View.GONE);
        }
        String state_;
        if (data.intentionState.equals("S")) state_ = "已完成";
        else if (data.intentionState.equals("O")) state_ = "进行中";
        else state_ = "已取消";
        viewHolder.setText(R.id.intent_state, state_);


    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
