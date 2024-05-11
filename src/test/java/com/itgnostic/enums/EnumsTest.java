package com.itgnostic.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumsTest {
    @Test
    public void testDetectWorkMode() {
        assertEquals(WorkMode.ALL, WorkMode.detect(null));
        assertEquals(WorkMode.ALL, WorkMode.detect(""));
        assertEquals(WorkMode.ALL, WorkMode.detect("dassdf"));
        assertEquals(WorkMode.ALL, WorkMode.detect("all"));
        assertEquals(WorkMode.ALL, WorkMode.detect("aLl"));
        assertEquals(WorkMode.ALL, WorkMode.detect("ALL"));
        assertEquals(WorkMode.ALL, WorkMode.detect("ALLs"));
        assertEquals(WorkMode.ALL, WorkMode.detect("ALL\t"));
        assertEquals(WorkMode.ALL, WorkMode.detect("once\t"));

        assertEquals(WorkMode.ONCE, WorkMode.detect("once"));
        assertEquals(WorkMode.ONCE, WorkMode.detect("onCe"));
        assertEquals(WorkMode.ONCE, WorkMode.detect("ONCE"));
    }

    @Test
    public void testDetectReadMode() {
        assertEquals(ReadMode.LINE, ReadMode.detect(null));
        assertEquals(ReadMode.LINE, ReadMode.detect(""));
        assertEquals(ReadMode.LINE, ReadMode.detect("dassdf"));
        assertEquals(ReadMode.LINE, ReadMode.detect("line"));
        assertEquals(ReadMode.LINE, ReadMode.detect("lIne"));
        assertEquals(ReadMode.LINE, ReadMode.detect("LINE"));
        assertEquals(ReadMode.LINE, ReadMode.detect("lines"));
        assertEquals(ReadMode.LINE, ReadMode.detect("block\t"));

        assertEquals(ReadMode.BLOCK, ReadMode.detect("block"));
        assertEquals(ReadMode.BLOCK, ReadMode.detect("bloCk"));
        assertEquals(ReadMode.BLOCK, ReadMode.detect("BLOCK"));
    }
}
