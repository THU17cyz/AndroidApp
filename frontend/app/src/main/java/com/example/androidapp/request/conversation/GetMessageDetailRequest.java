package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetMessageDetailRequest extends BaseGetRequest {
    public GetMessageDetailRequest(Callback callback, String message_id) {
        // 设置请求URL
        this.to("/api/conversation/get_message_detail");
        // 设置请求参数
        this.put("message_id", message_id);
        // 设置回调函数
        this.call(callback);
    }
}
