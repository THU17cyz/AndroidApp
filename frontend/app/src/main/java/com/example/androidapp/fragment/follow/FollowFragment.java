package com.example.androidapp.fragment.follow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.FollowListAdapter;
import com.example.androidapp.entity.Follower;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FollowFragment extends Fragment {

  SmartRefreshLayout refreshLayout;
  RecyclerView recyclerView;
  FollowListAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_follow, container, false);

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

    recyclerView = root.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    adapter = new FollowListAdapter(this.getActivity());
    recyclerView.setAdapter(adapter);

    List<Follower> followers = new ArrayList<>(Arrays.asList(
            new Follower("",""),
            new Follower("","")
            ));
    adapter.setTopicListAndNotify(followers);


    return root;
  }
}
