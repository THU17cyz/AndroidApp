package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class UserAuthRequest extends BasePostRequest {
    public UserAuthRequest(Callback callback, String type, String teacher_number, String student_number, String id_number) {
        // 设置请求URL
        this.to("/api/user/user_auth");
        // 设置文件参数
        this.put("type", type);
        this.put("teacher_number", teacher_number);
        this.put("student_number", student_number);
        this.put("id_number", id_number);
        // 设置回调函数
        this.call(callback);
    }
}
