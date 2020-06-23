package com.example.androidapp.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ApplicationInfo;

import java.util.List;

public class ApplicationListAdapter<T> extends MyBaseAdapter {

    public ApplicationListAdapter(List<T> data, Context context) {
        super(R.layout.item_application_info, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {

    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {

        ApplicationInfo data = (ApplicationInfo) o;
        viewHolder.setText(R.id.direction, data.direction)
                .setText(R.id.profile, data.profile);
        if (data.state.equals("O")) {
            viewHolder.setText(R.id.state, "进行");
        } else if (data.state.equals("S")) {
            viewHolder.setText(R.id.state, "成功");
        } else if (data.state.equals("F")) {
            viewHolder.setText(R.id.state, "失败");
        }
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {

    }
}
