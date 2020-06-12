package com.example.androidapp.util;

import android.app.Activity;
import android.view.View;

public class Listener {
    public static void addViewsListener(Activity activity, int viewId, View.OnClickListener onClickListener) {
        View view = activity.findViewById(viewId);
        view.setOnClickListener(onClickListener);
    }

    public static void addViewsListener(Activity activity, int[] viewIds, View.OnClickListener onClickListener) {
        for (int viewId : viewIds) {
            View view = activity.findViewById(viewId);
            view.setOnClickListener(onClickListener);
        }
    }
}
