package com.example.androidapp.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.R;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.TeacherProfile;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private ArrayList<ShortProfile> mNameList;
    private ShortProfileAdapter mShortProfileAdapter;

    //To do
    public RecommendFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_fragment1, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        // mNameList = Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔");
        mNameList = new ArrayList<>();
        mNameList.add(new TeacherProfile(1, "黄翔", "清华大学", "",999));
        mNameList.add(new TeacherProfile(2, "黄翔", "清华大学", "",999));
        mShortProfileAdapter = new ShortProfileAdapter(mNameList, getContext());//初始化NameAdapter
        mShortProfileAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        // mTestAdapter.openLeftAnimation();//设置加载动画

        // 子组件的监听事件，比如按钮
        // 在Adapter里注册（addOnClickListener）
        mShortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
            ((FocusButton) view).click();
            // view.setBackground(getContext().getDrawable(R.drawable.shape_unwatch_button));
        });

        // RecycleView 本身的监听事件
        mShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return root;

    }
    //To do closed
}
