package com.example.androidapp.util;

import android.net.Uri;

import java.io.File;

public class Uri2File {
    public static File convert(Uri uri) {
        return new File(uri.getPath());
    }

    public static File convert(String path) {
        return new File(path);
    }
}
