package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;
import okhttp3.Callback;

public class LoginRequest extends BasePostRequest {
    public LoginRequest(Callback callback, String type, String account, String password) {
        // 设置请求URL
        this.to("/api/user/login");
        // 设置请求参数
        this.put("type", type);
        this.put("account", account);
        this.put("password", password);
        // 设置回调函数
        this.call(callback);
    }
}
