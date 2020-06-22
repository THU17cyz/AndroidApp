package com.example.androidapp.fragment.QueryResult;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.myView.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.myView.SelectList;
import com.example.androidapp.request.follow.AddToWatchRequest;
import com.example.androidapp.request.follow.DeleteFromWatchRequest;
import com.example.androidapp.util.BasicInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.orderSpinner)
    protected Spinner orderSpinner;

    @BindView(R.id.selectText)
    protected TextView selectText;

    protected ArrayAdapter<String> spinnerAdapter;

    public boolean isFilterOpen = false;

    @BindView(R.id.recycleView)
    protected RecyclerView recyclerView;

    protected ShortProfileAdapter mShortProfileAdapter;

    SelectList selectList;
    protected ArrayList<ShortProfile> mProfileList;

    protected ArrayList<ShortProfile> filteredProfileList;
    private Lock lock = new ReentrantLock();



    protected Unbinder unbinder;

//    LoadService loadService;

    protected String[] order;
    boolean[] filters = new boolean[] {false, false, false, false};
    protected int current_order = 0;

    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        mProfileList = new ArrayList<>();
        filteredProfileList = new ArrayList<>();
//        mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
//                test_url,999, true, false));
        mShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
        mShortProfileAdapter.setRecyclerManager(recyclerView);//设置RecyclerView特性
//        mShortProfileAdapter.openLeftAnimation();//设置加载动画

        addButtonListener(mShortProfileAdapter, mProfileList);

        mShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return root;

    }

    protected void initViews() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, order);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        orderSpinner.setAdapter(spinnerAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                current_order = pos;
                adjustList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.selectText)
    public void openSelectWindow() {
        if (isFilterOpen) {
            // isFilterOpen = false;
            if (selectList != null) selectList.dismiss();
            // selectText.setTextColor(Color.BLACK);
        } else {
            isFilterOpen = true;
            // selectText.setTextColor(Color.BLUE);
            selectList = new SelectList(getContext());
            selectList.showPopupWindow(orderSpinner);

        }

//        orderList.setOutSideTouchable(true);
//        orderList.setPopupGravity(Gravity.BOTTOM);
//        orderList.setAlignBackground(true);
//        orderList.setAlignBackgroundGravity(Gravity.TOP);
        // orderList.setBackground(0);

    }

    @Override
    public void onStart() {
        super.onStart();
        int i = 0;
        lock.lock();
        for (ShortProfile shortProfile: mProfileList) {
            boolean true_state = BasicInfo.isInWatchList(shortProfile.id, shortProfile.isTeacher);
            if (true_state && !shortProfile.isFan) {
                shortProfile.isFan = true;
                mShortProfileAdapter.notifyItemChanged(i);
            }
            if (!true_state && shortProfile.isFan) {
                shortProfile.isFan = false;
                mShortProfileAdapter.notifyItemChanged(i);
            }
            i++;
        }
        lock.unlock();
    }

    public void setFilterClosed() {
        this.isFilterOpen = false;
    }

    public boolean[] getFilters() {
        return filters;
    }

    void addProfileItem(boolean isRefresh, ShortProfile shortProfile) {
        if (recyclerView.isComputingLayout()) {
            Log.e("errorrecyclerview", "ohno");
            recyclerView.post(() -> {
                if (isRefresh) {

                    for (ShortProfile tmp: mProfileList) {
                        if (tmp.id == shortProfile.id) return;
                    }
                    mProfileList.remove(0);
                    mShortProfileAdapter.notifyItemRemoved(0);
                }
                if (shortProfile.id == BasicInfo.ID && shortProfile.isTeacher == BasicInfo.IS_TEACHER) return; // 如果是自己，筛去
                lock.lock();
                mProfileList.add(shortProfile);
                lock.unlock();
            });


        } else {
            if (isRefresh) {
                for (ShortProfile tmp: mProfileList) {
                    if (tmp.id == shortProfile.id) return;
                }
                mProfileList.remove(0);
                mShortProfileAdapter.notifyItemRemoved(0);
            }
            if (shortProfile.id == BasicInfo.ID && shortProfile.isTeacher == BasicInfo.IS_TEACHER) return; // 如果是自己，筛去
            lock.lock();
            mProfileList.add(shortProfile);
            lock.unlock();
        }
    }

    private void visitHomePage(int position) {
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        ShortProfile shortProfile = mProfileList.get(position);
        intent.putExtra("id",shortProfile.id);
        intent.putExtra("isTeacher", shortProfile.isTeacher);
        intent.putExtra("isFan", shortProfile.isFan);
        intent.putExtra("profile", shortProfile);
        startActivity(intent);
    }

    private void addButtonListener(ShortProfileAdapter shortProfileAdapter, ArrayList<ShortProfile> shortProfileArrayList) {
        shortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FocusButton btn = ((FocusButton) view);
            btn.startLoading(() -> {
                ShortProfile profile = shortProfileArrayList.get(position);
                if (profile.isFan) {
                    new DeleteFromWatchRequest(new okhttp3.Callback() {
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
                    new AddToWatchRequest(new okhttp3.Callback() {
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

    public void adjustList() {
        int i = 0;
        ArrayList<Integer> removed = new ArrayList<>();
        mProfileList.addAll(filteredProfileList);
        filteredProfileList.clear();
        for (ShortProfile shortProfile: mProfileList) {
            if (filters[0] && !shortProfile.isMale) {
                removed.add(i);
            }
            if (filters[1] && shortProfile.isMale) {
                removed.add(i);
            }
            if (filters[2] && !shortProfile.isValidated) {
                removed.add(i);
            }
            i++;
        }
        for (int j = removed.size() - 1; j >= 0; j--) {
            int idx = removed.get(j);
            filteredProfileList.add(mProfileList.get(idx));
            mProfileList.remove(idx);
            mShortProfileAdapter.notifyItemRemoved(idx);
        }

        if (current_order == 0) {
            Collections.sort(mProfileList, (p1, p2) -> Integer.valueOf(p2.relate).compareTo(Integer.valueOf(p1.relate)));
        } else {
            Collections.sort(mProfileList, (p1, p2) -> Integer.valueOf(p2.fanNum).compareTo(Integer.valueOf(p1.fanNum)));
        }
        mShortProfileAdapter.notifyItemRangeChanged(0, mProfileList.size());

    }

    public void filterResult(boolean[] filters) {
//        loadService = LoadSir.getDefault().register(recyclerView, (com.kingja.loadsir.callback.Callback.OnReloadListener) v -> {
//
//        });
        int i = 0;
        for (Boolean filter : filters) {
            this.filters[i] = filter;
            i++;
        }
        adjustList();
//        loadService.showSuccess();
    }
}
