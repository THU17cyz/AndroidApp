package com.example.androidapp.request;

import com.example.androidapp.util.CommonInterface;

import java.util.HashMap;

import okhttp3.Callback;

public class BasePostRequest {

    protected String url;
    protected HashMap<String, String> params;
    protected Callback callback;

    public BasePostRequest() {
        params = new HashMap<>();
    }

    public void sendRequest() {
        CommonInterface.sendOkHttpPostRequest(url, callback, params);
    }

}
