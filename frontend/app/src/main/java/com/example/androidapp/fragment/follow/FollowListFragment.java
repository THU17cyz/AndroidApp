package com.example.androidapp.fragment.follow;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.fragment.ProfileListFragment;
import com.example.androidapp.request.follow.AddToWatchRequest;
import com.example.androidapp.request.follow.DeleteFromWatchRequest;
import com.example.androidapp.request.follow.GetFanlistRequest;
import com.example.androidapp.request.follow.GetWatchlistRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.ViewFolder;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowListFragment extends Fragment {

//    @BindView(R.id.follow_list_layout)
//    ConstraintLayout constraintLayout;

    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;

    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;

    @BindView(R.id.t_recycler_view)
    RecyclerView tRecyclerView;

//  @BindView(R.id.t_refresh_layout)
//  RefreshLayout tRefreshLayout;

    @BindView(R.id.s_recycler_view)
    RecyclerView sRecyclerView;

    @BindView(R.id.follow_list_teacher)
    TextView teacherTextView;

//  @BindView(R.id.s_refresh_layout)
//  RefreshLayout sRefreshLayout;

    boolean isWatchList;

    Unbinder unbinder;

    ArrayList<ShortProfile> sProfileList;
    ArrayList<ShortProfile> tProfileList;

    LoadService tLoadService;
    LoadService sLoadService;

    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

    protected ShortProfileAdapter tShortProfileAdapter;
    protected ShortProfileAdapter sShortProfileAdapter;

    public FollowListFragment(boolean isWatchList) {
        this.isWatchList = isWatchList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_follow_list, container, false);
        unbinder = ButterKnife.bind(this, root);

        sProfileList = new ArrayList<>();
        tProfileList = new ArrayList<>();

        tShortProfileAdapter = new ShortProfileAdapter(tProfileList, getContext());//初始化NameAdapter
        tShortProfileAdapter.setRecyclerManager(tRecyclerView);//设置RecyclerView特性

        sShortProfileAdapter = new ShortProfileAdapter(sProfileList, getContext());//初始化NameAdapter
        sShortProfileAdapter.setRecyclerManager(sRecyclerView);//设置RecyclerView特性

        // RecycleView 本身的监听事件
        tShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(true, position);
        });

        // RecycleView 本身的监听事件
        sShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(false, position);
        });

        addButtonListener(tShortProfileAdapter, tProfileList);
        addButtonListener(sShortProfileAdapter, sProfileList);

//        getFanList();
//        getWatchList();


        addDivider();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        int tSize = tProfileList.size();
        int sSize = sProfileList.size();
        tProfileList.clear();
        sProfileList.clear();
        tShortProfileAdapter.notifyItemRangeRemoved(0, tSize);
        sShortProfileAdapter.notifyItemRangeRemoved(0, sSize);
        if (isWatchList) {
            for (ShortProfile shortProfile : BasicInfo.WATCH_LIST) {
                if (shortProfile.isTeacher) tProfileList.add(shortProfile);
                else sProfileList.add(shortProfile);
            }
        } else {
            for (ShortProfile shortProfile : BasicInfo.FAN_LIST) {
                boolean is_fan = false;
                for (ShortProfile shortProfile2 : BasicInfo.WATCH_LIST) {
                    if (shortProfile2.id == shortProfile.id) {
                        is_fan = true;
                    }
                }
                shortProfile.isFan = is_fan;
                if (shortProfile.isTeacher) tProfileList.add(shortProfile);
                else sProfileList.add(shortProfile);
            }
        }


