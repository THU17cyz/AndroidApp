package com.example.androidapp.fragment.follow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.TeacherProfile;
import com.example.androidapp.fragment.ProfileListFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

public class FollowFragment extends ProfileListFragment {

  SmartRefreshLayout refreshLayout;
  RecyclerView recyclerView;
  ShortProfileAdapter adapter;

  public FollowFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View root = super.onCreateView(inflater, container, savedInstanceState);  // inflater.inflate(R.layout.fragment_follow, container, false);

//    refreshLayout = root.findViewById(R.id.refreshLayout);
//    refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//      @Override
//      public void onRefresh(RefreshLayout refreshlayout) {
//        refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//      }
//    });
//    refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//      @Override
//      public void onLoadMore(RefreshLayout refreshlayout) {
//        refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//      }
//    });



    return root;
  }
}
