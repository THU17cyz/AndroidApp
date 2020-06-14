package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class LogonRequest extends BasePostRequest {
    public LogonRequest(Callback callback, String type, String account, String password, String name) {
        // 设置请求URL
        this.to("/api/user/logon");
        // 设置请求参数
        this.put("type", type);
        this.put("account", account);
        this.put("password", password);
        this.put("name", name);
        // 设置回调函数
        this.call(callback);
    }
}
