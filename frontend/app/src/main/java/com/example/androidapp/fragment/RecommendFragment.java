package com.example.androidapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidapp.UI.home.HomeFragment;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.request.recommend.RecommendFitRequest;
import com.example.androidapp.request.recommend.RecommendFitStudentRequest;
import com.example.androidapp.request.recommend.RecommendFitTeacherRequest;
import com.example.androidapp.request.recommend.RecommendHotRequest;
import com.example.androidapp.request.recommend.RecommendRandomRequest;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends ProfileListFragment {

    boolean isTeacher;
    LoadService loadService;
    Activity activity;
    int fixed_num;


    public RecommendFragment(int num) {
        if (num == 0) isTeacher = true;
        else isTeacher = false;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
//        refreshLayout = ((HomeFragment) getParentFragment()).refreshLayout;
        activity = getActivity();
        getRecommendList(false);

        refreshLayout.setOnRefreshListener(refreshlayout -> {
            getRecommendList(true);
        });
        return root;

    }

    private void getRecommendList(boolean isRefresh) {
        if (!isRefresh) {
            loadService = LoadSir.getDefault().register(mRecyclerView, (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> {

            });
        }
        final int[] receiveCount = {0};

        new RecommendRandomRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (receiveCount[0] == 2) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(false);
                    } else {
                        activity.runOnUiThread(loadService::showSuccess);
                    }
                }
                receiveCount[0]++;
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONObject jsonObject1;
                    if (isTeacher) jsonObject1 = (JSONObject) jsonObject.get("teacher_info");
                    else jsonObject1 = (JSONObject) jsonObject.get("student_info");
                    addProfileItem(isRefresh, new ShortProfile(jsonObject1, isTeacher));
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(true);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                } catch (JSONException e) {
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                    Log.e("error", e.toString());
                }
            }
        }, isTeacher).send();

        new RecommendHotRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                if (receiveCount[0] == 2) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(false);
                    } else {
                        activity.runOnUiThread(loadService::showSuccess);
                    }
                }
                receiveCount[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONObject jsonObject1;
                    if (isTeacher) jsonObject1 = (JSONObject) jsonObject.get("teacher_info");
                    else jsonObject1 = (JSONObject) jsonObject.get("student_info");
                    addProfileItem(isRefresh, new ShortProfile(jsonObject1, isTeacher));
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(true);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                }
            }
        }, isTeacher).send();

        new RecommendFitRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (receiveCount[0] == 2) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(false);
                    } else {
                        activity.runOnUiThread(loadService::showSuccess);
                    }
                }
                receiveCount[0]++;
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    if (isTeacher) jsonArray = (JSONArray) jsonObject.get("teacher_info_list");
                    else jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), isTeacher);
                        Log.e("..", "! " + shortProfile.isFan + shortProfile.id + " " + shortProfile.name);
                        addProfileItem(isRefresh, shortProfile);
                    }
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(true);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    if (receiveCount[0] == 2) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            activity.runOnUiThread(loadService::showSuccess);
                        }
                    }
                    receiveCount[0]++;
                }
            }
        }, isTeacher).send();
    }

    void addProfileItem(boolean isRefresh, ShortProfile shortProfile) {
        if (mRecyclerView.isComputingLayout()) {
            Log.e("errorrecyclerview", "ohno");
            mRecyclerView.post(() -> {
                if (isRefresh) {
                    for (ShortProfile tmp: mProfileList) {
                        if (tmp.id == shortProfile.id) return;
                    }
                    mProfileList.remove(0);
                    mShortProfileAdapter.notifyItemRemoved(0);
                }
                mProfileList.add(shortProfile);
            });


        } else {
            if (isRefresh) {
                for (ShortProfile tmp: mProfileList) {
                    if (tmp.id == shortProfile.id) return;
                }
                mProfileList.remove(0);
                mShortProfileAdapter.notifyItemRemoved(0);
            }
            mProfileList.add(shortProfile);
        }
    }
}
