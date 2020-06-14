package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetApplyIntentionPictureRequest extends BaseGetRequest {
    public GetApplyIntentionPictureRequest(Callback callback, String application_id) {
        // 设置请求URL
        this.to("/api/intention/get_apply_intention_picture");
        // 设置请求参数
        this.put("application_id", application_id);
        // 设置回调函数
        this.call(callback);
    }
}
