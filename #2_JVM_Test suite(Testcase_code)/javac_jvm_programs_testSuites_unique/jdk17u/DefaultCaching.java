import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;

public class DefaultCaching {

    public static void main(String[] args) throws Exception {
        String hostsFileName = System.getProperty("jdk.net.hosts.file");
        addMappingToHostsFile("theclub", "129.156.220.219", hostsFileName, false);
        test("theclub", "129.156.220.219", true);
        test("luster", "1.16.20.2", false);
        addMappingToHostsFile("luster", "10.5.18.21", hostsFileName, true);
        test("luster", "1.16.20.2", false);
        sleep(10 + 1);
        test("luster", "10.5.18.21", true, 3);
        sleep(5);
        addMappingToHostsFile("theclub", "129.156.220.1", hostsFileName, false);
        addMappingToHostsFile("foo", "10.5.18.22", hostsFileName, true);
        addMappingToHostsFile("luster", "10.5.18.21", hostsFileName, true);
        test("theclub", "129.156.220.219", true, 3);
        test("luster", "10.5.18.21", true, 3);
        test("bar", "10.5.18.22", false, 4);
        test("foo", "10.5.18.22", true, 5);
        sleep(5);
        test("foo", "10.5.18.22", true, 5);
        test("theclub", "129.156.220.1", true, 6);
        sleep(11);
        test("luster", "10.5.18.21", true, 7);
        test("theclub", "129.156.220.1", true, 7);
        sleep(10 + 6);
        test("theclub", "129.156.220.1", true, 8);
        test("luster", "10.5.18.21", true, 8);
        test("foo", "10.5.18.22", true, 9);
    }

    static void test(String host, String address, boolean shouldSucceed, int count) {
        test(host, address, shouldSucceed);
    }

    static void sleep(int seconds) {
        try {
            sleepms(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static long sleepms(long millis) throws InterruptedException {
        long start = System.nanoTime();
        long ms = millis;
        while (ms > 0) {
            assert ms < Long.MAX_VALUE / 1000_000L;
            Thread.sleep(ms);
            long elapsedms = (System.nanoTime() - start) / 1000_000L;
            ms = millis - elapsedms;
        }
        return millis - ms;
    }

    static void test(String host, String address, boolean shouldSucceed) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
            if (!shouldSucceed) {
                throw new RuntimeException(host + ":" + address + ": should fail (got " + addr + ")");
            }
            if (!address.equals(addr.getHostAddress())) {
                throw new RuntimeException(host + "/" + address + ": compare failed (found " + addr + ")");
            }
            System.out.println("test: " + host + "/" + address + " succeeded - got " + addr);
        } catch (UnknownHostException e) {
            if (shouldSucceed) {
                throw new RuntimeException(host + ":" + address + ": should succeed");
            } else {
                System.out.println("test: " + host + "/" + address + " succeeded - got expected " + e);
            }
        }
    }

    private static void addMappingToHostsFile(String host, String addr, String hostsFileName, boolean append) throws Exception {
        String mapping = addr + " " + host;
        try (PrintWriter hfPWriter = new PrintWriter(new BufferedWriter(new FileWriter(hostsFileName, append)))) {
            hfPWriter.println(mapping);
        }
    }
}
