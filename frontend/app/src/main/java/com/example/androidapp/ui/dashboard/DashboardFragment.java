package com.example.androidapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.Adapter.TestAdapter;
import com.example.androidapp.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class DashboardFragment extends Fragment {

    //@BindView(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private List<String> mNameList;
    private TestAdapter mTestAdapter;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mNameList = Arrays.asList("小明","小红","小芳","小花","小海","小林","小叶","小虎","小柔");
        mTestAdapter=new TestAdapter(mNameList, getContext());//初始化NameAdapter
        mTestAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        mTestAdapter.openLeftAnimation();//设置加载动画

        mTestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //子空控件点击事件
        mTestAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "onItemLongClick" + mNameList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }






}
