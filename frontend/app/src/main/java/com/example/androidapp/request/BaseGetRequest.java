package com.example.androidapp.request;

import com.example.androidapp.util.Http;

import java.util.HashMap;
import okhttp3.Callback;

public class BaseGetRequest {
    private String url = null;
    private HashMap<String, String> query = new HashMap<>();
    private Callback callback = null;

    public void send() {
        Http.sendOkHttpGetRequest(url, query, callback);
    }
}
