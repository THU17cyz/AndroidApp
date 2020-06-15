package com.example.androidapp.fragment.QueryResult;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.entity.TeacherProfile;
import com.example.androidapp.R;
import com.example.androidapp.entity.TeacherQueryInfo;
import com.example.androidapp.request.information.GetInformationRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class Teacher extends Base {

    public Teacher() {
        order = new String[]{"最相关（默认）", "关注人数最多"};

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);

        initViews();

        ArrayList<TeacherProfile> mNameList = new ArrayList<>();
        mNameList.add(new TeacherProfile(2, "黄翔", "清华大学", "", 999));
        adapter = new ShortProfileAdapter(mNameList, getContext());//初始化NameAdapter
        adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性
        adapter.openLeftAnimation();//设置加载动画

        // 子组件的监听事件，比如按钮
        // 在Adapter里注册（addOnClickListener）
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 用position获取点击的是什么
            // TODO 关注
            Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
            view.setBackground(getContext().getDrawable(R.drawable.shape_unwatch_button));
        });

        // RecycleView 本身的监听事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            // TODO 进入其主页
            Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return root;

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public List<TeacherQueryInfo> loadQueryInfo() {
        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
        for (Integer id: ((QueryResultActivity) getActivity()).getTeacherIdList()) {
            new GetInformationRequest(new okhttp3.Callback() {
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

                    } catch (JSONException e) {

                    }
                }
            }).send();
        }
        loadService.showSuccess();
        return new ArrayList<>();
    }


}
