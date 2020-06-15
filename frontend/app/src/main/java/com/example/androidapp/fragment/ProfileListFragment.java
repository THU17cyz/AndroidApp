package com.example.androidapp.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.ShortProfileAdapter;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileListFragment extends Fragment {
    protected Unbinder unbinder;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected ArrayList<ShortProfile> mProfileList;
    protected ShortProfileAdapter mShortProfileAdapter;

    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

    //To do
    public ProfileListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_list, container, false);
        unbinder = ButterKnife.bind(this, root);

        mProfileList = new ArrayList<>();
        mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
                test_url,999, true, false));
        mProfileList.add(new ShortProfile(1, "黄翔", "清华大学",
                test_url,999, true, false));

        mShortProfileAdapter = new ShortProfileAdapter(mProfileList, getContext());//初始化NameAdapter
        mShortProfileAdapter.setRecyclerManager(mRecyclerView);//设置RecyclerView特性
        // mTestAdapter.openLeftAnimation();//设置加载动画

        // 子组件的监听事件，比如按钮
        // 在Adapter里注册（addOnClickListener）
        mShortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Toast.makeText(getActivity(), "testItemChildClick" + position, Toast.LENGTH_SHORT).show();
            ((FocusButton) view).click();
        });

        // RecycleView 本身的监听事件
        mShortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            Toast.makeText(getActivity(), "testItemClick" + position, Toast.LENGTH_SHORT).show();
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
