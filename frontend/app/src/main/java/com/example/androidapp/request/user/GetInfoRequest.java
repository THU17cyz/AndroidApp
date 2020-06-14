package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetInfoRequest extends BaseGetRequest {
    public GetInfoRequest(Callback callback, String type, String teacher_id, String student_id) {
        // 设置请求URL
        this.to("/api/user/get_info");
        // 设置请求参数
        this.put("type", type);
        this.put("teacher_id", teacher_id);
        this.put("student_id", student_id);
        // 设置回调函数
        this.call(callback);
    }
}
