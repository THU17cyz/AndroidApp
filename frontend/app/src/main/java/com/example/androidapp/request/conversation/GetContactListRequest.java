package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetContactListRequest extends BaseGetRequest {
    public GetContactListRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/conversation/get_conversation");
        // 设置回调函数
        this.call(callback);
    }
}
