package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BasePostRequest;

import java.io.File;

import okhttp3.Callback;

public class SendMessageRequest extends BasePostRequest {
    public SendMessageRequest(Callback callback, String object_id, String object_type, String message_type, String message_content_t, File message_content_p) {
        // 设置请求URL
        this.to("/api/conversation/send_message");
        // 设置请求参数
        this.put("object_id", object_id);
        this.put("object_type", object_type);
        this.put("message_type", message_type);
        this.put("message_content", message_content_t);
        this.load("message_content", message_content_p);
        // 设置回调函数
        this.call(callback);
    }
}
