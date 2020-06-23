package com.example.androidapp.adapter;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HistoryAdapter<T> extends MyBaseAdapter {

    public HistoryAdapter(List<T> data, Context context) {
        super(R.layout.layout_history_list, data, context);
    }

    @Override
    public void remove(int position) {
        getData().remove(position);
        notifyItemRemoved(position);
    }


    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {


    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        String data = (String) o;
        viewHolder.setText(R.id.textView, data);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(R.id.button);
    }
}

