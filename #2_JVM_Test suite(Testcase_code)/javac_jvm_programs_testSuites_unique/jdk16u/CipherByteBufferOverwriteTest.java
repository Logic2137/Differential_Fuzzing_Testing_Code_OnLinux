



import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CipherByteBufferOverwriteTest {

    private static final boolean DEBUG = false;

    private static String transformation;

    
    
    private static final int PLAINTEXT_SIZE = 8192;
    
    private static final int CIPHERTEXT_BUFFER_SIZE = PLAINTEXT_SIZE + 32;

    private static final SecretKey KEY = new SecretKeySpec(new byte[16], "AES");
    private static AlgorithmParameterSpec params;

    private static ByteBuffer inBuf;
    private static ByteBuffer outBuf;

    private enum BufferType {
        ALLOCATE, DIRECT, WRAP;
    }

    public static void main(String[] args) throws Exception {

        transformation = args[0];
        int offset = Integer.parseInt(args[1]);
        boolean useRO = Boolean.parseBoolean(args[2]);

        if (transformation.equalsIgnoreCase("AES/GCM/NoPadding")) {
            params = new GCMParameterSpec(16 * 8, new byte[16]);
        } else {
            params = new IvParameterSpec(new byte[16]);
        }
        
        
        byte[] expectedPT = new byte[PLAINTEXT_SIZE];
        byte[] buf = new byte[offset + CIPHERTEXT_BUFFER_SIZE];
        System.arraycopy(expectedPT, 0, buf, 0, PLAINTEXT_SIZE);

        
        Cipher c = Cipher.getInstance(transformation);
        c.init(Cipher.ENCRYPT_MODE, KEY, params);
        byte[] expectedCT = c.doFinal(expectedPT);

        
        prepareBuffers(BufferType.ALLOCATE, useRO, buf.length,
                buf, 0, PLAINTEXT_SIZE, offset);

        runTest(offset, expectedPT, expectedCT);
        System.out.println("\tALLOCATE: passed");

        
        prepareBuffers(BufferType.DIRECT, useRO, buf.length,
                buf, 0, PLAINTEXT_SIZE, offset);

        runTest(offset, expectedPT, expectedCT);
        System.out.println("\tDIRECT: passed");

        
        prepareBuffers(BufferType.WRAP, useRO, buf.length,
                buf, 0, PLAINTEXT_SIZE, offset);

        runTest(offset, expectedPT, expectedCT);
        System.out.println("\tWRAP: passed");

        System.out.println("All Tests Passed");
    }

    private static void prepareBuffers(BufferType type,
            boolean useRO, int bufSz, byte[] in, int inOfs, int inLen,
            int outOfs) {
        switch (type) {
            case ALLOCATE:
                outBuf = ByteBuffer.allocate(bufSz);
                inBuf = outBuf.slice();
                inBuf.put(in, inOfs, inLen);
                inBuf.rewind();
                inBuf.limit(inLen);
                outBuf.position(outOfs);
                break;
            case DIRECT:
                outBuf = ByteBuffer.allocateDirect(bufSz);
                inBuf = outBuf.slice();
                inBuf.put(in, inOfs, inLen);
                inBuf.rewind();
                inBuf.limit(inLen);
                outBuf.position(outOfs);
                break;
            case WRAP:
                if (in.length < bufSz) {
                    throw new RuntimeException("ERROR: Input buffer too small");
                }
                outBuf = ByteBuffer.wrap(in);
                inBuf = ByteBuffer.wrap(in, inOfs, inLen);
                outBuf.position(outOfs);
                break;
        }
        if (useRO) {
            inBuf = inBuf.asReadOnlyBuffer();
        }
        if (DEBUG) {
            System.out.println("inBuf, pos = " + inBuf.position() +
                ", capacity = " + inBuf.capacity() +
                ", limit = " + inBuf.limit() +
                ", remaining = " + inBuf.remaining());
            System.out.println("outBuf, pos = " + outBuf.position() +
                ", capacity = " + outBuf.capacity() +
                ", limit = " + outBuf.limit() +
                ", remaining = " + outBuf.remaining());
        }
    }

    private static void runTest(int ofs, byte[] expectedPT, byte[] expectedCT)
            throws Exception {

        Cipher c = Cipher.getInstance(transformation);
        c.init(Cipher.ENCRYPT_MODE, KEY, params);
        int ciphertextSize = c.doFinal(inBuf, outBuf);

        
        outBuf.position(ofs);
        byte[] finalCT = new byte[ciphertextSize];
        if (DEBUG) {
            System.out.println("runTest, ciphertextSize = " + ciphertextSize);
            System.out.println("runTest, ofs = " + ofs +
                ", remaining = " + finalCT.length +
                ", limit = " + outBuf.limit());
        }
        outBuf.get(finalCT);

        if (!Arrays.equals(finalCT, expectedCT)) {
            System.err.println("Ciphertext mismatch:" +
                "\nresult   (len=" + finalCT.length + "):\n" +
                String.format("%0" + (finalCT.length << 1) + "x",
                    new BigInteger(1, finalCT)) +
                "\nexpected (len=" + expectedCT.length + "):\n" +
                String.format("%0" + (expectedCT.length << 1) + "x",
                    new BigInteger(1, expectedCT)));
            throw new Exception("ERROR: Ciphertext does not match");
        }

        
        outBuf.position(ofs);
        outBuf.limit(ofs + ciphertextSize);
        c.init(Cipher.DECRYPT_MODE, KEY, params);
        ByteBuffer finalPTBuf = ByteBuffer.allocate(
                c.getOutputSize(outBuf.remaining()));
        c.doFinal(outBuf, finalPTBuf);

        
        finalPTBuf.flip();
        byte[] finalPT = new byte[finalPTBuf.remaining()];
        finalPTBuf.get(finalPT);

        if (!Arrays.equals(finalPT, expectedPT)) {
            System.err.println("Ciphertext mismatch " +
                "):\nresult   (len=" + finalCT.length + "):\n" +
                String.format("%0" + (finalCT.length << 1) + "x",
                    new BigInteger(1, finalCT)) +
                "\nexpected (len=" + expectedCT.length + "):\n" +
                String.format("%0" + (expectedCT.length << 1) + "x",
                    new BigInteger(1, expectedCT)));
            throw new Exception("ERROR: Plaintext does not match");
        }
    }
}

