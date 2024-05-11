package com.itgnostic.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IpUtilTest {
    private final String UNPACK_IP_1 = "128.6.7.9";
    private final String UNPACK_IP_2 = "0.0.3.9";
    private final int PACK_IP_1 = -2147088631;
    private final int PACK_IP_2 = 777;

    @Test
    public void packTest() {
        System.out.println("Pack ip %s = %d".formatted(UNPACK_IP_1, IpUtil.pack(UNPACK_IP_1)));
        assertEquals(PACK_IP_1, IpUtil.pack(UNPACK_IP_1));

        System.out.println("Pack ip %s = %d".formatted(UNPACK_IP_2, IpUtil.pack(UNPACK_IP_2)));
        assertEquals(PACK_IP_2, IpUtil.pack(UNPACK_IP_2));
    }

    @Test
    public void unpackTest() {
        System.out.println("Unpack ip %d = %s".formatted(PACK_IP_1, IpUtil.unpack(PACK_IP_1)));
        assertEquals(UNPACK_IP_1, IpUtil.unpack(PACK_IP_1));

        System.out.println("Unpack ip %d = %s".formatted(PACK_IP_2, IpUtil.unpack(PACK_IP_2)));
        assertEquals(UNPACK_IP_2, IpUtil.unpack(PACK_IP_2));


    }

    @Test
    public void crossTest() {
        assertEquals(PACK_IP_1, IpUtil.pack(IpUtil.unpack(PACK_IP_1)));
        assertEquals(PACK_IP_2, IpUtil.pack(IpUtil.unpack(PACK_IP_2)));

        assertEquals(UNPACK_IP_1, IpUtil.unpack(IpUtil.pack(UNPACK_IP_1)));
        assertEquals(UNPACK_IP_2, IpUtil.unpack(IpUtil.pack(UNPACK_IP_2)));
    }

}
