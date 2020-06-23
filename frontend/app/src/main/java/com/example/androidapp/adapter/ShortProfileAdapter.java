package com.example.androidapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.myView.FocusButton;
import com.example.androidapp.util.MyImageLoader;
import com.example.androidapp.util.StringCutter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShortProfileAdapter<T> extends MyBaseAdapter {

    private CircleImageView mHead;
    private TextView mName;
    private TextView mAffiliation;
    private FocusButton mWatchBtn;

    public ShortProfileAdapter(List<T> data, Context context) {
        super(R.layout.layout_profile_row, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        // 这里大多数情况应该不需要初始化
//        mName = viewHolder.getView(R.id.name);
//        mHead = viewHolder.getView(R.id.profile_image);
//        mAffiliation = viewHolder.getView(R.id.affiliation);

        //
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        ShortProfile data = (ShortProfile) o;
        viewHolder.setText(R.id.profile_user_name, data.name)
                .setText(R.id.info, StringCutter.cutter(data.school + " " + data.department, 15))
                .setText(R.id.fan, data.fanNum + " 人关注");

        try {
            mHead = viewHolder.getView(R.id.profile_image);
            MyImageLoader.loadImage(mHead, data.url);
        } catch (Exception e) {
            System.out.println(e);
        }

        if (data.isValidated) {
            TextView mNameView = viewHolder.getView(R.id.profile_user_name);
            Drawable drawable = mNameView.getContext().getDrawable(R.drawable.ic_validated);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mNameView.setCompoundDrawables(drawable, null, null, null);
            mNameView.setCompoundDrawablePadding(5);
        }

        mWatchBtn = viewHolder.getView(R.id.watch_btn);
        mWatchBtn.setPressed_(data.isFan);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        // 注册按钮点击事件
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
