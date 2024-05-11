package com.itgnostic.bank;

import lombok.Synchronized;

import java.util.Objects;
import java.util.stream.IntStream;

public class UniqueAddress implements IpBank {
    private long u = 0L;
    private long[] bankH = new long[33554432];
    private long[] bankL = new long[33554432];

    @Override
    public void add(int ip) {
        boolean isH = ip >= 0;
        if (!isH)
            ip = ~ip;

        int index = ip >> 6;
        //byte indexByte = (byte) (ip & 255 >> 2);
        byte indexByte = (byte) (ip & 63);
        long mask = 1L << indexByte;

        long v = isH ? bankH[index] : bankL[index];

        if (((v & mask) >> indexByte) == 0) {
            if (isH)
                bankH[index] = v | mask;
            else
                bankL[index] = v | mask;
            u++;
        }
    }

    @Override
    @Synchronized
    public void addSync(int ip) {
        add(ip);
    }

    @Override
    public long getUniqueCount() {
        return u;
    }

    @Override
    public IntStream getUniqueAddress() {
        int maxIndH = bankH.length;
        int maxInd = maxIndH + bankL.length;

        return IntStream.range(0, maxInd)
                .map(i -> i < maxIndH ? i : i + maxIndH)
                .flatMap(ind -> {
                    boolean isH = ind < maxIndH;
                    int workIndex = isH ? ind : ~ind;
                    long v = isH ? bankH[ind] : bankL[ind - maxInd];

                    return v != 0
                            ? IntStream.range(0, 63).boxed()
                                .filter(bInd -> ((v & (1L << bInd)) >> bInd) == 1)
                                .map(bInd -> isH ? bInd : 63 - bInd)
                                .mapToInt(bInd -> (workIndex << 6) | bInd)
                            : null;
                })
                .filter(Objects::nonNull);
    }

}
