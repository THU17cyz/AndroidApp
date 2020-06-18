package com.example.androidapp.request.search;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class DeleteRecordRequest extends BasePostRequest {
    public DeleteRecordRequest(Callback callback, String key) {
        // 设置请求URL
        this.to("/api/intention/delete_search_record");
        // 设置请求参数
        this.put("key", key);
        // 设置回调函数
        this.call(callback);
    }
}
