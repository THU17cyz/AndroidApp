package com.example.androidapp.request.search;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class SearchApplyIntentionRequest extends BaseGetRequest {
    public SearchApplyIntentionRequest(Callback callback, String key) {
        // 设置请求URL
        this.to("/api/search/search_apply_intention");
        // 设置请求参数
        this.put("key", key);
        // 设置回调函数
        this.call(callback);
    }
}
