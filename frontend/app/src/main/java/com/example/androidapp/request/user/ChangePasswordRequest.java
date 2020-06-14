package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class ChangePasswordRequest extends BasePostRequest {
    public ChangePasswordRequest(Callback callback, String old_password, String new_password) {
        // 设置请求URL
        this.to("/api/user/change_password");
        // 设置文件参数
        this.put("old_password", old_password);
        this.put("new_password", new_password);
        // 设置回调函数
        this.call(callback);
    }
}
