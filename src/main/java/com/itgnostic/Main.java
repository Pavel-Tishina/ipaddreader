package com.itgnostic;

import com.itgnostic.bank.IpBank;
import com.itgnostic.bank.UniqueAddress;
import com.itgnostic.bank.UniqueAddressInFile;
import com.itgnostic.cfg.Config;
import com.itgnostic.enums.ReadMode;
import com.itgnostic.enums.TimeParts;
import com.itgnostic.enums.WorkMode;
import com.itgnostic.util.IpUtil;
import com.itgnostic.util.StrUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.itgnostic.enums.TimeParts.*;

public class Main {

    private static IpBank uniqAddr;
    private static Config cfg;
    public static void main(String[] args) throws IOException {
        cfg = new Config(args);

        Set<String> errors = cfg.getErrors();
        if (!errors.isEmpty()) {
            System.out.println("Sorry, but something going wrong:\n" + errors.stream().collect(Collectors.joining("\n\t")));
            return;
        }

        long start = System.currentTimeMillis();

        System.out.println(cfg.info());

        uniqAddr = cfg.getWorkMode() == WorkMode.ALL
                ? new UniqueAddress()
                : new UniqueAddressInFile();

        if (cfg.getReadMode() == ReadMode.LINE)
            readByLines();
        else
            readByBlocks();

        System.out.println("Unique IPs: " + uniqAddr.getUniqueCount());

        Map<TimeParts, Long> elapsedTime = getElapsedTime(start);
        System.out.println("Need time to work: %d:%d:%d".formatted(elapsedTime.get(HOUR), elapsedTime.get(MINUTE), elapsedTime.get(SECOND)));


        if (StrUtil.notNullOrBlank(cfg.getOutFile())) {
            System.out.println("Wait a little bit, I store all result IPs to file...");
            saveResult();
        }
    }

    private static Map<TimeParts, Long> getElapsedTime(long startDate) {
        long elapsedTime = System.currentTimeMillis() - startDate;
        return Map.of(
                HOUR, elapsedTime / 3600000,
                MINUTE, (elapsedTime / (60000)) % 60,
                SECOND, (elapsedTime / 1000) % 60
        );
    }

    private static void readByLines() throws IOException {
        Files.lines(Paths.get(cfg.getIpFile()), StandardCharsets.US_ASCII)
                .filter(ip -> !cfg.isChkIp() || StrUtil.okIP(ip))
                .mapToInt(IpUtil::pack)
                .forEach(uniqAddr::add);
    }

    private static void readByBlocks() {
        byte[] buff = new byte[cfg.getBs()];
        StringBuilder sb = new StringBuilder();
        sb.ensureCapacity(cfg.getBs() + 16);
        int bytesRead;
        try (var inputStream = Files.newInputStream(Path.of(cfg.getIpFile()))) {
            while ((bytesRead = inputStream.read(buff)) != -1) {
                sb.append(new String(buff, 0, bytesRead, StandardCharsets.US_ASCII));

                int lastNL = sb.lastIndexOf("\n");
                int size = sb.length();

                Stream.of(sb.substring(0, lastNL).split("\n"))
                        .flatMapToInt(ip -> {
                            if (StrUtil.notNullOrBlank(ip) && (!cfg.isChkIp() || StrUtil.okIP(ip)))
                                return IntStream.of(IpUtil.pack(ip));

                            return IntStream.empty();
                        })
                        .forEach(uniqAddr::add);

                for (int i = 0; i < size - lastNL; i++)
                    sb.setCharAt(i, sb.charAt(i + lastNL));

                sb.setLength(size - lastNL);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveResult() throws IOException {
        FileOutputStream fos = new FileOutputStream(cfg.getOutFile());

        uniqAddr.getUniqueAddress().boxed().map(IpUtil::unpack).forEach(ip -> {
            try {
                fos.write((ip + "\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        fos.flush();
        fos.close();
    }

}