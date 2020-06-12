package com.example.androidapp.request.user;

<<<<<<< HEAD:frontend/app/src/main/java/com/example/androidapp/request/user/LoginRequest.java
import com.example.androidapp.request.base.BasePostRequest;
import okhttp3.Callback;
=======
import com.example.androidapp.activity.LoginActivity;
>>>>>>> ae9e954e7b767e05075522a4cf9db22bffe9456d:frontend/app/src/main/java/com/example/androidapp/request/LoginRequest.java

public class LoginRequest extends BasePostRequest {
    public LoginRequest(Callback callback, String type, String account, String password) {
        // 设置请求URL
        this.to("/api/user/login");
        // 设置请求参数
        this.put("type", type);
        this.put("account", account);
        this.put("password", password);
        // 设置回调函数
        this.call(callback);

//                callback = new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                context.runOnUiThread(() -> Toast.makeText(context, resStr, Toast.LENGTH_LONG).show());
//                Log.e("response", resStr);
//                try {
//
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//                    boolean status = (Boolean) jsonObject.get("status");
//                    if (status) {
//                        context.runOnUiThread(() -> {
//                            context.jumpToMain();
//                        });
//                    } else {
//                        context.runOnUiThread(() -> Toast.makeText(context, resStr, Toast.LENGTH_LONG).show());
//                    }
//                } catch (JSONException e) {
//                }
//        }
//        };
    }
}
