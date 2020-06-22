package com.example.androidapp.fragment.homepageEdit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.R;
import com.example.androidapp.adapter.EditRecruitmentListAdapter;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.request.intention.ClearAllIntentionRequest;
import com.example.androidapp.request.intention.CreateRecruitIntentionRequest;
import com.example.androidapp.util.BasicInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EditRecruitmentInfoFragment extends Fragment
        implements View.OnClickListener{

    @BindView(R.id.btn_add)
    FloatingActionButton btn_add;

    RecyclerView recyclerView;

    EditRecruitmentListAdapter adapter;

    private ArrayList<RecruitmentInfo> mRecruitmentList;

    private Unbinder unbinder;

    //To do
    public EditRecruitmentInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);
        unbinder = ButterKnife.bind(this,view);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mRecruitmentList = new ArrayList<>();
        adapter = new EditRecruitmentListAdapter(mRecruitmentList, getContext());
        adapter.setRecyclerManager(recyclerView);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Log.d("index",String.valueOf(i));

                if(view.getId() == R.id.delete){
                    mRecruitmentList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        });

        btn_add.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setInfo();
    }

    public void setInfo() {
        for (RecruitmentInfo recruitmentInfo: BasicInfo.mRecruitmentList) {
            mRecruitmentList.add(new RecruitmentInfo((recruitmentInfo)));
        }
        adapter.notifyItemRangeInserted(0, BasicInfo.mRecruitmentList.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public boolean checkContent() {
        for(int i = 0;i < mRecruitmentList.size(); i++){
            RecruitmentInfo recruitmentInfo = mRecruitmentList.get(i);
            if(recruitmentInfo.direction==null||recruitmentInfo.direction.length()==0){
                return false;
            }
        }
        return true;
    }

    public void update() {
        BasicInfo.mRecruitmentList.clear();
        for(int i = 0; i < mRecruitmentList.size(); i++) {
            RecruitmentInfo recruitmentInfo = mRecruitmentList.get(i);
            BasicInfo.mRecruitmentList.add(recruitmentInfo);
        }
        new ClearAllIntentionRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("response", "FA");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                // getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    String info = jsonObject.getString("info");
                    // getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());

                    // 全部删除以后再插入
                    for(int i = 0; i< mRecruitmentList.size(); i++){
                        RecruitmentInfo recruitmentInfo = mRecruitmentList.get(i);
                        new CreateRecruitIntentionRequest(new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("response", "res");
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                String resStr = response.body().string();
                                Log.e("response", resStr);
                                try {
                                    // 解析json，然后进行自己的内部逻辑处理
                                    JSONObject jsonObject = new JSONObject(resStr);
                                    Boolean status = jsonObject.getBoolean("status");
                                    String info = jsonObject.getString("info");
                                } catch (JSONException e) {
                                }
                            }
                        },
                                recruitmentInfo.studentType,
                                recruitmentInfo.number,
                                recruitmentInfo.direction,
                                recruitmentInfo.introduction,
                                recruitmentInfo.state,
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
                if(mRecruitmentList.size()>= BasicInfo.MAX_INTENTION_NUMBER){
                    Toast.makeText(getContext(),"已达到意向数量上限",Toast.LENGTH_SHORT).show();
                    break;
                }
                // Toast.makeText(getActivity(),"添加",Toast.LENGTH_SHORT).show();
                RecruitmentInfo recruitmentInfo = new RecruitmentInfo("","本科生","","进行","",-1, RecruitmentInfo.Type.ADD);
                mRecruitmentList.add(recruitmentInfo);
                adapter.notifyItemInserted(mRecruitmentList.size() - 1);
                recyclerView.smoothScrollToPosition(mRecruitmentList.size() - 1);
                View view = getActivity().getCurrentFocus();
                if (view != null) view.clearFocus();
                break;
            }
        }

    }
}
