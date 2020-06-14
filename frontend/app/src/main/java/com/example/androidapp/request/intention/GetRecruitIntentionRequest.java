package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetRecruitIntentionRequest extends BaseGetRequest {
    public GetRecruitIntentionRequest(Callback callback, String teacher_id) {
        // 设置请求URL
        this.to("/api/intention/get_recruit_intention");
        // 设置请求参数
        this.put("teacher_id", teacher_id);
        // 设置回调函数
        this.call(callback);
    }
}
