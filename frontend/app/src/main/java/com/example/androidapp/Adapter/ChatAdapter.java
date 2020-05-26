package com.example.androidapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.Fragments.Notification.ChatFragment;
import com.example.androidapp.Fragments.Notification.ChatItem;
import com.example.androidapp.R;

import java.util.List;

public class ChatAdapter<T> extends MyBaseAdapter {

    private ImageView mHead;
    private TextView mName;

    public ChatAdapter(List<T> data, Context context) {
        super(R.layout.chat_layout, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        mName = viewHolder.getView(R.id.name);
        mHead = viewHolder.getView(R.id.head);
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        ChatItem t = (ChatItem)o;

        mName.setText(t.str1);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(mName.getId());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        mName.setText(((ChatItem)mData.get(position)).str1);
    }

//    @NonNull
//    @Override
//    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View item = LayoutInflater.from(mContext).inflate(R.layout.chat_layout , parent ,false);
//        Log.d("aaa","onCreateViewHolder————"+viewType);
//        if (viewType == 1){//标题
//            item.setTag(true);
//        }else{
//            item.setTag(false);
//        }
//        return new ViewHolde(item);
//
//    }
//
//
//    static class ViewHolde extends BaseViewHolder {
//
//        TextView txt;
//        public ViewHolde(View itemView) {
//            super(itemView);
//            txt = itemView.findViewById(R.id.name);
//        }
//    }
}
