package com.example.androidapp.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.Fragments.TabFragment1;

import com.example.androidapp.R;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class HistoryAdapter<T> extends MyBaseAdapter {

    private CircleImageView mHead;
    private TextView history;
    private TextView mAffiliation;
    private Button mWatchBtn;

    public HistoryAdapter(List<T> data, Context context){
        super(R.layout.layout_history_list, data, context);
    }

    @Override
    public void remove(int position) {
        System.out.println(getData());
        getData().remove(position);
        notifyItemRemoved(position);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {


    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        String data = (String) o;
//        RTextView rView = (RTextView) viewHolder.getView(R.id.textView);
//        rView.setText(data);
        viewHolder.setText(R.id.textView, data);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        // 注册按钮点击事件
        viewHolder.addOnClickListener(R.id.button);
    }
}

