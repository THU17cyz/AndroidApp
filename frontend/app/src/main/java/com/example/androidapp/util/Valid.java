package com.example.androidapp.util;

public class Valid {
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

}
