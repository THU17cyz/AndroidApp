package com.example.androidapp.request.search;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class SearchHotRecordRequest extends BaseGetRequest {
    public SearchHotRecordRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/search/get_hot_search_record");
        // 设置回调函数
        this.call(callback);
    }

    public SearchHotRecordRequest(Callback callback, String number) {
        // 设置请求URL
        this.to("/api/search/get_hot_search_record");
        // 设置请求参数
        this.put("number", number);
        // 设置回调函数
        this.call(callback);
    }
}
