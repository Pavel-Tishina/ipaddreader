package com.itgnostic.enums;

import com.itgnostic.util.StrUtil;

public enum ReadMode {
    BLOCK,  // read file by fixed blocks. need more memory, but probably faster
    LINE;   // read file by lines. it's optimal for memory, but probably not to fast [default]

    public static ReadMode detect(String s) {
        if (StrUtil.notNullOrBlank(s)) {
            s = s.toUpperCase();

            for (ReadMode rm : values())
                if (rm.name().equals(s))
                    return rm;
        }

        return LINE;
    }
}
