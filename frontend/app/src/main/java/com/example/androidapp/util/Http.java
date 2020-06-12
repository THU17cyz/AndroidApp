package com.example.androidapp.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/***************
 * [class] Http通用函数
 ***************/
public class Http {
    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static CookieJar cookieJar= new CookieJar() {
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) { cookieStore.put(httpUrl.host(), list); }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
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
    public static void sendOkHttpGetRequest(String url, HashMap<String, String> query, okhttp3.Callback callback)
    {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(server_url + url)).newBuilder();
        for(Map.Entry<String, String> entry : query.entrySet()) { urlBuilder.addQueryParameter(entry.getKey(), entry.getValue()); }
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        Request request = builder.get().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发起 POST 请求（异步）
     * @param url {String} 请求URL 例如 /api/post
     * @param param {HashMap<String, String>} 参数
     * @param callback {Callback} 回调函数
     */
    public static void sendOkHttpPostRequest(String url, HashMap<String, String> param, okhttp3.Callback callback)
    {
        FormBody.Builder builder = new FormBody.Builder();
        for(String key:param.keySet()) {
            builder.add(key, param.get(key));
        }
        RequestBody requestBody=builder.build();
        Request request = new Request.Builder().url(server_url + url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
