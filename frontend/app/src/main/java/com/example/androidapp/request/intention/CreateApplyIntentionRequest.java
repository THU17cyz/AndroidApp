package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BasePostRequest;

import java.io.File;

import okhttp3.Callback;

public class CreateApplyIntentionRequest extends BasePostRequest {
    public CreateApplyIntentionRequest(Callback callback, String research_interests,
                                       String introduction, String intention_state, File intention_picture) {
        // 设置请求URL
        this.to("/api/intention/create_apply_intention");
        // 设置请求参数
        this.put("research_interests", research_interests);
        this.put("introduction", introduction);
        this.put("intention_state", intention_state);
        this.load("intention_picture", intention_picture);
        // 设置回调函数
        this.call(callback);
    }
}
