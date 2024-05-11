package com.itgnostic.enums;

import com.itgnostic.util.StrUtil;

public enum WorkMode {
    ALL,    // calculate count of all uniq ip-address, that file contains [default]
    ONCE;   // calculate count of all ip-address, that meet in file once

    public static WorkMode detect(String s) {
        if (StrUtil.notNullOrBlank(s)) {
            s = s.toUpperCase();

            for (WorkMode wm : values())
                if (wm.name().equals(s))
                    return wm;
        }

        return ALL;
    }
}
