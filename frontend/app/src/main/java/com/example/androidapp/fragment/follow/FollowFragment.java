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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

public class FollowFragment extends Fragment {

  SmartRefreshLayout refreshLayout;
  RecyclerView recyclerView;
  ShortProfileAdapter adapter;

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

    ArrayList<ShortProfile> mNameList = new ArrayList<>();
    mNameList.add(new TeacherProfile(1, "黄翔", "清华大学", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg",999));
    mNameList.add(new TeacherProfile(2, "黄翔", "清华大学", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg",999));
    adapter = new ShortProfileAdapter(mNameList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性
    // adapter.openLeftAnimation();//设置加载动画

    // 子组件的监听事件，比如按钮
    // 在Adapter里注册（addOnClickListener）
    adapter.setOnItemChildClickListener((adapter, view, position) -> {
      // 用position获取点击的是什么
      Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
      ((FocusButton) view).click();
      // view.setBackground(getContext().getDrawable(R.drawable.shape_unwatch_button));
    });

    // RecycleView 本身的监听事件
    adapter.setOnItemClickListener((adapter, view, position) -> {
      Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
    });


//    adapter = new FollowListAdapter(this.getActivity());
//    recyclerView.setAdapter(adapter);
//
//    List<Follower> followers = new ArrayList<>(Arrays.asList(
//            new Follower("",""),
//            new Follower("","")
//            ));
//    adapter.setTopicListAndNotify(followers);


    return root;
  }
}
