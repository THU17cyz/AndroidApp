package com.example.androidapp.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;

import java.util.List;

public class InfoAdapter<T> extends MyBaseAdapter {

    private ImageView mHead;
    private TextView mName;

    public InfoAdapter(List data, Context context) {
        super(R.layout.info_layout, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        mName = viewHolder.getView(R.id.logon1_type);
        mHead = viewHolder.getView(R.id.head);

    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        mName.setText(o.toString());
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(mName.getId());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
