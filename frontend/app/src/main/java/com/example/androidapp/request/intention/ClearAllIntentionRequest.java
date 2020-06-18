package com.example.androidapp.request.intention;

import android.util.Log;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class ClearAllIntentionRequest extends BasePostRequest {
    public ClearAllIntentionRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/intention/clear_all_intention");
        // 设置请求参数
        this.put("whatever", "whatever");
        // 设置回调函数
        this.call(callback);
    }
}
