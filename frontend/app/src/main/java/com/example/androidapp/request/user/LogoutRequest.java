package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;
import okhttp3.Callback;

public class LogoutRequest extends BasePostRequest {
    public LogoutRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/user/logout");
        // 设置回调函数
        this.call(callback);
    }
}
