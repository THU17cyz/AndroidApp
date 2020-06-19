package com.example.androidapp.fragment.HomepageEdit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.R;
import com.example.androidapp.adapter.EditEnrollmentListAdapter;
import com.example.androidapp.entity.EnrollmentInfo;
import com.example.androidapp.request.intention.ClearAllIntentionRequest;
import com.example.androidapp.request.intention.CreateRecruitIntentionRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionDetailRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionRequest;
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

public class EditEnrollmentInfoFragment extends Fragment
implements View.OnClickListener{

  @BindView(R.id.btn_add)
  FloatingActionButton btn_add;

//  @BindView(R.id.btn_concern)
//  Button btn_concern;

  RecyclerView recyclerView;

  EditEnrollmentListAdapter adapter;

  private ArrayList<EnrollmentInfo> mEnrollmentList;

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
    mEnrollmentList = new ArrayList<>();
    adapter = new EditEnrollmentListAdapter(mEnrollmentList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    // 获取招收意向id列表
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

            // 按id获取招收意向

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
                                jsonObject.getString("introduction"));
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



//    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//      @Override
//      public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//        Toast.makeText(getActivity(), "testItemClick " + i, Toast.LENGTH_SHORT).show();
//      }
//    });

    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        Log.d("index",String.valueOf(i));
        ImageView imageView = (ImageView)view;
        mEnrollmentList.remove(i);
        adapter.notifyDataSetChanged();
      }
    });

    btn_add.setOnClickListener(this);

//    btn_concern.setOnClickListener(this);

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  public void update() {
    new ClearAllIntentionRequest(new okhttp3.Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e("response", "FA");
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
          String info = jsonObject.getString("info");
          getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());

          // 全部删除以后再插入
          for(int i=0;i<mEnrollmentList.size();i++){
            EnrollmentInfo enrollmentInfo = mEnrollmentList.get(i);
            new CreateRecruitIntentionRequest(new okhttp3.Callback() {
              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("response", "res");
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
                  String info = jsonObject.getString("info");
                  getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                } catch (JSONException e) {
                }
              }
            },
                    enrollmentInfo.studentType,
                    enrollmentInfo.number,
                    enrollmentInfo.direction,
                    enrollmentInfo.introduction,
                    enrollmentInfo.state,
                    null).send();
          }


        } catch (JSONException e) {
        }
      }
    }).send();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_add:
      {
        if(mEnrollmentList.size()>= BasicInfo.MAX_INTENTION_NUMBER){
          Toast.makeText(getContext(),"已达到意向数量上限",Toast.LENGTH_SHORT).show();
          break;
        }
        Toast.makeText(getActivity(),"添加",Toast.LENGTH_SHORT).show();
        EnrollmentInfo enrollmentInfo = new EnrollmentInfo("","本科生","","进行","",-1,EnrollmentInfo.Type.ADD);
        mEnrollmentList.add(enrollmentInfo);
        adapter.notifyDataSetChanged();
        break;
      }
    }

  }
}
