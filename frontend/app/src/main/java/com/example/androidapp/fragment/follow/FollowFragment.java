package com.example.androidapp.fragment.follow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.fragment.ProfileListFragment;
import com.example.androidapp.request.follow.AddToWatchRequest;
import com.example.androidapp.request.follow.DeleteFromWatchRequest;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowFragment extends Fragment {

  @BindView(R.id.t_recycler_view)
  RecyclerView tRecyclerView;

//  @BindView(R.id.t_refresh_layout)
//  RefreshLayout tRefreshLayout;

  @BindView(R.id.s_recycler_view)
  RecyclerView sRecyclerView;

//  @BindView(R.id.s_refresh_layout)
//  RefreshLayout sRefreshLayout;

  ArrayList<ShortProfile> sProfileList;
  ArrayList<ShortProfile> tProfileList;

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

    sProfileList = new ArrayList<>();
    tProfileList = new ArrayList<>();
    sProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
            test_url,999, true, false));
    tProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
            test_url,999, true, false));

    tShortProfileAdapter = new ShortProfileAdapter(sProfileList, getContext());//初始化NameAdapter
    tShortProfileAdapter.setRecyclerManager(tRecyclerView);//设置RecyclerView特性

    sShortProfileAdapter = new ShortProfileAdapter(tProfileList, getContext());//初始化NameAdapter
    sShortProfileAdapter.setRecyclerManager(sRecyclerView);//设置RecyclerView特性

    addButtonListener(tShortProfileAdapter, tProfileList);
    addButtonListener(sShortProfileAdapter, sProfileList);

    return root;
  }

  private void getFanList() {

  }

  private void getFollowList() {

  }

  private void addButtonListener(ShortProfileAdapter shortProfileAdapter, ArrayList<ShortProfile> shortProfileArrayList) {
    shortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
      Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
      FocusButton btn = ((FocusButton) view);
      btn.startLoading(() -> {
        ShortProfile profile = shortProfileArrayList.get(position);
        if (profile.isFan) {
          new DeleteFromWatchRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
              Log.e("error1", e.toString());
              getActivity().runOnUiThread(btn::clickFail);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
              String resStr = response.body().string();
              Log.e("response", resStr);
              try {
                JSONObject jsonObject = new JSONObject(resStr);
                profile.isFan = false;
                getActivity().runOnUiThread(btn::clickSuccess);
              } catch (JSONException e) {
                Log.e("error2", e.toString());
                getActivity().runOnUiThread(btn::clickFail);
              }

            }
          }, String.valueOf(profile.id), profile.isTeacher).send();
        } else {
          new AddToWatchRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
              Log.e("error1", e.toString());
              getActivity().runOnUiThread(btn::clickFail);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
              String resStr = response.body().string();
              Log.e("response", resStr);
              try {
                JSONObject jsonObject = new JSONObject(resStr);
                profile.isFan = true;
                getActivity().runOnUiThread(btn::clickSuccess);
              } catch (JSONException e) {
                Log.e("error2", e.toString());
                getActivity().runOnUiThread(btn::clickFail);
              }

            }
          }, String.valueOf(profile.id), profile.isTeacher).send();
        }
      });
    });
  }
}
