package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetApplyIntentionDetailRequest extends BaseGetRequest {
    public GetApplyIntentionDetailRequest(Callback callback, String application_id) {
        // 设置请求URL
        this.to("/api/intention/get_apply_intention_detail");
        // 设置请求参数
        this.put("application_id", application_id);
        // 设置回调函数
        this.call(callback);
    }
}
