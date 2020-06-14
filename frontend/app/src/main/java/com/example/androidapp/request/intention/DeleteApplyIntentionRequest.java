package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class DeleteApplyIntentionRequest extends BasePostRequest {
    public DeleteApplyIntentionRequest(Callback callback, String application_id) {
        // 设置请求URL
        this.to("/api/intention/delete_apply_intention");
        // 设置请求参数
        this.put("application_id", application_id);
        // 设置回调函数
        this.call(callback);
    }
}
