package com.example.androidapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginCache {

    public static String type;
    public static String account;
    public static String password;

    // 用于注册时判断是否有登录缓存
    public static boolean hasLoginCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean hasLogin = sharedPreferences.getBoolean("hasLogin", false);
        if (!hasLogin) {
            return false;
        } else {
            type = sharedPreferences.getString("type", "");
            account = sharedPreferences.getString("account", "");
            password = sharedPreferences.getString("password", "");
            return true;
        }
    }

    // 用于登录时保存登录信息
    public static void saveCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasLogin", true);
        editor.putString("type", BasicInfo.TYPE);
        editor.putString("account", BasicInfo.ACCOUNT);
        editor.putString("password", BasicInfo.PASSWORD);
        editor.commit();
    }

    // 用于登出时删除登录信息 || 登录异常时清除缓存
    public static void removeCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasLogin", false);
        editor.putString("type", "");
        editor.putString("account", "");
        editor.putString("password", "");
        editor.commit();
    }

    // 用于修改密码后更新密码
    public static void updateCachePassword(Context context, String newPassword) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", newPassword);
        editor.commit();
    }


}
