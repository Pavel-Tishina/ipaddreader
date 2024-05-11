package com.itgnostic.enums;

public enum Arguments {
    READ_MODE("-read='(block|line)'", "$1"),  // read type line or block [default: line]
    WORK_MODE("-mode='(all|once)'", "$1"),    // mode of working [default: ALL]
    OUTPUT_FILE("-out='(.*)'", "$1"),         // if exist - create result file with ip-address
    IP_FILE("-ipfile='(.*)'", "$1"),          // file with ip-address for process [*required*]
    BLOCK_SIZE("-bs='(\\d+)'", "$1"),         // block size for read (convert in MB)
    CHK_IP("-chk", "true");                   // if exist - chk ip address string [default: no check]

    String regExp, val;

    Arguments(String regExp, String val) {
        this.regExp = regExp;
        this.val = val;
    }

    public String getRegExp() {
        return regExp;
    }

    public String getVal() {
        return val;
    }
}
