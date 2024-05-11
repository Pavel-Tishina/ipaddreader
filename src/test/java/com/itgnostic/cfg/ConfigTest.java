package com.itgnostic.cfg;

import com.itgnostic.enums.IpFileError;
import com.itgnostic.enums.OutputFileError;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    private static final String PATH_ROOT_DIR = System.getProperty("user.dir") + "/src/test";
    private static final String TEST_DATA_DIR = PATH_ROOT_DIR + "/resources";
    private static final String TEST_FILE = TEST_DATA_DIR + "/test_file";
    private static final String TEST_OUT_FILE = TEST_DATA_DIR + "/test_out_file";

    private static final int BS_DEFAULT = 8 << 20;
    private static final int BS_128 = 128 << 20;

    private Config CFG;

    @Before
    public void init() {
        delete(TEST_FILE);
        delete(TEST_OUT_FILE);
    }

    @Test
    public void errorsTest() {
        String[] ip_not_set_1 = new String[1];
        String[] ip_not_set_2 = {"", "", ""};
        String[] ip_not_set_3 = {"-ipfilee='fff'", "", ""};
        String[] ip_not_set_4 = {"-ipfile='cc:/file'", "", ""};

        CFG = new Config(ip_not_set_1);
        assertEquals(Set.of(IpFileError.NOT_SET.getTextError()), CFG.getErrors());

        CFG = new Config(ip_not_set_2);
        assertEquals(Set.of(IpFileError.NOT_SET.getTextError()), CFG.getErrors());

        CFG = new Config(ip_not_set_3);
        assertEquals(Set.of(IpFileError.NOT_SET.getTextError()), CFG.getErrors());

        CFG = new Config(ip_not_set_4);
        assertEquals(Set.of(IpFileError.NOT_SET.getTextError()), CFG.getErrors());

        String[] ip_not_set_5 = {"-ipfile='" + TEST_FILE + "'", "", ""};
        createFile(TEST_FILE);
        CFG = new Config(ip_not_set_5);
        delete(TEST_FILE);
        assertEquals(Set.of(IpFileError.NOT_EXIST.getTextError()), CFG.getErrors());


        createDir(TEST_FILE);
        CFG = new Config(ip_not_set_5);
        assertEquals(Set.of(IpFileError.THIS_IS_DIRECTORY.getTextError()), CFG.getErrors());
        delete(TEST_FILE);

        String[] ip_not_set_6 = {"-ipfile='" + TEST_FILE + "'", "-out='" + TEST_OUT_FILE + "'", ""};
        createFile(TEST_FILE);
        createDir(TEST_OUT_FILE);
        CFG = new Config(ip_not_set_6);
        assertEquals(Set.of(OutputFileError.THIS_IS_DIRECTORY.getTextError()), CFG.getErrors());
        delete(TEST_OUT_FILE);

        createFile(TEST_OUT_FILE);
        blockFile(TEST_OUT_FILE, true);
        CFG = new Config(ip_not_set_6);
        assertEquals(Set.of(OutputFileError.NO_WRITE_PERMISSION.getTextError()), CFG.getErrors());
        blockFile(TEST_OUT_FILE, false);
        delete(TEST_OUT_FILE);

        String[] ip_not_set_7 = {"-ipfile='" + TEST_FILE + "'", "-out='" + TEST_FILE + "'", ""};
        createFile(TEST_FILE);
        //createDir(TEST_OUT_FILE);
        CFG = new Config(ip_not_set_7);
        assertEquals(Set.of(OutputFileError.SAME_IP_FILE_NAME.getTextError()), CFG.getErrors());
        delete(TEST_OUT_FILE);
    }

    @Test
    public void setBsTest() {
        String[] setBS = {"-bs='-1'"};

        CFG = new Config(setBS);
        assertEquals(1, CFG.getBs());

        CFG.setBs("0");
        assertEquals(BS_DEFAULT, CFG.getBs());

        CFG.setBs("dfgh");
        assertEquals(BS_DEFAULT, CFG.getBs());

        CFG.setBs("389047589345689723678956789634785623947856238745698735629783456");
        assertEquals(BS_DEFAULT, CFG.getBs());

        CFG.setBs(Integer.toString(BS_128));
        assertEquals(BS_128, CFG.getBs());
    }

    @Test
    public void infoTest() {
        String[] arg = {"-bs='4M'", "-chk", "-out='" + TEST_OUT_FILE + "'",
                "-ipfile='" + TEST_FILE + "'", "-mode='once'", "-read='block'"};

        String infoString = "Ok, let's start a challenge!\n" +
                "\tIpFile: 'E:\\JavaDev\\ipaddreader/src/test/resources/test_file'\n" +
                "\tWorkMode: ONCE\n" +
                "\tReadMode: BLOCK\n" +
                "\tCheck incoming ip: true\n" +
                "\tBlock size: 4194304\n" +
                "\tSave ip result file: 'E:\\JavaDev\\ipaddreader/src/test/resources/test_out_file'";

        createFile(TEST_FILE);
        createFile(TEST_OUT_FILE);
        CFG = new Config(arg);

        assertEquals(infoString, CFG.info());
    }

    @Test
    public void isOkTest() {
        String[] arg = {"-bs='4M'", "-chk", "-out='" + TEST_OUT_FILE + "'",
                "-ipfile='" + TEST_FILE + "'", "-mode='once'", "-read='block'"};

        createFile(TEST_FILE);
        createFile(TEST_OUT_FILE);
        CFG = new Config(arg);

        assertTrue(CFG.isOk());
    }

    private void createFile(String source) {
        try {
            boolean create = new File(source).createNewFile();
            System.out.println("Creation of file '%s' = %s".formatted(source, create));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDir(String source) {
        System.out.println("Creation of dir '%s' = %s"
                .formatted(source, new File(source).mkdir()));
    }

    private static void delete(String source) {
        System.out.println("Creation of dir '%s' = %s"
                .formatted(source, new File(source).delete()));

    }

    private static void blockFile(String source, boolean block) {

        System.out.println((block ? "Blocking" : "Unblocking") + " of dir '%s' = %s"
                .formatted(source, new File(source).setReadable(!block)));

        new File(source).setWritable(!block);
    }

}
