package com.example.androidapp.fragment.homepage;


import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ApplicationInfoFragment extends Fragment {

  RecyclerView recyclerView;
  ApplicationListAdapter adapter;
  ArrayList<ApplicationInfo> mApplicationList;
  private int studentId;
  private List<Integer> applicationIdList;
  //To do
  public ApplicationInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_intention_info, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    // 获取个人id
    new GetInfoRequest(new okhttp3.Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e("error", e.toString());
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String resStr = response.body().string();
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
        Log.e("response", resStr);
        try {
          // 解析json，然后进行自己的内部逻辑处理
          JSONObject jsonObject = new JSONObject(resStr);

          Boolean status = jsonObject.getBoolean("status");
          if(status){
            studentId = jsonObject.getInt("student_id");
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);

    // 获取申请意向id列表
    new GetApplyIntentionRequest(new okhttp3.Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e("error", e.toString());
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String resStr = response.body().string();
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
        Log.e("response", resStr);
        try {
          // 解析json，然后进行自己的内部逻辑处理
          JSONObject jsonObject = new JSONObject(resStr);

          Boolean status = jsonObject.getBoolean("status");
          if(status){
            JSONArray array = jsonObject.getJSONArray("application_id_list");
            applicationIdList = new ArrayList<>();
            for (int i=0;i<array.length();i++){
              applicationIdList.add(array.getInt(i));
            }
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },String.valueOf(studentId));

    // 按id获取申请意向
    mApplicationList = new ArrayList<>();
    if(applicationIdList!=null){
      for(int i=0;i<applicationIdList.size();i++){
        new GetApplyIntentionDetailRequest(new okhttp3.Callback() {
          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("error", e.toString());
          }

          @Override
          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String resStr = response.body().string();
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
            Log.e("response", resStr);
            try {
              // 解析json，然后进行自己的内部逻辑处理
              JSONObject jsonObject = new JSONObject(resStr);

              Boolean status = jsonObject.getBoolean("status");
              if(status){
                ApplicationInfo applicationInfo = new ApplicationInfo(
                        jsonObject.getString("research_interests"),
                        jsonObject.getString("intention_state"),
                        jsonObject.getString("introduction")
                );
                mApplicationList.add(applicationInfo);
              }else{
                String info = jsonObject.getString("info");
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
              }
            } catch (JSONException e) {

            }
          }
        },String.valueOf(applicationIdList.get(i)));
      }
    }

    mApplicationList.add(new ApplicationInfo("计算机图形学", "进行中", "我是xxx"));
    mApplicationList.add(new ApplicationInfo("物联网", "进行中", "我是xxx"));
    adapter = new ApplicationListAdapter(mApplicationList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    return view;
  }

}


