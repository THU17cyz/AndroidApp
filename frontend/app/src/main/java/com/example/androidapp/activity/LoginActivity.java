package com.example.androidapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.androidapp.R;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.request.follow.GetFanlistRequest;
import com.example.androidapp.request.follow.GetWatchlistRequest;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionDetailRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionRequest;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.request.user.LoginRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.LocalPicx;
import com.example.androidapp.util.LoginCache;
import com.example.androidapp.util.OptionItems;
import com.example.androidapp.util.Valid;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.Slide;

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
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LoginActivity extends BaseActivity {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.login)
    Button loginButton;

    @BindView(R.id.logon)
    Button logonButton;

    @BindView(R.id.introduce)
    Button introduceButton;

    @BindView(R.id.login_type)
    TextView typeSelector;

    @BindView(R.id.login_account)
    FormEditText accountEditText;

    @BindView(R.id.login_password)
    FormEditText passwordEditText;

    IntroductionBuilder introductionBuilder;

    /******************************
     ************ 方法 ************
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 添加验证
        accountEditText.addValidator(new Valid.AccountValidator());
        accountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { accountEditText.testValidity(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        passwordEditText.addValidator(new Valid.PasswordValidator());
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { passwordEditText.testValidity(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        // 软键盘设置
//        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                Toast.makeText(LoginActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                Toast.makeText(LoginActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
//            }
//        });

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        // 已经登录过则跳过登录
        if(LoginCache.hasLoginCache(getApplicationContext())){
            Hint.startActivityLoad(this);
            new LoginRequest(this.handleLogin, LoginCache.type, LoginCache.account, LoginCache.password).send();
        }

        // 引导页设置
        introductionBuilder = new IntroductionBuilder(this);
        introductionBuilder.withSlides(Hint.generateSlides()).introduceMyself();
    }


    private void onJumpToMain() {
        Hint.endActivityLoad(LoginActivity.this);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void beforeJump1() {
        // 获取account，id，type供全局使用
        new GetInfoRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        BasicInfo.ACCOUNT = jsonObject.getString("account");
                        BasicInfo.mName = jsonObject.getString("name");
                        BasicInfo.mGender = jsonObject.getString("gender");
                        BasicInfo.mSchool = jsonObject.getString("school");
                        BasicInfo.mDepartment = jsonObject.getString("department");

                        if(jsonObject.has("student_id")){
                            BasicInfo.ID = jsonObject.getInt("student_id");
                            BasicInfo.TYPE = "S";
                            BasicInfo.PATH = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
                            BasicInfo.IS_TEACHER = false;
                            BasicInfo.mMajor = jsonObject.getString("major");
                            BasicInfo.mDegree = jsonObject.getString("degree");
                        } else {
                            BasicInfo.ID = jsonObject.getInt("teacher_id");
                            BasicInfo.TYPE = "T";
                            BasicInfo.PATH = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();

                            BasicInfo.IS_TEACHER = true;
                            BasicInfo.mTitle = jsonObject.getString("title");
                        }

                        Log.d("basic info",BasicInfo.ACCOUNT);
                        beforeJump2();
                    }else{
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {

                }
            }
        },"I",null,null).send();
    }

    private void beforeJump2() {
        final int count[] = {0};
        new GetFanlistRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error1", e.toString());
                count[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("fanlist_teachers");
                    BasicInfo.FAN_LIST.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), true);
                        BasicInfo.FAN_LIST.add(shortProfile);
                   }
                    jsonArray = (JSONArray) jsonObject.get("fanlist_students");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);

                        BasicInfo.FAN_LIST.add(shortProfile);
                    }
                    count[0]++;
                } catch (JSONException e) {
                    count[0]++;
                    Log.e("error2", e.toString());
                }

            }
        }).send();

        new GetWatchlistRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error1", e.toString());
                count[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    BasicInfo.WATCH_LIST.clear();
                    jsonArray = (JSONArray) jsonObject.get("watchlist_teachers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), true);
                        BasicInfo.addToWatchList(shortProfile);
                    }
                    jsonArray = (JSONArray) jsonObject.get("watchlist_students");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);
                        BasicInfo.addToWatchList(shortProfile);
                    }
                    count[0]++;
                } catch (JSONException e) {
                    Log.e("error2", e.toString());
                    count[0]++;
                }

            }
        }).send();

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
                        BasicInfo.mSignature = jsonObject.getString("signature");
                        BasicInfo.mPhone = jsonObject.getString("phone");
                        BasicInfo.mEmail = jsonObject.getString("email");
                        BasicInfo. mHomepage = jsonObject.getString("homepage");
                        BasicInfo.mAddress = jsonObject.getString("address");
                        BasicInfo.mIntroduction = jsonObject.getString("introduction");
                        BasicInfo.mIdNumber = jsonObject.getString("id_number");
                        if (BasicInfo.TYPE.equals("S")) {
                            BasicInfo.mStudentNumber = jsonObject.getString("student_number");
                            BasicInfo.mInterest = jsonObject.getString("research_interest");
                            BasicInfo.mExperience = jsonObject.getString("research_experience");
                            BasicInfo.mUrl = jsonObject.getString("promotional_video_url");
                        } else {
                            BasicInfo.mTeacherNumber = jsonObject.getString("teacher_number");
                            BasicInfo.mDirection = jsonObject.getString("research_fields");
                            BasicInfo.mResult = jsonObject.getString("research_achievements");
                            BasicInfo.mUrl = jsonObject.getString("promotional_video_url");
                        }

                        count[0]++;
                        while (count[0] != 4) {

                        }
                        onJumpToMain();
                    } else {
                        String info = jsonObject.getString("info");
                        count[0]++;
                    }
                } catch (JSONException e) {
                    count[0]++;
                }
            }
        }, "I", null, null).send();

        BasicInfo.mApplicationList.clear();
        BasicInfo.mRecruitmentList.clear();
        if (BasicInfo.TYPE.equals("T")) {
            // 获取招收意向id列表
            new GetRecruitIntentionRequest(new okhttp3.Callback() {
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
                            JSONArray array = jsonObject.getJSONArray("recruitment_id_list");
                            List<Integer> enrollmentIdList = new ArrayList<>();
                            for (int i=0;i<array.length();i++){
                                enrollmentIdList.add(array.getInt(i));
                            }

                            // 按id获取招收意向

                            if(enrollmentIdList!=null){
                                for(int i=0;i<enrollmentIdList.size();i++){
                                    new GetRecruitIntentionDetailRequest(new okhttp3.Callback() {
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
                                                    RecruitmentInfo recruitmentInfo = new RecruitmentInfo(
                                                            jsonObject.getString("research_fields"),
                                                            jsonObject.getString("recruitment_type"),
                                                            String.valueOf(jsonObject.getInt("recruitment_number")),
                                                            jsonObject.getString("intention_state"),
                                                            jsonObject.getString("introduction")
                                                    );
                                                    BasicInfo.mRecruitmentList.add(recruitmentInfo);

                                                } else {
                                                    String info = jsonObject.getString("info");
                                                }
                                            } catch (JSONException e) {

                                            }
                                        }
                                    },String.valueOf(enrollmentIdList.get(i))).send();
                                }
                            }
                            count[0]++;
                        }else{
                            String info = jsonObject.getString("info");
                            count[0]++;
                        }
                    } catch (JSONException e) {
                        count[0]++;
                    }
                }
            },String.valueOf(BasicInfo.ID)).send();
        } else {
            // 获取申请意向id列表
            new GetApplyIntentionRequest(new okhttp3.Callback() {
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
                            JSONArray array = jsonObject.getJSONArray("application_id_list");
                            List<Integer> applicationIdList = new ArrayList<>();
                            for (int i=0;i<array.length();i++){
                                applicationIdList.add(array.getInt(i));
                            }
                            // 按id获取申请意向
                            if(applicationIdList != null){
                                for(int i=0;i<applicationIdList.size();i++){
                                    new GetApplyIntentionDetailRequest(new okhttp3.Callback() {
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
                                                    ApplicationInfo applicationInfo = new ApplicationInfo(
                                                            jsonObject.getString("research_interests"),
                                                            jsonObject.getString("intention_state"),
                                                            jsonObject.getString("introduction")
                                                    );
                                                    BasicInfo.mApplicationList.add(applicationInfo);
                                                } else {
                                                    String info = jsonObject.getString("info");
                                                }
                                            } catch (JSONException e) {

                                            }
                                        }
                                    }, String.valueOf(applicationIdList.get(i))).send();
                                }
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
            }, String.valueOf(BasicInfo.ID)).send();
        }
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.introduce)
    public void onClickIntroduce() {
        introductionBuilder = new IntroductionBuilder(this);
        introductionBuilder.withSlides(Hint.generateSlides()).introduceMyself();
    }

    @OnClick(R.id.login)
    public void onClickLogin() {
        String type = "";
        type = "T";
        if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(0))){
            type = "T";
        } else if(typeSelector.getText().toString().equals(OptionItems.optionsType.get(1))){
            type = "S";
        }
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        // 后门儿
        if (account.length() == 0) {
            Hint.startActivityLoad(this);
            new LoginRequest(this.handleLogin, "T", "T4", "P12345").send();
            return;
        }

        // 后门儿2
//        if (!Valid.isAccount(account) || !Valid.isPassword(password)) {
//            Hint.showLongCenterToast(this, "格式错误！");
//            return;
//        }
        Hint.startActivityLoad(this);
        new LoginRequest(this.handleLogin, type, account, password).send();
    }

    @OnClick(R.id.logon)
    public void onClickLogon() {
        Intent intent = new Intent(this, LogonActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.login_type)
    public void onClickTypeSelector(){

        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(typeSelector.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String type = OptionItems.optionsType.get(options1);
                typeSelector.setText(type);
            }
        })
                .setTitleText("选择类型")
                .setSubmitText("确定")
                .setCancelText("取消")
                .setContentTextSize(18)
                .setDividerColor(Color.GRAY)
                .setSelectOptions(0)
                .build();
        pvOptions.setPicker(OptionItems.optionsType);
        pvOptions.show();
    }


    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
                try {
                if (response.code() != 200) {
                    LoginCache.removeCache(getApplicationContext());
                    LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {

                        // 保存密码以加入shared...
                        BasicInfo.PASSWORD = passwordEditText.getText().toString();

                        LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, info));
//                        LoginActivity.this.runOnUiThread(LoginActivity.this::onJumpToMain);
                        beforeJump1();
                    } else {
                        LoginCache.removeCache(getApplicationContext());
                        LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, info));
                    }
                }
            } catch (JSONException e) {
                LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };
}
