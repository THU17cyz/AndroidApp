package com.example.androidapp.fragment.homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.EnrollmentListAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EnrollmentInfo;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.user.GetInfoRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class EnrollmentInfoFragment extends Fragment {

  RecyclerView recyclerView;
  EnrollmentListAdapter adapter;
  ArrayList<EnrollmentInfo> mEnrollmentList;
  private int teacherId;
  private List<Integer> enrollmentIdList;
  //To do
  public EnrollmentInfoFragment() {

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
            teacherId = jsonObject.getInt("teacher_id");
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },"I",null,null);

    // 获取招收意向id列表
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
            JSONArray array = jsonObject.getJSONArray("recruit_id_list");
            enrollmentIdList = new ArrayList<>();
            for (int i=0;i<array.length();i++){
              enrollmentIdList.add(array.getInt(i));
            }
          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },String.valueOf(teacherId));

    // 按id获取招收意向
    mEnrollmentList = new ArrayList<>();
    if(enrollmentIdList!=null){
      for(int i=0;i<enrollmentIdList.size();i++){
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
                EnrollmentInfo enrollmentInfo = new EnrollmentInfo(
                        jsonObject.getString("research_fields"),
                        jsonObject.getString("recruitment_type"),
                        String.valueOf(jsonObject.getInt("recruitment_number")),
                        jsonObject.getString("intention_state"),
                        jsonObject.getString("introduction")
                );
                mEnrollmentList.add(enrollmentInfo);
              }else{
                String info = jsonObject.getString("info");
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
              }
            } catch (JSONException e) {

            }
          }
        },String.valueOf(enrollmentIdList.get(i)));
      }
    }

    mEnrollmentList.add(new EnrollmentInfo("计算机图形学", "本科生", "100","进行中","介绍就是xxx"));
    mEnrollmentList.add(new EnrollmentInfo("物联网", "进行中", "我是xxx","进行中","介绍是xxx"));
    adapter = new EnrollmentListAdapter(mEnrollmentList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    return view;
  }

}
