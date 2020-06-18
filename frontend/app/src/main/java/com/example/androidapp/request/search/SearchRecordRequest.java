package com.example.androidapp.request.search;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class SearchRecordRequest extends BaseGetRequest {
    public SearchRecordRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/search/get_search_record");
        // 设置回调函数
        this.call(callback);
    }

    public SearchRecordRequest(Callback callback, String number) {
        // 设置请求URL
        this.to("/api/search/get_search_record");
        // 设置请求参数
        this.put("number", number);
        // 设置回调函数
        this.call(callback);
    }
}
