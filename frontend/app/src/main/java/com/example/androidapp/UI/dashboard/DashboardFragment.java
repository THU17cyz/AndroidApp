package com.example.androidapp.UI.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.activity.EditInfoActivity;
import com.example.androidapp.activity.HomepageActivity;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.request.follow.GetFanlistRequest;
import com.example.androidapp.request.follow.GetWatchlistRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class DashboardFragment
        extends Fragment
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.homepage_name)
    TextView name;

    @BindView(R.id.signature)
    TextView signature;

    @BindView(R.id.num_focus)
    TextView numFocus;

    @BindView(R.id.num_focused)
    TextView numFocused;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_edit)
    Button button;

    private WholeProfile wholeProfile;
    private ShortProfile shortProfile;

    private HomepagePagerAdapter pagerAdapter;

    private String mAccount;
    private String mSignature;
    private int mNumFocus;
    private int mNumFocused;
    private String type;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this,root);

        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).init();

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


//        // 获取用户名和类型
//        new GetInfoRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//
//                    Boolean status = jsonObject.getBoolean("status");
//                    if(status){
//                        mAccount = jsonObject.getString("account");
//                        type = jsonObject.getJSONObject("teacher_id")==null?"S":"T";
//                    }else{
//                        String info = jsonObject.getString("info");
//                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
//                    }
//                } catch (JSONException e) {
//
//                }
//            }
//        },"I",null,null);

        pagerAdapter.setType(type);


        pagerAdapter = new HomepagePagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HomepageActivity.class);
                startActivity(intent);
            }
        });



        // 获取个性签名
        new GetInfoPlusRequest(new okhttp3.Callback() {
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

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        mSignature = jsonObject.getString("signature");
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        },"I",null,null);

        // 获取头像

        // 获取关注人数
        new GetWatchlistRequest(new okhttp3.Callback() {
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

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = (JSONArray) jsonObject.get("watchlist_teachers");
                        JSONArray jsonArray1 = (JSONArray) jsonObject.get("watchlist_students");
                        mNumFocus = jsonArray.length()+jsonArray1.length();
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        });

        // 获取被关注人数
        new GetFanlistRequest(new okhttp3.Callback() {
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

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = (JSONArray) jsonObject.get("fanlist_teachers");
                        JSONArray jsonArray1 = (JSONArray) jsonObject.get("fanlist_students");
                        mNumFocused = jsonArray.length()+jsonArray1.length();
                    }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        });

        //显示
        name.setText(mAccount);
        signature.setText(mSignature);
        numFocus.setText(mNumFocus);
        numFocused.setText(mNumFocused);

        return root;


    }

//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
}
