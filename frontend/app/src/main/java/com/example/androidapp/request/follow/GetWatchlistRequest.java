package com.example.androidapp.request.follow;

import com.example.androidapp.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetWatchlistRequest extends BaseGetRequest {
    public GetWatchlistRequest(Callback callback) {
        // 设置请求URL
        this.to("/api/follow/get_watchlist");
        // 设置回调函数
        this.call(callback);
    }
}
