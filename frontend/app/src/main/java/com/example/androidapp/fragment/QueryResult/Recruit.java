package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.entity.ShortIntent;
import com.example.androidapp.request.search.SearchRecruitIntentionRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 搜索结果（招生意向）
 */
public class Recruit extends IntentFragment {
    private Unbinder unbinder;

    public Recruit() {
        order = new String[]{"最相关（默认）"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, root);
        return root;

    }

    public void clearQueryResult() {
        int size = mIntentList.size();
        mIntentList.clear();
        mShortIntentAdapter.notifyItemRangeRemoved(0, size);
    }

    public void loadQueryInfo(String query) {
        LoadService loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
        Log.e("query", query);
        new SearchRecruitIntentionRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                loadService.showSuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("recruitment_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortIntent shortIntent = new ShortIntent(jsonArray.getJSONObject(i), true);
                        addIntentItem(false, shortIntent);
                    }
                    adjustList();
                    loadService.showSuccess();
                    ((QueryResultActivity) getActivity()).querySet(0);
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    loadService.showSuccess();
                }
            }
        }, query).send();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
