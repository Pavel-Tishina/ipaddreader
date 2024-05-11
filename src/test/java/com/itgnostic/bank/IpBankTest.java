package com.itgnostic.bank;

import com.itgnostic.util.IpUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IpBankTest {
    private static final IpBank ALL_U = new UniqueAddress();
    private static final IpBank ONCE_U = new UniqueAddressInFile();

    private static final IpBank ALL_U_S = new UniqueAddress();
    private static final IpBank ONCE_U_S = new UniqueAddressInFile();

    private final List<String> STR_IP = Arrays.asList(
            //  IP              N   How much add?   Uniq for ALL    Uniq for ONCE
            "128.0.10.10",  //  0   add 3-times         yes             no
            "128.0.10.15",  //  1                       yes             yes
            "0.0.1.9",      //  2   add twice           yes             no
            "0.0.1.99",     //  3                       yes             yes
            "127.0.0.1",    //  4                       yes             yes
            "255.0.255.1",  //  5   add twice           yes             no
            "255.0.255.10", //  6   add 3-times         yes             no
            "255.0.255.100" //  7                       yes             yes
            //                                          8               4
    );

    @Test
    public void test_stage_1_ALL_U_add_ip() {
        // add ip, they all unique
        STR_IP.forEach(ip -> ALL_U.add(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ALL_U.getUniqueCount());
    }

    @Test
    public void test_stage_1_ONCE_U_add_ip() {
        // add ip, they all unique
        STR_IP.forEach(ip -> ONCE_U.add(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ONCE_U.getUniqueCount());
    }

    @Test
    public void test_stage_1_ALL_U_SYNC_add_ip() {
        // add ip, they all unique
        STR_IP.parallelStream().forEach(ip -> ALL_U_S.addSync(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ALL_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_1_ONCE_U_SYNC_add_ip() {
        // add ip, they all unique
        STR_IP.parallelStream().forEach(ip -> ONCE_U_S.addSync(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ONCE_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_2_ALL_U_chk_uniq_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_2_ONCE_U_chk_uniq_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ONCE_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_2_ALL_U_SYNC_chk_uniq_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_2_ONCE_U_SYNC_chk_uniq_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ONCE_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_3_ALL_U_add_duplicates_ip() {
        // add some duplicates of ip (see comments above)
        ALL_U.add(IpUtil.pack(STR_IP.get(0)));
        ALL_U.add(IpUtil.pack(STR_IP.get(2)));
        ALL_U.add(IpUtil.pack(STR_IP.get(6)));
        ALL_U.add(IpUtil.pack(STR_IP.get(0)));
        ALL_U.add(IpUtil.pack(STR_IP.get(6)));
        ALL_U.add(IpUtil.pack(STR_IP.get(5)));
        ALL_U.add(IpUtil.pack(STR_IP.get(6)));
        ALL_U.add(IpUtil.pack(STR_IP.get(0)));

        assertEquals(STR_IP.size(), ALL_U.getUniqueCount());
    }

    @Test
    public void test_stage_3_ONCE_U_add_duplicates_ip() {
        // add some duplicates of ip (see comments above)
        ONCE_U.add(IpUtil.pack(STR_IP.get(0)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(2)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(6)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(0)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(6)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(5)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(6)));
        ONCE_U.add(IpUtil.pack(STR_IP.get(0)));

        assertEquals(4, ONCE_U.getUniqueCount());
    }

    @Test
    public void test_stage_3_ALL_U_SYNC_add_duplicates_ip() {
        // add some duplicates of ip (see comments above)
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(0)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(2)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(0)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(5)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ALL_U_S.addSync(IpUtil.pack(STR_IP.get(0)));

        assertEquals(STR_IP.size(), ALL_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_3_ONCE_U_SYNC_add_duplicates_ip() {
        // add some duplicates of ip (see comments above)
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(0)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(2)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(0)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(5)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(6)));
        ONCE_U_S.addSync(IpUtil.pack(STR_IP.get(0)));

        assertEquals(4, ONCE_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_4_ALL_U_add_duplicates_ip_get_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_4_ONCE_U_add_duplicates_ip_get_ips() {
        assertEquals(Set.of(STR_IP.get(1), STR_IP.get(3), STR_IP.get(4), STR_IP.get(7)),
                ONCE_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_4_ALL_U_SYNC_add_duplicates_ip_get_ips() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_4_ONCE_U_SYNC_add_duplicates_ip_get_ips() {
        assertEquals(Set.of(STR_IP.get(1), STR_IP.get(3), STR_IP.get(4), STR_IP.get(7)),
                ONCE_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_5_ALL_U_add_all_ip_twice() {
        // add all ip twice
        STR_IP.forEach(ip -> ALL_U.add(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ALL_U.getUniqueCount());
    }

    @Test
    public void test_stage_5_ONCE_U_add_all_ip_twice() {
        // add all ip twice
        STR_IP.forEach(ip -> ONCE_U.add(IpUtil.pack(ip)));

        assertEquals(0, ONCE_U.getUniqueCount());
    }

    @Test
    public void test_stage_5_ALL_U_SUNC_add_all_ip_twice() {
        // add all ip twice
        STR_IP.parallelStream().forEach(ip -> ALL_U_S.addSync(IpUtil.pack(ip)));

        assertEquals(STR_IP.size(), ALL_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_5_ONCE_U_SUNC_add_all_ip_twice() {
        // add all ip twice
        STR_IP.parallelStream().forEach(ip -> ONCE_U_S.addSync(IpUtil.pack(ip)));

        assertEquals(0, ONCE_U_S.getUniqueCount());
    }

    @Test
    public void test_stage_6_ALL_U_after_all_twice() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_6_ONCE_U_after_all_twice() {
        assertEquals(Collections.emptySet(),
                ONCE_U.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_6_ALL_U_SYNC_after_all_twice() {
        assertEquals(new HashSet<>(STR_IP),
                ALL_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }

    @Test
    public void test_stage_6_ONCE_U_SYNC_after_all_twice() {
        assertEquals(Collections.emptySet(),
                ONCE_U_S.getUniqueAddress().boxed().map(IpUtil::unpack).collect(Collectors.toSet()));
    }
}
