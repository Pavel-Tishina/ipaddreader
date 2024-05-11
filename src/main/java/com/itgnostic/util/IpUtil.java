package com.itgnostic.util;

public class IpUtil {
    public static int pack(String ip) {
        int p = 0;
        int out = 0;
        for (byte i = 0; i < ip.length(); i++) {
            char c = ip.charAt(i);
            if (c == '.') {
                out = (out << Byte.SIZE) | p;
                p = 0;
            }
            else
                p = p * 10 + c - '0';
        }

        return (out << Byte.SIZE) | p;
    }

    public static String unpack(int ip) {
        return ((ip >>> 24) & 255) + "." + ((ip >>> 16) & 255) + "." + ((ip >>> 8) & 255) + "." + (ip & 255);
    }
}
