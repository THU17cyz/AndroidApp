package com.example.androidapp.request.recommend;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class RecommendHotTeacherRequest extends BaseGetRequest {
    public RecommendHotTeacherRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/recommend/recommend_hot_teacher");
        // 设置回调函数
        this.call(callback);
    }
}
