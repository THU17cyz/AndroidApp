package com.example.androidapp.fragment.QueryResult;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.entity.StudentProfile;
import com.example.androidapp.entity.StudentQueryInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Student extends Base {

    public Student() {
        order = new String[]{"最相关（默认）", "关注人数最多"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        ArrayList<StudentProfile> mNameList = new ArrayList<>();
        mNameList.add(new StudentProfile(1, "黄翔", "清华大学", "", 999));
        adapter = new ShortProfileAdapter(mNameList, getContext());//初始化NameAdapter
        adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性
        adapter.openLeftAnimation();//设置加载动画

        // 子组件的监听事件，比如按钮
        // 在Adapter里注册（addOnClickListener）
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            // TODO 关注
            Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
            view.setBackground(getContext().getDrawable(R.drawable.shape_unwatch_button));
        });

        // RecycleView 本身的监听事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            // TODO 进入其主页
            Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        recyclerView.addItemDecoration(dividerItemDecoration);


//        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {
//
//        });



        return root;

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public List<StudentQueryInfo> loadQueryInfo() {
        return new ArrayList<>();
    }

}
