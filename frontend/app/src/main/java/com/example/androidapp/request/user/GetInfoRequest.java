package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetInfoRequest extends BaseGetRequest {
    public GetInfoRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/user/login");
        // 设置回调函数
        this.call(callback);
    }
}
