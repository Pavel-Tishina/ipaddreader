package com.itgnostic.util;

public class StrUtil {
    public static boolean notNullOrBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static boolean okIP(String ip) {
        return notNullOrBlank(ip) && ip.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
    }
}
