package com.example.androidapp.util;

import android.util.Log;

import com.example.androidapp.request.user.GetInfoRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/***************
 * [class] Http通用函数
 ***************/
public class Http {
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static CookieJar cookieJar= new CookieJar() {
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) { cookieStore.put(httpUrl.host(), list); }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<>();
        }
    };
    private static final String server_url = Global.SERVER_URL;                                             // 服务端 URL
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();     // 客户端 实例

    /**
     * 发送 GET 请求（异步）
     * @param url {String} 请求URL 例如 /api/get
     * @param query {HashMap<String, String>} 请求参数
     * @param callback {Callback} 回调函数
     */
    public static void sendOkHttpGetRequest(String url, HashMap<String, String> query, okhttp3.Callback callback) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(server_url + url)).newBuilder();
        for(Map.Entry<String, String> entry : query.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        Request request = builder.get().build();
        if (Global.HTTP_DEBUG_MODE)
            Log.e("HttpRequest", request.toString());
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发起 POST 请求（异步）
     * @param url {String} 请求URL 例如 /api/post
     * @param param {HashMap<String, String>} 参数
     * @param fileKey {String} 文件关键字
     * @param fileObject {File} 文件对象
     * @param callback {Callback} 回调函数
     */
    public static void sendOkHttpPostRequest(String url, HashMap<String, String> param, String fileKey, File fileObject, okhttp3.Callback callback) {
        MultipartBody.Builder mulBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(Map.Entry<String, String> entry : param.entrySet()) {
            mulBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        if (fileKey != null && fileObject != null) {
            mulBuilder.addFormDataPart(fileKey, fileObject.getName(), RequestBody.create(fileObject, MediaType.parse("image/jpeg")));
        }
        Request request = new Request.Builder().url(server_url + url).post(mulBuilder.build()).build();
        if (Global.HTTP_DEBUG_MODE)
            Log.e("HttpRequest", request.toString());
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static Callback callbackExample = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                // 打印返回结果
                String res = Objects.requireNonNull(response.body()).string();
                Log.e("HttpResponse", res);
                JSONObject jsonObject = new JSONObject(res);
                boolean status = (Boolean) jsonObject.get("status");
                if (status)
                    Log.e("HttpResponse", "√√√√√√√√√√√√√√√√√√√√√√√√√√");
                else
                    Log.e("HttpResponse", "××××××××××××××××××××××××××");
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };

    public static Callback callbackExample_2 = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                // 打印返回结果
                String res = Objects.requireNonNull(response.body()).string();
                Log.e("HttpResponse", res);
                JSONObject jsonObject = new JSONObject(res);
                boolean status = (Boolean) jsonObject.get("status");
                if (status)
                    Log.e("HttpResponse", "√√√√√√√√√√√√√√√√√√√√√√√√√√");
                else
                    Log.e("HttpResponse", "××××××××××××××××××××××××××");
                new GetInfoRequest(callbackExample).send();
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };
}
