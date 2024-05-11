package com.itgnostic.enums;

public enum OutputFileError {
    THIS_IS_DIRECTORY("Here is a directory with same name. Please rename"),
    NO_WRITE_PERMISSION("Sorry, but I haven't permission for write this file"),
    SAME_IP_FILE_NAME("Sorry, but we don't want example of ip-file. Please change name");

    String txt;
    OutputFileError(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    public String getTextError() {
        return "[RESULT FILE] " + getTxt();
    }
}
