package com.example.androidapp.util;



public class Valid {
    /**
     * 判断是否为空白字符
     * 包括 null、空字符串、全由空白字符构成的字符串
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * 判断是否为有效账号
     * 由字母、数字、下划线组成
     * 不以数字开头
     * 2 ~ 10 个字符
     */
    public static boolean isAccount(String s) {
        return s.matches("^[^0-9][\\w]{1,9}$");
    }

    /**
     * 判断是否为有效密码
     * 由字母、数字、下划线组成
     * 不以数字开头
     * 6 ~ 20 个字符
     */
    public static boolean isPassword(String s) {
        return s.matches("^[\\w]{6,20}$");
    }
}
