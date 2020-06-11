package com.example.androidapp.Requests;

import android.util.Log;
import android.widget.Toast;

import com.example.androidapp.LoginActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginRequest extends BasePostRequest {
    public LoginRequest(LoginActivity context, String type, String account, String password) {
        url = "/api/user/login";
        params.put("type", type);
        params.put("account", account);
        params.put("password", password);
        callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                context.runOnUiThread(() -> Toast.makeText(context, resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {

                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = new JSONObject(resStr);
                    boolean status = (Boolean) jsonObject.get("status");
                    if (status) {
                        context.runOnUiThread(() -> {
                            context.jumpToMain();
                        });
                    } else {
                        context.runOnUiThread(() -> Toast.makeText(context, resStr, Toast.LENGTH_LONG).show());
                    }
                } catch (JSONException e) {

                }
            }
        };
    }
}
