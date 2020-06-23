package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class DeleteRecruitIntentionRequest extends BasePostRequest {
    public DeleteRecruitIntentionRequest(Callback callback, String recruitment_id) {
        // 设置请求URL
        this.to("/api/intention/delete_recruit_intention");
        // 设置请求参数
        this.put("recruitment_id", recruitment_id);
        // 设置回调函数
        this.call(callback);
    }
}
