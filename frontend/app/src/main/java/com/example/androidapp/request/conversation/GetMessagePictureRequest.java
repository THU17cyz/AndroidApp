package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetMessagePictureRequest extends BaseGetRequest {
    public GetMessagePictureRequest(Callback callback, String message_id) {
        // 设置请求URL
        this.to("/api/conversation/get_message_picture");
        // 设置请求参数
        this.put("message_id", message_id);
        // 设置回调函数
        this.call(callback);
    }
}
