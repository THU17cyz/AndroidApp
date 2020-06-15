package com.example.androidapp.request.follow;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetFanlistRequest extends BaseGetRequest {
    public GetFanlistRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/follow/get_fanlist");
        // 设置回调函数
        this.call(callback);
    }
}
