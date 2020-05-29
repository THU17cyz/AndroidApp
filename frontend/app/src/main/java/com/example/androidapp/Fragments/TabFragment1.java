package com.example.androidapp.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.Adapter.TestAdapter;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    public class Profile {
        public String name;
        public String affiliation;
        public int fanNum;
        // TODO 照片
        public Profile(String name, String affiliation, int fanNum) {
            this.name = name;
            this.affiliation = affiliation;
            this.fanNum = fanNum;
        }
    }

    private RecyclerView mRecyclerView;

    private ArrayList<Profile> mNameList;
    private TestAdapter mTestAdapter;

    //To do
    public TabFragment1() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_fragment1, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        // mNameList = Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔");
        mNameList = new ArrayList<>();
        mNameList.add(new Profile("黄翔", "清华大学", 999));
        mNameList.add(new Profile("黄翔", "清华大学", 999));
        mNameList.add(new Profile("黄翔", "清华大学", 999));
        mTestAdapter = new TestAdapter(mNameList, getContext());//初始化NameAdapter
        mTestAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        mTestAdapter.openLeftAnimation();//设置加载动画

        // 子组件的监听事件，比如按钮
        // 在Adapter里注册（addOnClickListener）
        mTestAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
            view.setBackground(getContext().getDrawable(R.drawable.shape_unwatch_button));
        });

        // RecycleView 本身的监听事件
        mTestAdapter.setOnItemClickListener((adapter, view, position) -> {
            Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });

        return root;

    }
    //To do closed
}
