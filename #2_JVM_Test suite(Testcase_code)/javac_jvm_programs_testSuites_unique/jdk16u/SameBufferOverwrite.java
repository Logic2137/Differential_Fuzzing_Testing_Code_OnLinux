

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.AlgorithmParameters;
import java.util.Arrays;




public class SameBufferOverwrite {

    private SecretKey skey;
    private Cipher c;
    private int start = 17, end = 17;  

    SameBufferOverwrite(String algo, String transformation)
        throws Exception {

        KeyGenerator kg = KeyGenerator.getInstance(algo, "SunJCE");
        skey = kg.generateKey();
        c = Cipher.getInstance(transformation, "SunJCE");
    }

    
    void test() throws Exception {
        byte[] in = new byte[end + (c.getBlockSize() - (end % c.getBlockSize()))];
        Arrays.fill(in, (byte)8);
        int len = start;
        AlgorithmParameters params = null;

        System.out.println("Testing transformation: " + c.getAlgorithm() +
            ",  byte length from " + start + " to " + end);
        while (end >= len) {
            
            c.init(Cipher.ENCRYPT_MODE, skey, params);
            byte[] out = c.doFinal(in, 0, len);
            System.out.println("  enc = " + byteToHex(out));
            System.out.println("  => enc " + len + " bytes, ret " +
                (out == null ? "null" : (out.length + " byte")));

            
            params = c.getParameters();
            c.init(Cipher.DECRYPT_MODE, skey, params);
            int rLen = c.doFinal(out, 0, out.length, in);
            System.out.println("  dec = " + byteToHex(in));
            System.out.println("  => dec " + out.length + " bytes, ret " +
                rLen + " byte");
            
            for (int j = rLen; j < in.length; j++) {
                if (in[j] != (byte) 8) {
                    throw new Exception("Value check failed at index " + j);
                }
            }
            System.out.println(" Test Passed:  len = " + len);
            len++;

            
            if (c.getAlgorithm().contains("GCM")) {
                params = null;
            }
        }
    }

    
    SameBufferOverwrite iterate(int start, int end) {
        this.start = start;
        this.end = end;
        return this;
    }

    public static void main(String args[]) throws Exception {
        new SameBufferOverwrite("AES", "AES/GCM/NoPadding").iterate(1, 25).
            test();
        new SameBufferOverwrite("AES", "AES/CTR/NoPadding").iterate(1, 25).
            test();
        new SameBufferOverwrite("AES", "AES/CBC/PKCS5Padding").iterate(1, 25).
            test();
        new SameBufferOverwrite("AES", "AES/ECB/PKCS5Padding").iterate(1, 25).
            test();
        new SameBufferOverwrite("DES", "DES/CBC/PKCS5Padding").iterate(1, 17).
            test();
        new SameBufferOverwrite("DESede", "DESede/CBC/PKCS5Padding").iterate(1, 17).
            test();
    }

    private static String byteToHex(byte[] barray) {
        StringBuilder s = new StringBuilder();
        for (byte b : barray) {
            s.append(String.format("%02x", b));
        }
        return s.toString();
    }
}
