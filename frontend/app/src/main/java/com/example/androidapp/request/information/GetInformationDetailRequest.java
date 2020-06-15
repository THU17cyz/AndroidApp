package com.example.androidapp.request.information;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetInformationDetailRequest extends BaseGetRequest {
    public GetInformationDetailRequest(Callback callback, String information_id) {
        // 设置请求URL
        this.to("/api/information/get_information_detail");
        // 设置请求参数
        this.put("information_id", information_id);
        // 设置回调函数
        this.call(callback);
    }
}
