package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetRecruitIntentionDetailRequest extends BaseGetRequest {
    public GetRecruitIntentionDetailRequest(Callback callback, String recruitment_id) {
        // 设置请求URL
        this.to("/api/intention/get_recruit_intention_detail");
        // 设置请求参数
        this.put("recruitment_id", recruitment_id);
        // 设置回调函数
        this.call(callback);
    }
}
