package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetMessageRequest extends BaseGetRequest {
    public GetMessageRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/conversation/get_message");
        // 设置回调函数
        this.call(callback);
    }
}
