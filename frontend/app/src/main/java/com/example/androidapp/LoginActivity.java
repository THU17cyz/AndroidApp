package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_btn)
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    //按钮点击事件处理
    @OnClick(R.id.login_btn)
    public void login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //按钮点击事件处理
    @OnClick(R.id.logonButton)
    public void logon() {
        Intent intent = new Intent(this, LogonActivity.class);
        startActivity(intent);
    }

}
