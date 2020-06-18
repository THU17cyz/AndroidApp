package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.androidapp.request.intention.CreateRecruitIntentionRequest;
import com.example.androidapp.request.intention.DeleteRecruitIntentionRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionDetailRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionRequest;
import com.example.androidapp.request.intention.UpdateRecruitIntentionRequest;
import com.example.androidapp.util.BasicInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EditEnrollmentInfoFragment extends Fragment {

  @BindView(R.id.btn_add)
  FloatingActionButton btn_add;

  @BindView(R.id.btn_concern)
  Button btn_concern;

  RecyclerView recyclerView;
  EditEnrollmentListAdapter adapter;

  ArrayList<EnrollmentInfo> mEnrollmentList;
  private int teacherId;
  private List<Integer> enrollmentIdList;

  private Unbinder unbinder;

  //To do
  public EditEnrollmentInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);
    unbinder = ButterKnife.bind(this,view);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    mEnrollmentList = new ArrayList<>();
    adapter = new EditEnrollmentListAdapter(mEnrollmentList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    // 显示目前申请意向

    // 获取申请意向id列表
    new GetRecruitIntentionRequest(new okhttp3.Callback() {

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
            JSONArray array = jsonObject.getJSONArray("recruitment_id_list");
            enrollmentIdList = new ArrayList<>();
            for (int i=0;i<array.length();i++){
              enrollmentIdList.add(array.getInt(i));
            }

            // 按id获取申请意向
            mEnrollmentList = new ArrayList<>();
            if(enrollmentIdList!=null){
              for(int i=0;i<enrollmentIdList.size();i++){
                new GetRecruitIntentionDetailRequest(new okhttp3.Callback() {
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
                        enrollmentInfo.setType(EnrollmentInfo.Type.UPDATE);

                        getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                            mEnrollmentList.add(enrollmentInfo);
                            adapter.notifyDataSetChanged();
                          }
                        });


                      }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                      }
                    } catch (JSONException e) {

                    }
                  }
                },String.valueOf(enrollmentIdList.get(i))).send();
              }
            }


          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },String.valueOf(BasicInfo.ID)).send();




//    ArrayList<EnrollmentInfo> mEnrollmentList = new ArrayList<>();
//    mEnrollmentList.add(new EnrollmentInfo("计算机图形学", "本科生", "100","进行中","介绍就是xxx"));
//    mEnrollmentList.add(new EnrollmentInfo("物联网", "进行中", "我是xxx","进行中","介绍是xxx"));


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
        EnrollmentInfo enrollmentInfo = new EnrollmentInfo("","","","","",-1,EnrollmentInfo.Type.ADD);
        mEnrollmentList.add(enrollmentInfo);
        adapter = new EditEnrollmentListAdapter(mEnrollmentList,getContext());
      }
    });

    btn_concern.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        for(int i=0;i<mEnrollmentList.size();i++){
          EnrollmentInfo enrollmentInfo = mEnrollmentList.get(i);
          if(enrollmentInfo.type==EnrollmentInfo.Type.ADD){
            new CreateRecruitIntentionRequest(new okhttp3.Callback() {
              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {

              }

              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

              }
            },
                    enrollmentInfo.studentType,
                    enrollmentInfo.number,
                    enrollmentInfo.direction,
                    enrollmentInfo.introduction,
                    enrollmentInfo.state,
                    null);
          }
          else if(enrollmentInfo.type==EnrollmentInfo.Type.DELETE){
            new DeleteRecruitIntentionRequest(new okhttp3.Callback() {
              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {

              }

              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

              }
            },String.valueOf(enrollmentInfo.enrollmentId));
          } else {
            new UpdateRecruitIntentionRequest(new okhttp3.Callback() {
              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {

              }

              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

              }
            },
                    String.valueOf(enrollmentInfo.enrollmentId),
                    enrollmentInfo.studentType,
                    enrollmentInfo.number,
                    enrollmentInfo.direction,
                    enrollmentInfo.introduction,
                    enrollmentInfo.state,
                    null);
          }
        }
      }
    });


    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

}
