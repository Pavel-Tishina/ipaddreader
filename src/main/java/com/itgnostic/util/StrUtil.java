package com.itgnostic.util;

public class StrUtil {
    public static boolean notNullOrBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static boolean okIP(String ip) {
        return notNullOrBlank(ip) && ip.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
    }

    public static int parseBlockSizeValue(String v) {
        int out;
        try {
            out = Integer.parseInt(v.replaceAll("\\D", ""));
        }
        catch (Exception e) {
            return 0;
        }

        v = v.toUpperCase();

        if (v.endsWith("G"))
            out = out << 30;
        else if (v.endsWith("M"))
            out = out << 20;
        else if (v.endsWith("K"))
            out = out << 10;

        return out;
    }
}
