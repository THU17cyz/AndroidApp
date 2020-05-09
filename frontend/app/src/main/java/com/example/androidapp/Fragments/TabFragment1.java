package com.example.androidapp.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Adapter.TestAdapter;
import com.example.androidapp.R;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    private RecyclerView mRecyclerView;

    private List<String> mNameList;
    private TestAdapter mTestAdapter;

    //To do
    public TabFragment1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_fragment1, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mNameList = Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔");
        mTestAdapter=new TestAdapter(mNameList, getContext());//初始化NameAdapter
        mTestAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        mTestAdapter.openLeftAnimation();//设置加载动画
        return root;

    }
    //To do closed
}
