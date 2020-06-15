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
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
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
    protected ShortProfileAdapter mShortProfileAdapter;

    SelectList selectList;
    protected ArrayList<ShortProfile> mProfileList;


    protected Unbinder unbinder;

    LoadService loadService;

    protected String[] order;
    protected int current_order = 0;

    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";


    public Base() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        mProfileList = new ArrayList<>();
        mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
                test_url,999, true, false));
        mShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
        mShortProfileAdapter.setRecyclerManager(recyclerView);//设置RecyclerView特性
        mShortProfileAdapter.openLeftAnimation();//设置加载动画

        mShortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            // TODO 关注
            ((FocusButton) view).click();
        });

        mShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            // TODO 进入其主页
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
        if (isFilterOpen) {
            isFilterOpen = false;
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
}
