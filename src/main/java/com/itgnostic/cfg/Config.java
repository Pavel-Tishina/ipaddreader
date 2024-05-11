package com.itgnostic.cfg;

import com.itgnostic.enums.IpFileError;
import com.itgnostic.enums.OutputFileError;
import com.itgnostic.enums.Arguments;
import com.itgnostic.enums.ReadMode;
import com.itgnostic.enums.WorkMode;
import com.itgnostic.util.StrUtil;
import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Config {
    WorkMode workMode = WorkMode.ALL;
    ReadMode readMode = ReadMode.LINE;
    String ipFile;
    String outFile;
    boolean chkIp = false;
    int bs = 8 << 20;

    public Config(String[] arguments) {
        for (String argV : arguments)
            if (StrUtil.notNullOrBlank(argV))
                for (Arguments a : Arguments.values())
                    if (argV.matches(a.getRegExp())) {
                        setValue(a, argV.replaceFirst(a.getRegExp(), a.getVal()));
                        break;
                    }
    }

    public void setWorkMode(String v) {
        this.workMode = WorkMode.detect(v);
    }

    public void setReadMode(String v) {
        this.readMode = ReadMode.detect(v);
    }

    public void setOutFile(String v) {
        this.outFile = StrUtil.notNullOrBlank(v) && new File(v).exists() ? v : "";
    }

    public void setIpFile(String v) {
        this.ipFile = StrUtil.notNullOrBlank(v) && new File(v).exists() ? v : "";
    }


    public void setBs(String v) {
        this.bs = StrUtil.parseBlockSizeValue(v);

        if (this.bs < 1 || this.bs > (Runtime.getRuntime().freeMemory() << 3))
            this.bs = 8 << 20;
    }

    public void setChkIp(String v) {
        this.chkIp = Boolean.parseBoolean(v);
    }

    public boolean isOk() {
        return getErrors().isEmpty();
    }

    public Set<String> getErrors() {
        Set<String> errors = new HashSet<>();

        if (!StrUtil.notNullOrBlank(getIpFile()))
            errors.add(IpFileError.NOT_SET.getTextError());
        else if (Files.notExists(Path.of(getIpFile())))
            errors.add(IpFileError.NOT_EXIST.getTextError());
        else if (Files.isDirectory(Path.of(getIpFile())))
            errors.add(IpFileError.THIS_IS_DIRECTORY.getTextError());
        else if (!Files.isReadable(Path.of(getIpFile())))
            errors.add(IpFileError.NO_READ_PERMISSION.getTextError());

        if (StrUtil.notNullOrBlank(getOutFile())) {
            if (Files.isDirectory(Path.of(getOutFile())))
                errors.add(OutputFileError.THIS_IS_DIRECTORY.getTextError());
            else if (!Files.isWritable(Path.of(getOutFile())))
                errors.add(OutputFileError.NO_WRITE_PERMISSION.getTextError());
            else if ((isWindows() && getIpFile().equalsIgnoreCase(getOutFile()))
                    || getIpFile().equals(getOutFile()))
                errors.add(OutputFileError.SAME_IP_FILE_NAME.getTextError());
        }

        return errors;
    }

    public String info() {
        return "Ok, let's start a challenge!\n\tIpFile: '%s'\n\tWorkMode: %s\n\tReadMode: %s\n\tCheck incoming ip: %s"
                .formatted(getIpFile(), getWorkMode(), getReadMode(), isChkIp())
                + (getReadMode() == ReadMode.BLOCK ? "\n\tBlock size: " + getBs() : "")
                + (StrUtil.notNullOrBlank(getOutFile()) ? "\n\tSave ip result file: '%s'".formatted(getOutFile()) : "");

    }

    private void setValue(Arguments a, String v) {
        switch (a) {
            case OUTPUT_FILE -> setOutFile(v);
            case WORK_MODE -> setWorkMode(v);
            case READ_MODE -> setReadMode(v);
            case IP_FILE -> setIpFile(v);
            case BLOCK_SIZE -> setBs(v);
            case CHK_IP -> setChkIp(v);
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").matches("(?i)windows.*");
    }

}
