package com.itgnostic.bank;

import java.util.stream.IntStream;

public interface IpBank {
    void add(int ip); // add new packed ip address
    void addSync(int ip); // add new packed ip address (synchronized)

    long getUniqueCount(); // return count of unique address

    IntStream getUniqueAddress(); // return stream of unique ip-addresses as int-packed ip-addresses

}
