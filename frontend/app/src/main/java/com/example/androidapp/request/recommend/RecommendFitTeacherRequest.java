package com.example.androidapp.request.recommend;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class RecommendFitTeacherRequest extends BaseGetRequest {
    public RecommendFitTeacherRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/recommend/recommend_fit_teacher");
        // 设置回调函数
        this.call(callback);
    }
}
