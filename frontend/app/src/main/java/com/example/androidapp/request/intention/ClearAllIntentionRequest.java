package com.example.androidapp.request.intention;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class ClearAllIntentionRequest extends BasePostRequest {
  public ClearAllIntentionRequest(Callback callback) {
    // 设置请求URL
    this.to("/api/intention/clear_all_intention");
    // 设置回调函数
    this.call(callback);
  }
}
