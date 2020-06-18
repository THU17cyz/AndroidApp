package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.entity.ShortIntent;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.request.search.SearchApplyIntentionRequest;
import com.example.androidapp.request.search.SearchTeacherRequest;
import com.kingja.loadsir.callback.Callback;
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

public class Apply extends IntentFragment {
    private Unbinder unbinder;

    public Apply() {
        order = new String[]{"最相关（默认）", "关注人数最多"};
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
        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
        Log.e("query", query);
        new SearchApplyIntentionRequest(new okhttp3.Callback() {
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
                    jsonArray = (JSONArray) jsonObject.get("application_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortIntent shortIntent = new ShortIntent(jsonArray.getJSONObject(i), false);
                        // Log.e("..", "! " + shortProfile.isFan + shortProfile.id + " " + shortProfile.name);
                        addIntentItem(false, shortIntent);
                    }
                    adjustList();
                    loadService.showSuccess();
                    ((QueryResultActivity) getActivity()).querySet(0);
                } catch (JSONException e) {
                    loadService.showSuccess();
                }
            }
        }, query).send();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
