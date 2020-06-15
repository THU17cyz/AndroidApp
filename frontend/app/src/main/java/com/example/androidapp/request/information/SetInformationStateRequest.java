package com.example.androidapp.request.information;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class SetInformationStateRequest extends BasePostRequest {
    public SetInformationStateRequest(Callback callback, String information_id, String information_state) {
        // 设置请求URL
        this.to("/api/information/set_information_state");
        // 设置请求参数
        this.put("information_id", information_id);
        this.put("information_state", information_state);
        // 设置回调函数
        this.call(callback);
    }
}
