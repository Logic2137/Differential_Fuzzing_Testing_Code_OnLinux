



package compiler.intrinsics.sha;

import java.security.MessageDigest;
import java.util.Arrays;

public class TestDigest {
    private static final int HASH_LEN = 64; 
    private static final int ALIGN = 8;     

    public static void main(String[] args) throws Exception {
        String provider = System.getProperty("provider", "SUN");
        String algorithm = System.getProperty("algorithm", "SHA-1");
        String algorithm2 = System.getProperty("algorithm2", "");
        int msgSize = Integer.getInteger("msgSize", 1024);
        int offset = Integer.getInteger("offset", 0)  % ALIGN;
        int iters = (args.length > 0 ? Integer.valueOf(args[0]) : 100000);
        int warmupIters = (args.length > 1 ? Integer.valueOf(args[1]) : 20000);

        testDigest(provider, algorithm, msgSize, offset, iters, warmupIters);

        if (algorithm2.equals("") == false) {
            testDigest(provider, algorithm2, msgSize, offset, iters, warmupIters);
        }
    }

    public static void testDigest(String provider, String algorithm, int msgSize,
                        int offset, int iters, int warmupIters) throws Exception {
        System.out.println("provider = " + provider);
        System.out.println("algorithm = " + algorithm);
        System.out.println("msgSize = " + msgSize + " bytes");
        System.out.println("offset = " + offset);
        System.out.println("iters = " + iters);

        byte[] expectedHash = new byte[HASH_LEN];
        byte[] hash = new byte[HASH_LEN];
        byte[] data = new byte[msgSize + offset];
        for (int i = 0; i < (msgSize + offset); i++) {
            data[i] = (byte)(i & 0xff);
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm, provider);

            
            digest.reset();
            digest.update(data, offset, msgSize);
            expectedHash = digest.digest();

            
            for (int i = 0; i < warmupIters; i++) {
                digest.reset();
                digest.update(data, offset, msgSize);
                hash = digest.digest();
            }

            
            if (Arrays.equals(hash, expectedHash) == false) {
                System.out.println("TestDigest Error: ");
                showArray(expectedHash, "expectedHash");
                showArray(hash,         "computedHash");
                
                throw new Exception("TestDigest Error");
            } else {
                showArray(hash, "hash");
            }

            
            long start = System.nanoTime();
            for (int i = 0; i < iters; i++) {
                digest.reset();
                digest.update(data, offset, msgSize);
                hash = digest.digest();
            }
            long end = System.nanoTime();
            double total = (double)(end - start)/1e9;         
            double thruput = (double)msgSize*iters/1e6/total; 
            System.out.println("TestDigest runtime = " + total + " seconds");
            System.out.println("TestDigest throughput = " + thruput + " MB/s");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            
            throw new Exception(e);
        }
    }

    static void showArray(byte b[], String name) {
        System.out.format("%s [%d]: ", name, b.length);
        for (int i = 0; i < Math.min(b.length, HASH_LEN); i++) {
            System.out.format("%02x ", b[i] & 0xff);
        }
        System.out.println();
    }
}
