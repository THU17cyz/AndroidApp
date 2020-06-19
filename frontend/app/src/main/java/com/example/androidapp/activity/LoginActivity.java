package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.Validator;
import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.request.user.LoginRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.SoftKeyBoardListener;
import com.example.androidapp.util.Valid;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
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

    @BindView(R.id.forgetPassword)
    Button forgetPasswordButton;

    @BindView(R.id.login_type)
    Spinner typeSpinner;

    @BindView(R.id.login_account)
    FormEditText accountEditText;

    @BindView(R.id.login_password)
    FormEditText passwordEditText;

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
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(LoginActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(LoginActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onJumpToMain() {
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
                    if(status){
                        BasicInfo.ACCOUNT = jsonObject.getString("account");
                        if(jsonObject.has("student_id")){
                            BasicInfo.ID = jsonObject.getInt("student_id");
                            BasicInfo.TYPE = "S";
                        }else {
                            BasicInfo.ID = jsonObject.getInt("teacher_id");
                            BasicInfo.TYPE = "T";
                            // BasicInfo.PATH = loadImageCache();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Hint.endActivityLoad(LoginActivity.this);
                        Log.d("basic info",BasicInfo.ACCOUNT);
                    }else{
                        String info = jsonObject.getString("info");
                    }
                } catch (JSONException e) {

                }
            }
        },"I",null,null).send();

    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.login)
    public void onClickLogin() {
        String type = typeSpinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.t_s_type)[0]) ? "T" : "S";
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        // 后门儿
        if (account.length() == 0) {
            Hint.startActivityLoad(this);
            new LoginRequest(this.handleLogin, type, "T2", "T2").send();
        }

        if (!Valid.isAccount(account) || !Valid.isPassword(password)) {
            Hint.showLongBottomToast(this, "格式错误！");
            return;
        }
        Hint.startActivityLoad(this);
        new LoginRequest(this.handleLogin, type, account, password).send();
    }

    @OnClick(R.id.logon)
    public void onClickLogon() {
        Intent intent = new Intent(this, LogonActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgetPassword)
    public void onClickForgetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            Hint.endActivityLoad(LoginActivity.this);
                try {
                if (response.code() != 200) {
                    LoginActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(LoginActivity.this, "登录失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {
                        LoginActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(LoginActivity.this, info));
                        LoginActivity.this.runOnUiThread(LoginActivity.this::onJumpToMain);
                    } else {
                        LoginActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(LoginActivity.this, info));
                    }
                }
            } catch (JSONException e) {
                LoginActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(LoginActivity.this, "登录失败..."));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            LoginActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };
}
