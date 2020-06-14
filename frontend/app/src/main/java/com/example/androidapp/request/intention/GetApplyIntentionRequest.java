package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetApplyIntentionRequest extends BaseGetRequest {
    public GetApplyIntentionRequest(Callback callback, String student_id) {
        // 设置请求URL
        this.to("/api/intention/get_apply_intention");
        // 设置请求参数
        this.put("student_id", student_id);
        // 设置回调函数
        this.call(callback);
    }
}
