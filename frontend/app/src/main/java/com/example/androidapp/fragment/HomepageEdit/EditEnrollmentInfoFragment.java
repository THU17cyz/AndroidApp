package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.R;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.EditEnrollmentListAdapter;
import com.example.androidapp.adapter.EnrollmentListAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EnrollmentInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEnrollmentInfoFragment extends Fragment {

  @BindView(R.id.btn_add)
  FloatingActionButton btn_add;

  RecyclerView recyclerView;
  EditEnrollmentListAdapter adapter;
  //To do
  public EditEnrollmentInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);
    ButterKnife.bind(this,view);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    ArrayList<EnrollmentInfo> mEnrollmentList = new ArrayList<>();
    mEnrollmentList.add(new EnrollmentInfo("计算机图形学", "本科生", "100","进行中","介绍就是xxx"));
    mEnrollmentList.add(new EnrollmentInfo("物联网", "进行中", "我是xxx","进行中","介绍是xxx"));
    adapter = new EditEnrollmentListAdapter(mEnrollmentList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

      }
    });

    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        Toast.makeText(getActivity(), "testItemClick " + i, Toast.LENGTH_SHORT).show();
      }
    });

    btn_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // todo 添加栏目
        Toast.makeText(getActivity(),"添加",Toast.LENGTH_SHORT).show();
      }
    });

    return view;
  }

}
