


import java.util.Arrays;
import java.util.Random;
import java.security.Key;
import java.security.InvalidKeyException;
import javax.crypto.*;
import javax.crypto.spec.*;

public class TestKeySizeCheck {

    private static final byte[] BYTES_32 = new byte[32];
    static {
        for (int i = 0; i < BYTES_32.length; i++) {
            BYTES_32[i] = (byte) i;
        }
    }

    private static SecretKey getKey(int sizeInBytes) {
        if (sizeInBytes <= BYTES_32.length) {
            return new SecretKeySpec(BYTES_32, 0, sizeInBytes, "AES");
        } else {
            return new SecretKeySpec(new byte[sizeInBytes], "AES");
        }
    }

    private static String getModeStr(int mode) {
        return (mode == Cipher.ENCRYPT_MODE? "ENC" : "WRAP");
    }

    public static void test(String algo, int[] invalidKeySizes)
            throws Exception {

        System.out.println("Testing " + algo);
        Cipher c = Cipher.getInstance(algo, "SunJCE");

        int[] modes = { Cipher.ENCRYPT_MODE, Cipher.WRAP_MODE };
        for (int ks : invalidKeySizes) {
            System.out.println("keysize: " + ks);
            SecretKey key = getKey(ks);

            for (int m : modes) {
                try {
                    c.init(m, key);
                    throw new RuntimeException("Expected IKE not thrown for "
                            + getModeStr(m));
                } catch (InvalidKeyException ike) {
                    System.out.println(" => expected IKE thrown for "
                            + getModeStr(m));
                }
            }
        }
    }

    public static void main(String[] argv) throws Exception {

        test("AESWrap", new int[] { 120, 264 });
        test("AESWrap_128", new int[] { 192, 256 });
        test("AESWrap_192", new int[] { 128, 256 });
        test("AESWrap_256", new int[] { 128, 192 });
        test("AESWrapPad", new int[] { 120, 264 });
        test("AESWrapPad_128", new int[] { 192, 256 });
        test("AESWrapPad_192", new int[] { 128, 256 });
        test("AESWrapPad_256", new int[] { 128, 192 });

        test("AES/KW/NoPadding", new int[] { 120, 264 });
        test("AES_128/KW/NoPadding", new int[] { 192, 256 });
        test("AES_192/KW/NoPadding", new int[] { 128, 256 });
        test("AES_256/KW/NoPadding", new int[] { 128, 192 });
        test("AES/KWP/NoPadding", new int[] { 120, 264 });
        test("AES_128/KWP/NoPadding", new int[] { 192, 256 });
        test("AES_192/KWP/NoPadding", new int[] { 128, 256 });
        test("AES_256/KWP/NoPadding", new int[] { 128, 192 });

        System.out.println("All Tests Passed");
    }
}
