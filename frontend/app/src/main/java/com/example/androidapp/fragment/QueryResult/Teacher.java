package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.entity.queryInfo.TeacherQueryInfo;
import com.example.androidapp.request.information.GetInformationRequest;
import com.example.androidapp.request.search.SearchTeacherRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class Teacher extends Base {

    public Teacher() {
        order = new String[]{"最相关（默认）", "关注人数最多"};

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState); //inflater.inflate(R.layout.fragment_teacher_result, container, false);

        return root;

    }

    public List<TeacherQueryInfo> loadQueryInfo() {
        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
////        for (Integer id: ((QueryResultActivity) getActivity()).getTeacherIdList()) {
////            new GetInformationRequest(new okhttp3.Callback() {
////                @Override
////                public void onFailure(@NotNull Call call, @NotNull IOException e) {
////                    Log.e("error", e.toString());
////                }
////
////                @Override
////                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
////                    String resStr = response.body().string();
////                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
////                    Log.e("response", resStr);
////                    try {
////                        // 解析json，然后进行自己的内部逻辑处理
////                        JSONObject jsonObject = new JSONObject(resStr);
////
////                    } catch (JSONException e) {
////
////                    }
////                }
////            }).send();
////        }
////        loadService.showSuccess();
        new SearchTeacherRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                loadService.showSuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
//                    JSONArray jsonArray = (JSONArray) jsonObject.get("teacher_id_list");
//                    teacherIdList = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                        teacherIdList.add(jsonObject2.getInt("id"));
//
//                    }
                    loadService.showSuccess();
                } catch (JSONException e) {
                    loadService.showSuccess();
                }
            }
        }, "烦").send();
        return new ArrayList<>();
    }


}
