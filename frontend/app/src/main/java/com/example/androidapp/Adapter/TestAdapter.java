package com.example.androidapp.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class TestAdapter<T> extends MyBaseAdapter {

    private CircleImageView mHead;

    private TextView mName;

    public TestAdapter(List<T> data, Context context){
        super(R.layout.test_layout, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        mName = viewHolder.getView(R.id.name);
        mHead = viewHolder.getView(R.id.profile_image);
        //
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        mName.setText(o.toString());
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        //mTvName点击事件
        viewHolder.addOnClickListener(mName.getId());
    }
}
