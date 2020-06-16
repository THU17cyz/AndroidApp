package com.example.androidapp.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.androidapp.activity.BaseActivity;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

public class Hint {
    /******************************
     ************ Load ************
     ******************************/
    // Activity 进入加载状态
    public static void startActivityLoad(BaseActivity activity) {
        activity.loadService = LoadSir.getDefault().register(activity, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) { }
        });
    }

    // Activity 解除加载状态
    public static void endActivityLoad(BaseActivity activity) {
        if (activity.loadService != null)
            activity.loadService.showSuccess();
    }

    /******************************
     ************ Toast ***********
     ******************************/
    // 顶端显示短框提示
    public static void showShortTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // 顶端显示长框提示
    public static void showLongTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // 中间显示短框提示
    public static void showShortCenterToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 中间显示长框提示
    public static void showLongCenterToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 底端显示短框提示
    public static void showShortBottomToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    // 底端显示长框提示
    public static void showLongBottomToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


}
