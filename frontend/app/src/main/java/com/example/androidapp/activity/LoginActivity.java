package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.Validator;
import com.andreabaccega.widget.FormEditText;
import com.example.androidapp.R;
import com.example.androidapp.request.user.LoginRequest;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.SoftKeyBoardListener;

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
    Button loginBtn;

    @BindView(R.id.logon)
    Button logonBtn;

    @BindView(R.id.forgetPassword)
    Button forgetPasswordButton;

    @BindView(R.id.account)
    EditText account;

    @BindView(R.id.password)
    EditText password;

    /******************************
     ************ 方法 ************
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FormEditText formEditText = findViewById(R.id.account);
        
        formEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Log.d("testdsf", String.valueOf(formEditText.testValidity()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("testdsf", String.valueOf(formEditText.testValidity()));
                formEditText.testValidity();
                formEditText.setError("fuck");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("testdsf", String.valueOf(formEditText.testValidity()));
            }
        });

        formEditText.addValidator(new myValidator(""));


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

    public void onJumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.login)
    public void onClickLogin() {
        Hint.startActivityLoad(this);
        new LoginRequest(this.handleLogin, "T", "T1", "T1").send();

        // loadService.showSuccess();
//    loadService = LoadSir.getDefault().register(this, (Callback.OnReloadListener) v -> {
//    });
        // loadService.showSuccess();

//        new LoginRequest(LoginActivity.this, "T", account.getText().toString(),
//                password.getText().toString()).send();
        // this.onJumpToMain();
    }

    @OnClick(R.id.logon)
    public void onClickLogon() {
        Intent intent = new Intent(this, LogonActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgetPassword)
    public void resetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 回调 ************
     ******************************/
    public okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                Hint.endActivityLoad(LoginActivity.this);
                // 打印返回结果
                Log.e("HttpResponse", response.toString());
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody != null ? responseBody.string() : "";
                Log.e("HttpResponse", responseBodyString);
                JSONObject jsonObject = new JSONObject(responseBodyString);
                boolean status = (Boolean) jsonObject.get("status");
                Log.e("HttpResponse", status ? "√√√√√√√√√√√√√√√√√√√√√√√√√√" : "××××××××××××××××××××××××××");
                LoginActivity.this.onJumpToMain();
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };

    class myValidator extends Validator {


        public myValidator(String _customErrorMessage) {
            super("fuck wrong");
        }

        @Override
        public boolean isValid(EditText et) {
            return false;
        }
    }

}
