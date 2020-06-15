package com.example.androidapp.fragment.QueryResult;

import android.graphics.Color;
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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.MyBaseAdapter;
import com.example.androidapp.adapter.queryPageAdapter.TeacherAdapter;
import com.example.androidapp.entity.TeacherProfile;
import com.example.androidapp.popup.SelectList;
import com.example.androidapp.request.search.SearchTeacherRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class Base extends Fragment {
    @BindView(R.id.orderSpinner)
    protected Spinner orderSpinner;

    @BindView(R.id.selectText)
    protected TextView selectText;

    protected ArrayAdapter<String> spinnerAdapter;

    public boolean isFilterOpen = false;

    @BindView(R.id.recycleView)
    protected RecyclerView recyclerView;
    protected MyBaseAdapter adapter;

    SelectList selectList;


    protected Unbinder unbinder;

    LoadService loadService;

    protected String[] order;
    protected int current_order = 0;

    public Base() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        ArrayList<TeacherProfile> mNameList = new ArrayList<>();
        mNameList.add(new TeacherProfile(1, "黄翔", "清华大学", "", 999));
        adapter = new TeacherAdapter(mNameList, getContext());//初始化NameAdapter
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


        loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });

        new SearchTeacherRequest(new okhttp3.Callback() {
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
                    loadService.showSuccess();
                } catch (JSONException e) {

                }
            }
        }, "烦").send();

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
//        if (selectText.isActivated()) {
//            orderList.dismiss();
//            selectText.setActivated(false);
//        } else {
//            selectText.setActivated(true);
//            orderList = new OrderList(getContext());
//            orderList.showPopupWindow(orderSpinner);
//            orderList.setBackground(0);
//        }
        if (isFilterOpen) {
            isFilterOpen = false;
            if (selectList != null) selectList.dismiss();
            selectText.setTextColor(Color.BLACK);
        } else {
            isFilterOpen = true;
            selectText.setTextColor(Color.BLUE);
            selectList = new SelectList(getContext());
            selectList.showPopupWindow(orderSpinner);
        }

//        orderList.setOutSideTouchable(true);
//        orderList.setPopupGravity(Gravity.BOTTOM);
//        orderList.setAlignBackground(true);
//        orderList.setAlignBackgroundGravity(Gravity.TOP);
        // orderList.setBackground(0);

    }
}
