package com.example.androidapp.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.myView.FocusButton;
import com.example.androidapp.request.follow.AddToWatchRequest;
import com.example.androidapp.request.follow.DeleteFromWatchRequest;
import com.example.androidapp.util.BasicInfo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;
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

public class ProfileListFragment extends Fragment {
    protected Unbinder unbinder;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    protected ArrayList<ShortProfile> mProfileList;
    protected ShortProfileAdapter mShortProfileAdapter;
    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_list, container, false);
        unbinder = ButterKnife.bind(this, root);

        mProfileList = new ArrayList<>();

        mShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
        mShortProfileAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        mShortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FocusButton btn = ((FocusButton) view);
            btn.startLoading(() -> {
                ShortProfile profile = mProfileList.get(position);
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

        // RecycleView的监听事件
        mShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void visitHomePage(int position) {
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        ShortProfile shortProfile = mProfileList.get(position);
        intent.putExtra("id", shortProfile.id);
        intent.putExtra("isTeacher", shortProfile.isTeacher);
        intent.putExtra("isFan", shortProfile.isFan);
        intent.putExtra("profile", shortProfile);
        startActivity(intent);
    }
}
