package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetAllMessagesRequest extends BaseGetRequest {
    public GetAllMessagesRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/conversation/get_all_messages");
        // 设置回调函数
        this.call(callback);
    }
}
