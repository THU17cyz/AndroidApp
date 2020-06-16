package com.example.androidapp.request.recommend;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class RecommendRandomRequest extends BaseGetRequest {
    public RecommendRandomRequest(Callback callback, boolean isTeacher) {
        if (isTeacher) {
            // 设置请求URL
            this.to("/api/recommend/recommend_random_teacher");
        } else {
            this.to("/api/recommend/recommend_random_student");
        }

        // 设置回调函数
        this.call(callback);
    }

}
