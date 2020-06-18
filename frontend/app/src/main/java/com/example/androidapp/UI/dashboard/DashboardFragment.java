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
import com.example.androidapp.fragment.QueryResult.Base;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;
import com.example.androidapp.request.follow.GetFanlistRequest;
import com.example.androidapp.request.follow.GetWatchlistRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;
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
    private int mNumFocus;
    private int mNumFocused;
    private String type;

    public String mTitle;
    public String mMajor;
    public String mDegree;
    public String mTeacherNumber;
    public String mStudentNumber;
    public String mIdNumber;
    public String mGender;

    public String mName;
    public String mSchool;
    public String mDepartment;

    public String mSignature;
    public String mPhone;
    public String mEmail;
    public String mHomepage;
    public String mAddress;
    public String mIntroduction;
    public String mUrl;
    public String mDirection;
    public String mInterest;
    public String mResult;
    public String mExperience;

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


        type = BasicInfo.TYPE;

        pagerAdapter = new HomepagePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), type, -1);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        break;
                    }
                    case 1: {
                        ((StudyInfoFragment) pagerAdapter.getRegisteredFragment(1)).setInfo();
                        break;
                    }
                    default: {
//                        ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        break;
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 编辑信息按钮点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });




//        // 获取个性签名
//        new GetInfoPlusRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//                    Boolean status = jsonObject.getBoolean("status");
//                    if(status){
//                        mSignature = jsonObject.getString("signature");
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                signature.setText(mSignature);
//                            }
//                        });
//                    }else{
//                        String info = jsonObject.getString("info");
//                    }
//                } catch (JSONException e) {
//
//                }
//            }
//        },"I","","").send();

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
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = (JSONArray) jsonObject.get("watchlist_teachers");
                        JSONArray jsonArray1 = (JSONArray) jsonObject.get("watchlist_students");
                        mNumFocus = jsonArray.length()+jsonArray1.length();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                numFocus.setText(String.valueOf(mNumFocus));
                            }
                        });
                    }else{
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {

                }
            }
        }).send();

        // 获取被关注人数
        new GetFanlistRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = (JSONArray) jsonObject.get("fanlist_teachers");
                        JSONArray jsonArray1 = (JSONArray) jsonObject.get("fanlist_students");
                        mNumFocused = jsonArray.length()+jsonArray1.length();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                numFocused.setText(String.valueOf(mNumFocused));
                            }
                        });
                    }else{
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {

                }
            }
        }).send();

        //显示
//        name.setText(BasicInfo.ACCOUNT);
//        signature.setText(mSignature);
//        numFocus.setText(String.valueOf(mNumFocus));
//        numFocused.setText(String.valueOf(mNumFocused));

        return root;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//    setInfo();
        getInfo();
    }

    private void setFragmentInfo() {

    }

    protected void getInfo() {
        final int[] count = {0};

        // 获取用户名和类型
        new GetInfoRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                count[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mName = jsonObject.getString("name");
                        mGender = jsonObject.getString("gender");
                        mSchool = jsonObject.getString("school");
                        mDepartment = jsonObject.getString("department");
                        if (type.equals("S")) {
                            mMajor = jsonObject.getString("major");
                            mDegree = jsonObject.getString("degree");
                        }else {
                            mTitle = jsonObject.getString("title");
                        }

                        count[0]++;
                    } else {
                        String info = jsonObject.getString("info");
                        count[0]++;
                    }
                } catch (JSONException e) {
                    count[0]++;
                }
            }
        }, "I", null, null).send();

// 获取个性签名
        new GetInfoPlusRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                count[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mSignature = jsonObject.getString("signature");
                        mPhone = jsonObject.getString("phone");
                        mEmail = jsonObject.getString("email");
                        mHomepage = jsonObject.getString("homepage");
                        mAddress = jsonObject.getString("address");
                        mIntroduction = jsonObject.getString("introduction");
                        mIdNumber = jsonObject.getString("id_number");
                        if (type.equals("S")) {
                            mStudentNumber = jsonObject.getString("student_number");
                            mInterest = jsonObject.getString("research_interest");
                            mExperience = jsonObject.getString("research_experience");
                            mUrl = jsonObject.getString("promotional_video_url");
                        } else {
                            mTeacherNumber = jsonObject.getString("teacher_number");
                            mDirection = jsonObject.getString("research_fields");
                            mResult = jsonObject.getString("research_achievements");
                            mUrl = jsonObject.getString("promotional_video_url");
                        }
                        signature.setText(mSignature);
                        count[0]++;
                        while (count[0] != 2) {

                        }

                        ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        // ((StudyInfoFragment) pagerAdapter.getRegisteredFragment(1)).setInfo();
                    } else {
                        String info = jsonObject.getString("info");
                        count[0]++;
                    }
                } catch (JSONException e) {
                    count[0]++;
                }
            }
        }, "I", null, null).send();
    }

}
