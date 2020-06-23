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
import com.example.androidapp.adapter.EditApplicationListAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.request.intention.ClearAllIntentionRequest;
import com.example.androidapp.request.intention.CreateApplyIntentionRequest;
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

public class EditApplicationInfoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_add)
    FloatingActionButton btn_add;

    RecyclerView recyclerView;
    EditApplicationListAdapter adapter;

    ArrayList<ApplicationInfo> mApplicationList;

    private Unbinder unbinder;

    public EditApplicationInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mApplicationList = new ArrayList<>();
        adapter = new EditApplicationListAdapter(mApplicationList, getContext());//初始化NameAdapter
        adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                System.out.println("thh" + i);
                mApplicationList.remove(i);
                adapter.notifyItemRemoved(i);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
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
        for (ApplicationInfo applicationInfo : BasicInfo.mApplicationList) {
            mApplicationList.add(new ApplicationInfo((applicationInfo)));
        }
        adapter.notifyItemRangeInserted(0, BasicInfo.mApplicationList.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add: {
                if (mApplicationList.size() >= BasicInfo.MAX_INTENTION_NUMBER) {
                    Toast.makeText(getContext(), "已达到意向数量上限", Toast.LENGTH_SHORT).show();
                    break;
                }
                // Toast.makeText(getActivity(),"添加",Toast.LENGTH_SHORT).show();
                ApplicationInfo applicationInfo = new ApplicationInfo("", "进行", "");
                mApplicationList.add(applicationInfo);
                adapter.notifyItemInserted(mApplicationList.size() - 1);
                recyclerView.smoothScrollToPosition(mApplicationList.size() - 1);
                View view = getActivity().getCurrentFocus();
                if (view != null) view.clearFocus();
                break;
            }
        }

    }

    public boolean checkContent() {
        for (int i = 0; i < mApplicationList.size(); i++) {
            ApplicationInfo applicationInfo = mApplicationList.get(i);
            if (applicationInfo.direction == null || applicationInfo.direction.length() == 0) {
                return false;
            }
        }
        return true;
    }

    public void update() {

        BasicInfo.mApplicationList.clear();
        for (int i = 0; i < mApplicationList.size(); i++) {
            ApplicationInfo applicationInfo = mApplicationList.get(i);
            BasicInfo.mApplicationList.add(applicationInfo);
        }

        new ClearAllIntentionRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("response", "FA");
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


                    // 全部删除以后再插入
                    for (int i = 0; i < mApplicationList.size(); i++) {
                        ApplicationInfo applicationInfo = mApplicationList.get(i);

                        new CreateApplyIntentionRequest(new okhttp3.Callback() {
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
                                applicationInfo.direction,
                                applicationInfo.profile,
                                applicationInfo.state,
                                null).send();
                    }

                } catch (JSONException e) {
                }
            }
        }).send();
    }


}
