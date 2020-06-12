package com.example.androidapp.fragment.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapter.InfoAdapter;
import com.example.androidapp.R;

import java.util.Arrays;
import java.util.List;

public class InfoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mNameList;
    private InfoAdapter mInfoAdapter;

    public InfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mNameList = Arrays.asList("小红", "小芳", "小花", "小海", "小林", "小叶", "小虎", "小柔");
        mInfoAdapter = new InfoAdapter(mNameList, getContext());//初始化NameAdapter
        mInfoAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        mInfoAdapter.openLeftAnimation();//设置加载动画
        return root;

    }
}
