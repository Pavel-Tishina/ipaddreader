package com.itgnostic.util;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class StrUtilTest {
    private final Set<String> STR_FALSE = Set.of("", "  ", "\t \n\n ");
    private final String STR_TRUE = "Okaai";

    private final Set<String> IP_OK = Set.of("0.0.0.0", "255.255.255.255", "127.0.0.1");
    private final Set<String> IP_BAD = Set.of("0.0.00", "000.01.255.255", "255,255.255.255", "127.O.O.1", "I2S.o.o.I", "694.923.1.69947756734756"); // Hmmm... "127.O.O.1" ;-)

    private final int VAL_B = 2;
    private final int VAL_KB = 2 << 10;
    private final int VAL_MB = 2 << 20;
    private final int VAL_GB = 2 << 30;

    @Test
    public void notNullOrBlankTest() {
        for (String bad : STR_FALSE)
            assertFalse(StrUtil.notNullOrBlank(bad));

        assertFalse(StrUtil.notNullOrBlank(null));

        assertTrue(StrUtil.notNullOrBlank(STR_TRUE));
    }

    @Test
    public void okIPTest() {
        for (String bad : IP_BAD)
            assertFalse(StrUtil.okIP(bad));

        for (String bad : STR_FALSE)
            assertFalse(StrUtil.okIP(bad));

        assertFalse(StrUtil.okIP(STR_TRUE));
        assertFalse(StrUtil.okIP(null));


        for (String good : IP_OK)
            assertTrue(StrUtil.okIP(good));
    }

    @Test
    public void parseBlockSizeValueTest() {
        assertEquals(VAL_B, StrUtil.parseBlockSizeValue(Integer.toString(VAL_B)));
        assertEquals(VAL_KB, StrUtil.parseBlockSizeValue(VAL_B + "k"));
        assertEquals(VAL_MB, StrUtil.parseBlockSizeValue(VAL_B + "M"));
        assertEquals(VAL_GB, StrUtil.parseBlockSizeValue(VAL_B + "g"));
        assertEquals(0, StrUtil.parseBlockSizeValue("g"));
        assertEquals(0, StrUtil.parseBlockSizeValue(null));
    }

}
