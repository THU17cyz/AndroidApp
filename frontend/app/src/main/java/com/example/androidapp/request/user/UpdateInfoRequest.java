package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class UpdateInfoRequest extends BasePostRequest {
    public UpdateInfoRequest(Callback callback, String name, String gender, String school, String department, String title, String major, String degree) {
        // 设置请求URL
        this.to("/api/user/update_info");
        // 设置请求参数
        this.put("name", name);
        this.put("gender", gender);
        this.put("school", school);
        this.put("department", department);
        this.put("title", title);
        this.put("major", major);
        this.put("degree", degree);
        // 设置回调函数
        this.call(callback);
    }
}
