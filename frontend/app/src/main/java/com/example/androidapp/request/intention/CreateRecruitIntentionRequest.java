package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BasePostRequest;

import java.io.File;

import okhttp3.Callback;

public class CreateRecruitIntentionRequest extends BasePostRequest {
    public CreateRecruitIntentionRequest(Callback callback, String recruitment_type, String recruitment_number, String research_fields,
                                         String introduction, String intention_state, File intention_picture) {
        // 设置请求URL
        this.to("/api/intention/create_recruit_intention");
        // 设置请求参数
        this.put("recruitment_type", recruitment_type);
        this.put("recruitment_number", recruitment_number);
        this.put("research_fields", research_fields);
        this.put("introduction", introduction);
        this.put("intention_state", intention_state);
        this.load("intention_picture", intention_picture);
        // 设置回调函数
        this.call(callback);
    }
}
