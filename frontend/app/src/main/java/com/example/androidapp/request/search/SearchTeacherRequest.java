package com.example.androidapp.request.search;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class SearchTeacherRequest extends BaseGetRequest {
    public SearchTeacherRequest(Callback callback, String key) {
        // 设置请求URL
        this.to("/api/search/search_teacher");
        // 设置请求参数
        this.put("key", key);
        // 设置回调函数
        this.call(callback);
    }
}
