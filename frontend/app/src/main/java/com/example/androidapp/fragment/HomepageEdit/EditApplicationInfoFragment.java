package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.EditApplicationListAdapter;
import com.example.androidapp.entity.ApplicationInfo;

import java.util.ArrayList;

public class EditApplicationInfoFragment extends Fragment {

  RecyclerView recyclerView;
  EditApplicationListAdapter adapter;
  //To do
  public EditApplicationInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    ArrayList<ApplicationInfo> mApplicationList = new ArrayList<>();
    mApplicationList.add(new ApplicationInfo("计算机图形学", "进行中", "我是xxx"));
    mApplicationList.add(new ApplicationInfo("物联网", "进行中", "我是xxx"));
    adapter = new EditApplicationListAdapter(mApplicationList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    return view;
  }

}
