package com.example.androidapp.fragment.homepage;


import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.activity.LogonActivity;
import com.example.androidapp.R;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EditApplication;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.TeacherProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ApplicationInfoFragment extends Fragment {

  RecyclerView recyclerView;
  ApplicationListAdapter adapter;
  //To do
  public ApplicationInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_intention_info, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    ArrayList<ApplicationInfo> mApplicationList = new ArrayList<>();
    mApplicationList.add(new ApplicationInfo("计算机图形学", "进行中", "我是xxx"));
    mApplicationList.add(new ApplicationInfo("物联网", "进行中", "我是xxx"));
    adapter = new ApplicationListAdapter(mApplicationList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    return view;
  }

}


