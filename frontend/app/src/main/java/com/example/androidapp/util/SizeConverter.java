package com.example.androidapp.util;

import android.content.res.Resources;

public class SizeConverter {
    /**
     * dp转换为px
     */
    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }
}
