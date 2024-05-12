package com.itgnostic.enums;

import java.nio.file.Path;

public enum IpFileError {
    NOT_SET("Ip-file is not set... add run-argument -ipfile='/path/to/your/file'"),
    NOT_EXIST("Ip-file not exist... no file - no work, sir! bye-bye!"),
    THIS_IS_DIRECTORY("Hey... I need ip-file, not a directory"),
    NO_READ_PERMISSION("Sorry, but I haven't permission for read this file");

    String txt;
    IpFileError(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    public String getTextError() {
        return "[IP-FILE] " + getTxt();
    }

}