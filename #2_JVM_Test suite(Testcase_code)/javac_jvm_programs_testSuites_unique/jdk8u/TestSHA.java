import java.security.MessageDigest;
import java.util.Arrays;

public class TestSHA {

    private static final int HASH_LEN = 64;

    private static final int ALIGN = 8;

    public static void main(String[] args) throws Exception {
        String provider = System.getProperty("provider", "SUN");
        String algorithm = System.getProperty("algorithm", "SHA-1");
        String algorithm2 = System.getProperty("algorithm2", "");
        int msgSize = Integer.getInteger("msgSize", 1024);
        int offset = Integer.getInteger("offset", 0) % ALIGN;
        int iters = (args.length > 0 ? Integer.valueOf(args[0]) : 100000);
        int warmupIters = (args.length > 1 ? Integer.valueOf(args[1]) : 20000);
        testSHA(provider, algorithm, msgSize, offset, iters, warmupIters);
        if (algorithm2.equals("") == false) {
            testSHA(provider, algorithm2, msgSize, offset, iters, warmupIters);
        }
    }

    static void testSHA(String provider, String algorithm, int msgSize, int offset, int iters, int warmupIters) throws Exception {
        System.out.println("provider = " + provider);
        System.out.println("algorithm = " + algorithm);
        System.out.println("msgSize = " + msgSize + " bytes");
        System.out.println("offset = " + offset);
        System.out.println("iters = " + iters);
        byte[] expectedHash = new byte[HASH_LEN];
        byte[] hash = new byte[HASH_LEN];
        byte[] data = new byte[msgSize + offset];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < (msgSize + offset); i++) {
            data[i] = (byte) (i & 0xff);
        }
        try {
            MessageDigest sha = MessageDigest.getInstance(algorithm, provider);
            sha.reset();
            sha.update(data, offset, msgSize);
            expectedHash = sha.digest();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < warmupIters; i++) {
                sha.reset();
                sha.update(data, offset, msgSize);
                hash = sha.digest();
            }
            if (Arrays.equals(hash, expectedHash) == false) {
                System.out.println("TestSHA Error: ");
                showArray(expectedHash, "expectedHash");
                showArray(hash, "computedHash");
                throw new Exception("TestSHA Error");
            } else {
                showArray(hash, "hash");
            }
            long start = System.nanoTime();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < iters; i++) {
                sha.reset();
                sha.update(data, offset, msgSize);
                hash = sha.digest();
            }
            long end = System.nanoTime();
            double total = (double) (end - start) / 1e9;
            double thruput = (double) msgSize * iters / 1e6 / total;
            System.out.println("TestSHA runtime = " + total + " seconds");
            System.out.println("TestSHA throughput = " + thruput + " MB/s");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw new Exception(e);
        }
    }

    static void showArray(byte[] b, String name) {
        System.out.format("%s [%d]: ", name, b.length);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < Math.min(b.length, HASH_LEN); i++) {
            System.out.format("%02x ", b[i] & 0xff);
        }
        System.out.println();
    }
}
