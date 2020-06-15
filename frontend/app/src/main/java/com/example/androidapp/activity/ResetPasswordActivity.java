package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//import butterknife.BindView;
import com.example.androidapp.R;
import com.example.androidapp.activity.LoginActivity;


public class ResetPasswordActivity extends BaseActivity {

//    @BindView(R.id.resetButton)
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
//        ButterKnife.bind(this);
    }

    //按钮点击事件处理
//    @OnClick(R.id.resetButton)
    public void resetPassword() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //按钮点击事件处理
//    @OnClick(R.id.returnButton)
    public void returnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}