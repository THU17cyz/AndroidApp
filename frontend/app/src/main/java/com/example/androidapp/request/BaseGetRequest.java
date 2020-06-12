package com.example.androidapp.request;

import com.example.androidapp.util.Http;

import java.util.HashMap;
import okhttp3.Callback;

public class BaseGetRequest {
    private String url;
    private HashMap<String, String> query;
    private Callback callback;

    public BaseGetRequest() {
        query = new HashMap<>();
    }

    public void send() {
        Http.sendOkHttpGetRequest(url, query, callback);
    }

}
