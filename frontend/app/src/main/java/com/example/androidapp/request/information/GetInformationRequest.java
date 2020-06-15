package com.example.androidapp.request.information;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetInformationRequest extends BaseGetRequest {
    public GetInformationRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/information/get_information");
        // 设置回调函数
        this.call(callback);
    }
}
