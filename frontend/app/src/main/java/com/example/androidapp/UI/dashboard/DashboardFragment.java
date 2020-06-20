package com.example.androidapp.UI.dashboard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.activity.EditInfoActivity;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.chatTest.GifSizeFilter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.fragment.homepage.ApplicationInfoFragment;
import com.example.androidapp.fragment.homepage.RecruitmentInfoFragment;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionDetailRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionRequest;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.MyImageLoader;
import com.example.androidapp.util.SizeConverter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.visit_homepage_title)
    TextView title;

    @BindView(R.id.visit_homepage_appbar)
    AppBarLayout app_bar;

    private WholeProfile wholeProfile;
    private ShortProfile shortProfile;

    private static final int REQUEST_CODE_CHOOSE = 11;

    private HomepagePagerAdapter pagerAdapter;

    private String mAccount;
    private int mNumFocus;
    private int mNumFocused;
    private String type;
    private int id;

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

    public ArrayList<ApplicationInfo> mApplicationList;
    public ArrayList<RecruitmentInfo> mRecruitmentList;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        System.out.println("onCreateView");

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this,root);

        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        type = BasicInfo.TYPE;
        id = BasicInfo.ID;
        mApplicationList = new ArrayList<>();
        mRecruitmentList = new ArrayList<>();

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
                        if (type.equals("S"))
                            ((ApplicationInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
                        else
                            ((RecruitmentInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
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

        title.setText("我的个人主页");

        final int alphaMaxOffset = SizeConverter.dpToPx(150);
        toolbar.getBackground().setAlpha(0);
        title.setAlpha(0);

        app_bar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            // 设置 toolbar 背景
            if (verticalOffset > -alphaMaxOffset) {
                toolbar.getBackground().setAlpha(255 * -verticalOffset / alphaMaxOffset);
                title.setAlpha(1 * -verticalOffset / alphaMaxOffset);
            } else {
                toolbar.getBackground().setAlpha(255);
                title.setAlpha(1);
            }
        });
        numFocus.setText(String.valueOf(BasicInfo.WATCH_LIST.size()));
        numFocused.setText(String.valueOf(BasicInfo.FAN_LIST.size()));
        signature.setText(BasicInfo.mSignature);

        //显示
//        name.setText(BasicInfo.ACCOUNT);
//        signature.setText(mSignature);
//        numFocus.setText(String.valueOf(mNumFocus));
//        numFocused.setText(String.valueOf(mNumFocused));
        getAvatar(null);
        return root;
    }

    @OnClick(R.id.img_avatar)
    void changeAvatar() {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void getAvatar(String path) {
        if (path == null) {
            GetInfoPictureRequest request;
            if (type.equals("S")) request = new GetInfoPictureRequest(type, null, String.valueOf(id));
            else request = new GetInfoPictureRequest(type, String.valueOf(id), null);
            try {
                MyImageLoader.loadImage(imgAvatar, request.getWholeUrl());
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                System.out.println(path);
                MyImageLoader.loadImage(imgAvatar);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//    setInfo();
        System.out.println("onActivityCreated");

//        getInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
//    setInfo();
        System.out.println("onSTART");
//        ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
//        ((StudyInfoFragment) pagerAdapter.getRegisteredFragment(1)).setInfo();
//        if (type.equals("S"))
//            ((ApplicationInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
//        else
//            ((RecruitmentInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
        //getInfo();
    }

    private void setFragmentInfo() {

    }

//    protected void getInfo() {
//        final int[] count = {0};
//
//        // 获取用户名和类型
//        new GetInfoRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//                count[0]++;
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//
//                    Boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mName = jsonObject.getString("name");
//                        mGender = jsonObject.getString("gender");
//                        mSchool = jsonObject.getString("school");
//                        mDepartment = jsonObject.getString("department");
//                        if (type.equals("S")) {
//                            mMajor = jsonObject.getString("major");
//                            mDegree = jsonObject.getString("degree");
//                        }else {
//                            mTitle = jsonObject.getString("title");
//                        }
//
//                        count[0]++;
//                    } else {
//                        String info = jsonObject.getString("info");
//                        count[0]++;
//                    }
//                } catch (JSONException e) {
//                    count[0]++;
//                }
//            }
//        }, "I", null, null).send();
//
//// 获取个性签名
//        new GetInfoPlusRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//                count[0]++;
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//
//                    Boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mSignature = jsonObject.getString("signature");
//                        mPhone = jsonObject.getString("phone");
//                        mEmail = jsonObject.getString("email");
//                        mHomepage = jsonObject.getString("homepage");
//                        mAddress = jsonObject.getString("address");
//                        mIntroduction = jsonObject.getString("introduction");
//                        mIdNumber = jsonObject.getString("id_number");
//                        if (type.equals("S")) {
//                            mStudentNumber = jsonObject.getString("student_number");
//                            mInterest = jsonObject.getString("research_interest");
//                            mExperience = jsonObject.getString("research_experience");
//                            mUrl = jsonObject.getString("promotional_video_url");
//                        } else {
//                            mTeacherNumber = jsonObject.getString("teacher_number");
//                            mDirection = jsonObject.getString("research_fields");
//                            mResult = jsonObject.getString("research_achievements");
//                            mUrl = jsonObject.getString("promotional_video_url");
//                        }
//
//                        count[0]++;
//                        while (count[0] != 2) {
//
//                        }
//                        getActivity().runOnUiThread(() -> {
//                            signature.setText(mSignature);
//                            ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
//                        });
//
//                        // ((StudyInfoFragment) pagerAdapter.getRegisteredFragment(1)).setInfo();
//                    } else {
//                        String info = jsonObject.getString("info");
//                        count[0]++;
//                    }
//                } catch (JSONException e) {
//                    count[0]++;
//                }
//            }
//        }, "I", null, null).send();
//        getIntentionInfo();
//    }
//
//
//    private void getIntentionInfo() {
//        mApplicationList.clear();
//        mRecruitmentList.clear();
//        if (type.equals("T")) {
//            // 获取招收意向id列表
//            new GetRecruitIntentionRequest(new okhttp3.Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    Log.e("error", e.toString());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    String resStr = response.body().string();
//                    Log.e("response", resStr);
//                    try {
//                        // 解析json，然后进行自己的内部逻辑处理
//                        JSONObject jsonObject = new JSONObject(resStr);
//
//                        Boolean status = jsonObject.getBoolean("status");
//                        if(status){
//                            JSONArray array = jsonObject.getJSONArray("recruitment_id_list");
//                            List<Integer> enrollmentIdList = new ArrayList<>();
//                            for (int i=0;i<array.length();i++){
//                                enrollmentIdList.add(array.getInt(i));
//                            }
//
//                            // 按id获取招收意向
//
//                            if(enrollmentIdList!=null){
//                                for(int i=0;i<enrollmentIdList.size();i++){
//                                    new GetRecruitIntentionDetailRequest(new okhttp3.Callback() {
//                                        @Override
//                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                            Log.e("error", e.toString());
//                                        }
//
//                                        @Override
//                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                            String resStr = response.body().string();
//                                            Log.e("response", resStr);
//                                            try {
//                                                // 解析json，然后进行自己的内部逻辑处理
//                                                JSONObject jsonObject = new JSONObject(resStr);
//
//                                                Boolean status = jsonObject.getBoolean("status");
//                                                if(status){
//                                                    RecruitmentInfo recruitmentInfo = new RecruitmentInfo(
//                                                            jsonObject.getString("research_fields"),
//                                                            jsonObject.getString("recruitment_type"),
//                                                            String.valueOf(jsonObject.getInt("recruitment_number")),
//                                                            jsonObject.getString("intention_state"),
//                                                            jsonObject.getString("introduction")
//                                                    );
//                                                    mRecruitmentList.add(recruitmentInfo);
//
//                                                } else {
//                                                    String info = jsonObject.getString("info");
//                                                }
//                                            } catch (JSONException e) {
//
//                                            }
//                                        }
//                                    },String.valueOf(enrollmentIdList.get(i))).send();
//                                }
//                            }
//
//                        }else{
//                            String info = jsonObject.getString("info");
//                        }
//                    } catch (JSONException e) {
//
//                    }
//                }
//            },String.valueOf(BasicInfo.ID)).send();
//        } else {
//            // 获取申请意向id列表
//            new GetApplyIntentionRequest(new okhttp3.Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    Log.e("error", e.toString());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    String resStr = response.body().string();
//                    Log.e("response", resStr);
//                    try {
//                        // 解析json，然后进行自己的内部逻辑处理
//                        JSONObject jsonObject = new JSONObject(resStr);
//
//                        Boolean status = jsonObject.getBoolean("status");
//                        if(status){
//                            JSONArray array = jsonObject.getJSONArray("application_id_list");
//                            List<Integer> applicationIdList = new ArrayList<>();
//                            for (int i=0;i<array.length();i++){
//                                applicationIdList.add(array.getInt(i));
//                            }
//                            // 按id获取申请意向
//                            if(applicationIdList != null){
//                                for(int i=0;i<applicationIdList.size();i++){
//                                    new GetApplyIntentionDetailRequest(new okhttp3.Callback() {
//                                        @Override
//                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                            Log.e("error", e.toString());
//                                        }
//
//                                        @Override
//                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                            String resStr = response.body().string();
//                                            Log.e("response", resStr);
//                                            try {
//                                                // 解析json，然后进行自己的内部逻辑处理
//                                                JSONObject jsonObject = new JSONObject(resStr);
//                                                Boolean status = jsonObject.getBoolean("status");
//                                                if(status){
//                                                    ApplicationInfo applicationInfo = new ApplicationInfo(
//                                                            jsonObject.getString("research_interests"),
//                                                            jsonObject.getString("intention_state"),
//                                                            jsonObject.getString("introduction")
//                                                    );
//                                                    mApplicationList.add(applicationInfo);
//                                                } else {
//                                                    String info = jsonObject.getString("info");
//                                                }
//                                            } catch (JSONException e) {
//
//                                            }
//                                        }
//                                    }, String.valueOf(applicationIdList.get(i))).send();
//                                }
//                            }
//                        } else {
//                            String info = jsonObject.getString("info");
//                        }
//                    } catch (JSONException e) {
//
//                    }
//                }
//            },String.valueOf(BasicInfo.ID)).send();
//        }
//    }


}