//        int i = 0;
//        for (ShortProfile shortProfile: sProfileList) {
//            if (!BasicInfo.isInWatchList(shortProfile.id, false)) {
//                break;
//            }
//            i++;
//        }
//        if (i < sProfileList.size()) sProfileList.remove(i);
//        sShortProfileAdapter.notifyItemRemoved(i);
//
//        i = 0;
//        for (ShortProfile shortProfile: tProfileList) {
//            if (!BasicInfo.isInWatchList(shortProfile.id, true)) {
//                break;
//            }
//            i++;
//        }
//        if (i < tProfileList.size())tProfileList.remove(i);
//        tShortProfileAdapter.notifyItemRemoved(i);
    }

    @Override
    public void onResume() {
        super.onResume();
//        int tSize = tProfileList.size();
//        int sSize = sProfileList.size();
//        tProfileList.clear();
//        sProfileList.clear();
//        tShortProfileAdapter.notifyItemRangeRemoved(0, tSize);
//        sShortProfileAdapter.notifyItemRangeRemoved(0, sSize);
//        if (isWatchList) {
//            for (ShortProfile shortProfile : BasicInfo.WATCH_LIST) {
//                if (shortProfile.isTeacher) tProfileList.add(shortProfile);
//                else sProfileList.add(shortProfile);
//            }
//        } else {
//            for (ShortProfile shortProfile : BasicInfo.FAN_LIST) {
//                if (shortProfile.isTeacher) tProfileList.add(shortProfile);
//                else sProfileList.add(shortProfile);
//            }
//        }
    }

    private void visitHomePage(boolean isTop,  int position) {
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        ShortProfile shortProfile;
        if (isTop) {
            shortProfile = tProfileList.get(position);
        } else {
            shortProfile = sProfileList.get(position);
        }
        intent.putExtra("id",shortProfile.id);
        intent.putExtra("isTeacher", shortProfile.isTeacher);
        intent.putExtra("isFan", shortProfile.isFan);
        intent.putExtra("profile", shortProfile);
        startActivity(intent);
    }

    private void addDivider() {
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration1.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        tRecyclerView.addItemDecoration(dividerItemDecoration1);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration2.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));

        sRecyclerView.addItemDecoration(dividerItemDecoration2);
    }


    private void initLoad() {
        sLoadService = LoadSir.getDefault().register(sRecyclerView,
                (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> {

        });
        tLoadService = LoadSir.getDefault().register(tRecyclerView,
                (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> {

                });
    }

    private void loadSuccess() {
        sLoadService.showSuccess();
        tLoadService.showSuccess();
    }

    private void loadFail() {
        sLoadService.showSuccess();
        tLoadService.showSuccess();
    }

    private void getFanList() {
        initLoad();
        new GetFanlistRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error1", e.toString());
                loadFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("watchlist_teachers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), true);
                        addProfileItem(tProfileList, tRecyclerView, shortProfile);
                    }
                    jsonArray = (JSONArray) jsonObject.get("watchlist_students");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);
                        addProfileItem(sProfileList, sRecyclerView, shortProfile);
                    }
                    loadSuccess();
                } catch (JSONException e) {
                    loadFail();
                    Log.e("error2", e.toString());
                }

            }
        }).send();
    }

    private void getWatchList() {
        new GetWatchlistRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error1", e.toString());
                loadFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("watchlist_teachers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), true);
                        addProfileItem(tProfileList, tRecyclerView, shortProfile);
                    }
                    jsonArray = (JSONArray) jsonObject.get("watchlist_students");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);
                        addProfileItem(sProfileList, sRecyclerView, shortProfile);
                    }
                    loadSuccess();
                } catch (JSONException e) {
                    Log.e("error2", e.toString());
                    loadFail();
                }

            }
        }).send();
    }

    void addProfileItem(ArrayList<ShortProfile> profileArrayList, RecyclerView recyclerView, ShortProfile shortProfile) {
        if (recyclerView.isComputingLayout()) {
            Log.e("errorrecyclerview", "ohno");
            recyclerView.post(() -> {
                profileArrayList.add(shortProfile);
            });
        } else {
            profileArrayList.add(shortProfile);
        }
    }

    private void addButtonListener(ShortProfileAdapter shortProfileAdapter, ArrayList<ShortProfile> shortProfileArrayList) {
        shortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FocusButton btn = ((FocusButton) view);
            btn.startLoading(() -> {
                ShortProfile profile = shortProfileArrayList.get(position);
                if (profile.isFan) {
                    new DeleteFromWatchRequest(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e("error1", e.toString());
                            getActivity().runOnUiThread(btn::clickFail);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String resStr = response.body().string();
                            Log.e("response", resStr);
                            try {
                                JSONObject jsonObject = new JSONObject(resStr);
                                profile.isFan = false;
                                BasicInfo.removeFromWatchList(profile.id, profile.isTeacher);
                                getActivity().runOnUiThread(btn::clickSuccess);
                            } catch (JSONException e) {
                                Log.e("error2", e.toString());
                                getActivity().runOnUiThread(btn::clickFail);
                            }

                        }
                    }, String.valueOf(profile.id), profile.isTeacher).send();
                } else {
                    new AddToWatchRequest(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e("error1", e.toString());
                            getActivity().runOnUiThread(btn::clickFail);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String resStr = response.body().string();
                            Log.e("response", resStr);
                            try {
                                JSONObject jsonObject = new JSONObject(resStr);
                                profile.isFan = true;
                                BasicInfo.addToWatchList(profile);
                                getActivity().runOnUiThread(btn::clickSuccess);
                            } catch (JSONException e) {
                                Log.e("error2", e.toString());
                                getActivity().runOnUiThread(btn::clickFail);
                            }

                        }
                    }, String.valueOf(profile.id), profile.isTeacher).send();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

