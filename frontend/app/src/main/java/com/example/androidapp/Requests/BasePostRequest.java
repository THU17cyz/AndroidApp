package com.example.androidapp.Requests;

import android.util.Log;
import android.widget.Toast;

import com.example.androidapp.LoginActivity;
import com.example.androidapp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
