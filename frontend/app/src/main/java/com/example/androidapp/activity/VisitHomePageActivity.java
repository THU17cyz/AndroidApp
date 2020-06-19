package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.SizeConverter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class VisitHomePageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.visit_homepage_title)
    TextView title;

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

//    @BindView(R.id.btn_return)
//    ImageView btn_return;

    @BindView(R.id.btn_focus)
    Button btn_focus;

    @BindView(R.id.btn_chat)
    Button btn_chat;



    @BindView(R.id.visit_homepage_appbar)
    AppBarLayout app_bar;


    HomepagePagerAdapter pagerAdapter;

    ShortProfile shortProfile;
    WholeProfile wholeProfile;

    int id;
    String type;

    public String mTitle;
    public String mMajor;
    public String mDegree;
//    public String mTeacherNumber;
//    public String mStudentNumber;
//    public String mIdNumber;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        boolean isTeacher = intent.getBooleanExtra("isTeacher", true);

        if (isTeacher) {
            type = "T";
        } else {
            type = "S";
        }

        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        pagerAdapter = new HomepagePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), type, id);
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

        btn_chat.setOnClickListener(v -> {
            Intent intent1 =new Intent(VisitHomePageActivity.this, ChatActivity.class);
            startActivity(intent1);
        });

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

        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });
        getInfo();
    }


    protected void getInfo() {
        String type_ = "I";
        String teacher_id = null;
        String student_id = null;
        if (id == -1) {
            type_ = "I";
            teacher_id = null;
            student_id = null;
        } else if (type.equals("S")) {
            type_ = "S";
            teacher_id = null;
            student_id = String.valueOf(id);
        } else {
            type_ = "T";
            teacher_id = String.valueOf(id);
            student_id = null;
        }
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
                        // getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {
                    count[0]++;
                }
            }
        }, type_, teacher_id, student_id).send();

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
                    if(status){
                        mSignature = jsonObject.getString("signature");
                        mPhone = jsonObject.getString("phone");
                        mEmail = jsonObject.getString("email");
                        mHomepage = jsonObject.getString("homepage");
                        mAddress = jsonObject.getString("address");
                        mIntroduction = jsonObject.getString("introduction");
                        if (type.equals("S")) {
                            mInterest = jsonObject.getString("research_interest");
                            mExperience = jsonObject.getString("research_experience");
                            mUrl = jsonObject.getString("promotional_video_url");
                        } else {
                            mDirection = jsonObject.getString("research_fields");
                            mResult = jsonObject.getString("research_achievements");
                            mUrl = jsonObject.getString("promotional_video_url");
                        }

                        count[0]++;
                        while (count[0] != 2) {

                        }
                        runOnUiThread(() -> {
                            title.setText(mName + "的个人主页");
                            signature.setText(mSignature);
                            ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        });
                    } else {
                        String info = jsonObject.getString("info");
                        count[0]++;
                    }

                } catch (JSONException e) {
                    count[0]++;
                }
            }
        }, type_, teacher_id, student_id).send();
    }
}
