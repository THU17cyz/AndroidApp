package com.example.androidapp.request;

import com.example.androidapp.util.Http;

import java.io.File;
import java.util.HashMap;
import okhttp3.Callback;

public class BasePostRequest {
    private String url = null;
    private HashMap<String, String> param = new HashMap<>();
    private String fileKey = null;
    private File fileObject = null;
    private Callback callback;

    public BasePostRequest() {
        param = new HashMap<>();
    }

    public void send() {
        Http.sendOkHttpPostRequest(url, param, fileKey, fileObject, callback);
    }
}
