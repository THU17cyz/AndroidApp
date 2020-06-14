package com.example.androidapp.request.recommend;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class RecommendHotStudentRequest extends BaseGetRequest {
    public RecommendHotStudentRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/recommend/recommend_hot_student");
        // 设置回调函数
        this.call(callback);
    }
}
