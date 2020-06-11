package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.Activity.MainActivity;
import com.example.androidapp.Requests.LoginRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.ProgressCallback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_btn)
    Button loginBtn;

    @BindView(R.id.logon)
    Button logonBtn;

    @BindView(R.id.forgetPassword)
    Button forgetPasswordButton;

    @BindView(R.id.account)
    EditText account;

    @BindView(R.id.password)
    EditText password;

    LoadService loadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ProgressCallback loadingCallback = new ProgressCallback.Builder()
                .setTitle("Loading")
                .build();
        LoadSir.beginBuilder()
                .addCallback(loadingCallback)
                .setDefaultCallback(ProgressCallback.class)//设置默认状态页
                .commit();


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

    //按钮点击事件处理
    @OnClick(R.id.login_btn)
    public void login() {
        loadService = LoadSir.getDefault().register(this, (Callback.OnReloadListener) v -> {

        });
        new LoginRequest(LoginActivity.this, "T", account.getText().toString(),
                password.getText().toString()).sendRequest();
        jumpToMain();

    }

    //按钮点击事件处理
    @OnClick(R.id.logon)
    public void logon() {
        Intent intent = new Intent(this, LogonActivity.class);
        startActivity(intent);
    }

    //按钮点击事件处理
    @OnClick(R.id.forgetPassword)
    public void resetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void jumpToMain() {
        loadService.showSuccess();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
