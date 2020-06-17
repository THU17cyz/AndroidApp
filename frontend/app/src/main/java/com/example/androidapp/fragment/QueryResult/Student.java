package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.queryInfo.StudentQueryInfo;
import com.example.androidapp.entity.queryInfo.TeacherQueryInfo;
import com.example.androidapp.request.search.SearchStudentRequest;
import com.example.androidapp.request.search.SearchTeacherRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class Student extends Base {

    public Student() {
        order = new String[]{"最相关（默认）", "关注人数最多"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState); // inflater.inflate(R.layout.fragment_teacher_result, container, false);
        return root;

    }


    public void loadQueryInfo(String query) {
        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
        new SearchStudentRequest(new okhttp3.Callback() {
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
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);
                        Log.e("..", "! " + shortProfile.isFan + shortProfile.id + " " + shortProfile.name);
                        addProfileItem(false, shortProfile);
                    }
                    adjustList();
                    loadService.showSuccess();
                    ((QueryResultActivity) getActivity()).querySet(1);
                } catch (JSONException e) {
                    loadService.showSuccess();
                }
            }
        }, query).send();
    }

    public void clearQueryResult() {
        int size = mProfileList.size();
        mProfileList.clear();
        mShortProfileAdapter.notifyItemRangeRemoved(0, size);
    }

    public void adjustList() {
        int i = 0;
        for (ShortProfile shortProfile: mProfileList) {
            if (filters[0] && !shortProfile.isMale) {
                mProfileList.remove(shortProfile);
                filteredProfileList.add(shortProfile);
                mShortProfileAdapter.notifyItemRemoved(i);
            }
            if (filters[1] && shortProfile.isMale) {
                mProfileList.remove(shortProfile);
                filteredProfileList.add(shortProfile);
                mShortProfileAdapter.notifyItemRemoved(i);
            }
            i++;
        }
    }

    public void filterResult(List<Boolean> filters) {
        int i = 0;
        for (Boolean filter : filters) {
            this.filters[i] = filter;
            i++;
        }
        adjustList();
    }
}
