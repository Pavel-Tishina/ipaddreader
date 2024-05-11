package com.itgnostic.bank;

import lombok.Synchronized;

import java.util.Objects;
import java.util.stream.IntStream;

public class UniqueAddressInFile implements IpBank {
    private long u = 0L;
    private long[] bankH = new long[67108864];
    private long[] bankL = new long[67108864];

    @Override
    public void add(int ip) {
        boolean isH = ip >= 0;
        if (!isH)
            ip = ~ip;

        int ind = ip >> 5;
        //byte indB1 = (byte) (ip & 255 >> 3);
        byte indB1 = (byte) (ip & 31);
        byte indB2 = (byte) (indB1 + 32);
        long mask1 = 1L << indB1;
        long mask2 = 1L << indB2;

        long v = isH ? bankH[ind] : bankL[ind];

        if (((v & mask2) >> indB2) == 0) {
            if (((v & mask1) >> indB1) == 0) {
                v = v | mask1;
                u++;
            }
            else {
                v = v | mask2;
                u--;
            }

            if (isH)
                bankH[ind] = v;
            else
                bankL[ind] = v;
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
                            ? IntStream.range(0, 31).boxed()
                                .filter(bInd ->
                                        ((v & (1L << bInd)) >> bInd) == 1
                                          && ((v & (1L << (bInd + 32))) >> (bInd + 32)) == 0
                                )
                                .map(bInd -> isH ? bInd : 31 - bInd)
                                .mapToInt(bInd -> (workIndex << 5) | bInd)
                            : null;
                })
                .filter(Objects::nonNull);

    }



}
