package com.example.androidapp.request.user;

import com.example.androidapp.request.base.BasePostRequest;

import java.io.File;

import okhttp3.Callback;

public class UpdateInfoPictureRequest extends BasePostRequest {
    public UpdateInfoPictureRequest(Callback callback, File picture) {
        // 设置请求URL
        this.to("/api/user/update_info_picture");
        // 设置文件参数
        this.load("picture", picture);
        // 设置回调函数
        this.call(callback);
    }
}
