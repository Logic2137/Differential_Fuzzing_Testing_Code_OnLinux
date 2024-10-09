import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DirectBBRemaining {

    private static Random random = new SecureRandom();

    private static int testSizes = 40;

    private static int outputFrequency = 5;

    public static void main(String[] args) throws Exception {
        boolean failedOnce = false;
        Exception failedReason = null;
        byte[] keyBytes = new byte[8];
        random.nextBytes(keyBytes);
        SecretKey key = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println("Output test results for every " + outputFrequency + " tests...");
        for (int size = 0; size <= testSizes; size++) {
            boolean output = (size % outputFrequency) == 0;
            if (output) {
                System.out.print("\nTesting buffer size: " + size + ":");
            }
            int outSize = cipher.getOutputSize(size);
            try {
                encrypt(cipher, size, ByteBuffer.allocate(size), ByteBuffer.allocate(outSize), ByteBuffer.allocateDirect(size), ByteBuffer.allocateDirect(outSize), output);
            } catch (Exception e) {
                System.out.print("\n    Failed with size " + size);
                failedOnce = true;
                failedReason = e;
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
        }
        if (failedOnce) {
            throw failedReason;
        }
        System.out.println("\nTest Passed...");
    }

    private enum TestVariant {

        HEAP_HEAP, HEAP_DIRECT, DIRECT_HEAP, DIRECT_DIRECT
    }

    private static void encrypt(Cipher cipher, int size, ByteBuffer heapIn, ByteBuffer heapOut, ByteBuffer directIn, ByteBuffer directOut, boolean output) throws Exception {
        ByteBuffer inBB = null;
        ByteBuffer outBB = null;
        byte[] testdata = new byte[size];
        random.nextBytes(testdata);
        byte[] expected = cipher.doFinal(testdata);
        for (TestVariant tv : TestVariant.values()) {
            if (output) {
                System.out.print(" " + tv);
            }
            switch(tv) {
                case HEAP_HEAP:
                    inBB = heapIn;
                    outBB = heapOut;
                    break;
                case HEAP_DIRECT:
                    inBB = heapIn;
                    outBB = directOut;
                    break;
                case DIRECT_HEAP:
                    inBB = directIn;
                    outBB = heapOut;
                    break;
                case DIRECT_DIRECT:
                    inBB = directIn;
                    outBB = directOut;
                    break;
            }
            inBB.clear();
            outBB.clear();
            inBB.put(testdata);
            inBB.flip();
            cipher.update(inBB, outBB);
            if (inBB.hasRemaining()) {
                throw new Exception("buffer not empty");
            }
            cipher.doFinal(inBB, outBB);
            outBB.flip();
            if (outBB.remaining() != expected.length) {
                throw new Exception("incomplete encryption output, expected " + expected.length + " bytes but was only " + outBB.remaining() + " bytes");
            }
            byte[] encrypted = new byte[outBB.remaining()];
            outBB.get(encrypted);
            if (!Arrays.equals(expected, encrypted)) {
                throw new Exception("bad encryption output");
            }
            if (!Arrays.equals(cipher.doFinal(), cipher.doFinal())) {
                throw new Exception("Internal buffers still held data!");
            }
        }
    }
}
