package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.request.user.ChangePasswordRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.LoginCache;
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

//import butterknife.BindView;

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivity {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.old_password)
    FormEditText oldPasswordEditText;

    @BindView(R.id.new_password)
    FormEditText newPasswordEditText;

    @BindView(R.id.repeated_password)
    FormEditText repeatedPasswordEditText;

    @BindView(R.id.reset_password_button)
    Button resetPasswordButton;
    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleChangePassword = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            Hint.endActivityLoad(ResetPasswordActivity.this);
            try {
                if (response.code() != 200) {
                    ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败，请稍后重试！"));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {
                        ResetPasswordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 修改密码之后会要求重新登录，因此清空登录缓存
                                Hint.showLongBottomToast(ResetPasswordActivity.this, info);
                                LoginCache.removeCache(getApplicationContext());
                                onReturnToLogin();
                            }
                        });
                    } else {
                        ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, info));
                    }
                }
            } catch (JSONException e) {
                ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败，请稍后重试！"));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败，请稍后重试！"));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    /******************************
     ************ 方法 ************
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        // 添加验证
        oldPasswordEditText.addValidator(new Valid.PasswordValidator());
        oldPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPasswordEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        newPasswordEditText.addValidator(new Valid.PasswordValidator());
        newPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        repeatedPasswordEditText.addValidator(new Valid.PasswordValidator());
        repeatedPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                repeatedPasswordEditText.testValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick(R.id.returnButton)
    public void onReturnToLogin() {
        finish();
        Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.reset_password_button)
    public void onClickResetButton() {
        String old_password = oldPasswordEditText.getText().toString();
        String new_password = newPasswordEditText.getText().toString();
        String repeated_password = repeatedPasswordEditText.getText().toString();
        if (!Valid.isPassword(old_password) || !Valid.isPassword(new_password) || !Valid.isPassword(repeated_password)) {
            Hint.showLongBottomToast(this, "格式错误！");
            return;
        }
        if (old_password.equals(new_password)) {
            Hint.showLongBottomToast(this, "新旧密码相同！");
        } else if (!new_password.equals(repeated_password)) {
            Hint.showLongBottomToast(this, "重复密码不一致！");
        } else {
            Hint.startActivityLoad(this);
            new ChangePasswordRequest(handleChangePassword, old_password, new_password).send();
        }
    }
}
