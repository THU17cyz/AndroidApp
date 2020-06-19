package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

//import butterknife.BindView;
import com.example.androidapp.R;
import com.example.androidapp.activity.LoginActivity;
import com.example.androidapp.request.user.ChangePasswordRequest;
import com.example.androidapp.util.Global;
import com.example.androidapp.util.Hint;

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


public class ResetPasswordActivity extends BaseActivity {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.old_password)
    EditText oldPasswordEditText;

    @BindView(R.id.new_password)
    EditText newPasswordEditText;

    @BindView(R.id.repeated_password)
    EditText repeatedPasswordEditText;

    @BindView(R.id.reset_password_button)
    Button resetPasswordButton;

    /******************************
     ************ 方法 ************
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

    public void onReturnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.reset_password_button)
    public void onClickResetButton() {
        Log.d("reset", ">>>>>>>>>>>>>>>>>>>>>");
        String old_password = oldPasswordEditText.getText().toString();
        String new_password = newPasswordEditText.getText().toString();
        String repeated_password = repeatedPasswordEditText.getText().toString();
        if (old_password.equals(new_password)) {
            Hint.showLongBottomToast(this, "新旧密码相同！");
        }
        else if (!new_password.equals(repeated_password)) {
            Hint.showLongBottomToast(this, "重复密码不一致！");
        }
        else {
            Hint.startActivityLoad(this);
            new ChangePasswordRequest(handleChangePassword, old_password, new_password).send();
        }
    }

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleChangePassword = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            Hint.endActivityLoad(ResetPasswordActivity.this);
            try {
                if (response.code() != 200) {
                    ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    boolean status = (Boolean) jsonObject.get("status");
                    String info = (String) jsonObject.get("info");
                    if (status) {
                        ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, info));
                        ResetPasswordActivity.this.runOnUiThread(ResetPasswordActivity.this::onReturnToLogin);
                    } else {
                        ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, info));
                    }
                }
            } catch (JSONException e) {
                ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败..."));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            ResetPasswordActivity.this.runOnUiThread(() -> Hint.showLongBottomToast(ResetPasswordActivity.this, "修改失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

}
