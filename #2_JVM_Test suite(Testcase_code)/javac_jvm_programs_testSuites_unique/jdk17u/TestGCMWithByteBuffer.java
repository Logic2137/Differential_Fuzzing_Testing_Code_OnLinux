import java.nio.ByteBuffer;
import java.security.*;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.AEADBadTagException;
import javax.crypto.spec.*;

public class TestGCMWithByteBuffer {

    private static Random random = new SecureRandom();

    private static int dataSize = 4096;

    private static int multiples = 3;

    public static void main(String[] args) throws Exception {
        Provider[] provs = Security.getProviders();
        for (Provider p : provs) {
            try {
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", p);
                test(cipher);
            } catch (NoSuchAlgorithmException nsae) {
                continue;
            }
        }
    }

    private static void test(Cipher cipher) throws Exception {
        System.out.println("Testing " + cipher.getProvider());
        boolean failedOnce = false;
        Exception failedReason = null;
        int tagLen = 96;
        byte[] keyBytes = new byte[16];
        random.nextBytes(keyBytes);
        byte[] dataChunk = new byte[dataSize];
        random.nextBytes(dataChunk);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec s = new GCMParameterSpec(tagLen, keyBytes);
        for (int t = 1; t <= multiples; t++) {
            int size = t * dataSize;
            System.out.println("\nTesting data size: " + size);
            try {
                decrypt(cipher, key, s, dataChunk, t, ByteBuffer.allocate(dataSize), ByteBuffer.allocate(size), ByteBuffer.allocateDirect(dataSize), ByteBuffer.allocateDirect(size));
            } catch (Exception e) {
                System.out.println("\tFailed with data size " + size);
                failedOnce = true;
                failedReason = e;
            }
        }
        if (failedOnce) {
            throw failedReason;
        }
        System.out.println("\n=> Passed...");
    }

    private enum TestVariant {

        HEAP_HEAP, HEAP_DIRECT, DIRECT_HEAP, DIRECT_DIRECT
    }

    private static void decrypt(Cipher cipher, SecretKey key, GCMParameterSpec s, byte[] dataChunk, int multiples, ByteBuffer heapIn, ByteBuffer heapOut, ByteBuffer directIn, ByteBuffer directOut) throws Exception {
        ByteBuffer inBB = null;
        ByteBuffer outBB = null;
        for (TestVariant tv : TestVariant.values()) {
            System.out.println(" " + tv);
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
            inBB.put(dataChunk);
            outBB.clear();
            try {
                cipher.init(Cipher.DECRYPT_MODE, key, s);
                for (int i = 0; i < multiples; i++) {
                    inBB.flip();
                    cipher.update(inBB, outBB);
                    if (inBB.hasRemaining()) {
                        throw new Exception("buffer not empty");
                    }
                }
                cipher.doFinal(inBB, outBB);
                throw new RuntimeException("Error: doFinal completed without exception");
            } catch (AEADBadTagException ex) {
                System.out.println("Expected AEADBadTagException thrown");
                continue;
            }
        }
    }
}
