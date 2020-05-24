package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.resetButton)
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

    //按钮点击事件处理
    @OnClick(R.id.resetButton)
    public void resetPassword() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
