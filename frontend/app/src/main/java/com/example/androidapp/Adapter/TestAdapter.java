package com.example.androidapp.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;

import java.util.List;


public class TestAdapter<T> extends MyBaseAdapter {

    private TextView mTvName;

    public TestAdapter(List<T> data, Context context){
        super(R.layout.test_layout, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        mTvName=viewHolder.getView(R.id.tv_name);
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        mTvName.setText(o.toString());
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        //mTvName点击事件
        viewHolder.addOnClickListener(mTvName.getId());
    }
}
