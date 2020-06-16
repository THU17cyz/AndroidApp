package com.example.androidapp.fragment.follow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.fragment.ProfileListFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowFragment extends ProfileListFragment {

  @BindView(R.id.t_recycler_view)
  RecyclerView tRecyclerView;

  @BindView(R.id.t_refresh_layout)
  RefreshLayout tRefreshLayout;

  @BindView(R.id.s_recycler_view)
  RecyclerView sRecyclerView;

  @BindView(R.id.s_refresh_layout)
  RefreshLayout sRefreshLayout;

  private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

  protected ShortProfileAdapter tShortProfileAdapter;
  protected ShortProfileAdapter sShortProfileAdapter;

  public FollowFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_follow_list, container, false);
    ButterKnife.bind(this, root);

    mProfileList = new ArrayList<>();
    mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
            test_url,999, true, false));
    mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
            test_url,999, true, false));

    tShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
    tShortProfileAdapter.setRecyclerManager(tRecyclerView);//设置RecyclerView特性

    sShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
    sShortProfileAdapter.setRecyclerManager(sRecyclerView);//设置RecyclerView特性




    return root;
  }
}
