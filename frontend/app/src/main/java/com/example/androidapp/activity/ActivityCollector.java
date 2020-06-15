package com.example.androidapp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private long exitTime = 0;
    private static List<Activity> activities = new ArrayList<>();

    /**
     * 添加 Activity
     */
    static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除 Activity
     */
    static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有 Activity
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
