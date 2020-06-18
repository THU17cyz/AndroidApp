package com.example.androidapp.util;

public class StringCutter {
    public static String cutter(String s, int max) {
        if (s.length() >= max) {
            return s.substring(0, max) + "...";
        }
        return s;
    }
}
