import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;

public class TestCopySafe {

    private static boolean DEBUG = false;

    private static int INPUT_LENGTH = 160;

    private static byte[] PT = new byte[INPUT_LENGTH];

    private static SecretKey KEY = new SecretKeySpec(new byte[16], "AES");

    private static byte[] IV = new byte[16];

    private static int[] OFFSETS = { 1, 8, 9, 16, 17, 32, 33 };

    private static final String[] MODES = { "ECB", "CBC", "PCBC", "CTR", "CTS", "CFB", "CFB8", "CFB16", "CFB24", "CFB32", "CFB40", "CFB48", "CFB56", "CFB64", "OFB", "OFB8", "OFB16", "OFB24", "OFB32", "OFB40", "OFB48", "OFB56", "OFB64", "GCM" };

    public static void main(String[] argv) throws Exception {
        Provider p = Security.getProvider("SunJCE");
        AlgorithmParameterSpec params = null;
        boolean result = true;
        for (String mode : MODES) {
            String transformation = "AES/" + mode + "/NoPadding";
            boolean isGCM = (mode == "GCM");
            if (isGCM) {
                params = new GCMParameterSpec(128, IV);
            } else if (mode != "ECB") {
                params = new IvParameterSpec(IV);
            }
            Cipher c = Cipher.getInstance(transformation, p);
            System.out.println("Testing " + transformation + ":");
            for (int offset : OFFSETS) {
                System.out.print("=> offset " + offset + ": ");
                try {
                    test(c, params, offset, isGCM);
                    System.out.println("Passed");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    result = false;
                    continue;
                }
            }
        }
        if (!result) {
            throw new Exception("One or more test failed");
        }
    }

    private static void test(Cipher c, AlgorithmParameterSpec params, int offset, boolean isGCM) throws Exception {
        if (isGCM) {
            c.init(Cipher.ENCRYPT_MODE, KEY);
        }
        c.init(Cipher.ENCRYPT_MODE, KEY, params);
        byte[] answer = c.doFinal(PT);
        byte[] pt2 = Arrays.copyOf(PT, answer.length + offset);
        if (isGCM) {
            c.init(Cipher.ENCRYPT_MODE, KEY);
            c.init(Cipher.ENCRYPT_MODE, KEY, params);
        }
        c.doFinal(pt2, 0, PT.length, pt2, 0);
        if (!isTwoArraysEqual(pt2, 0, answer, 0, answer.length)) {
            throw new Exception("Enc#1 diff check failed!");
        } else if (DEBUG) {
            System.out.println("Enc#1 diff check passed");
        }
        System.arraycopy(PT, 0, pt2, 0, PT.length);
        if (isGCM) {
            c.init(Cipher.ENCRYPT_MODE, KEY);
            c.init(Cipher.ENCRYPT_MODE, KEY, params);
        }
        c.doFinal(pt2, 0, PT.length, pt2, offset);
        if (!isTwoArraysEqual(pt2, offset, answer, 0, answer.length)) {
            throw new Exception("Enc#2 diff check failed");
        } else if (DEBUG) {
            System.out.println("Enc#2 diff check passed");
        }
        System.arraycopy(PT, 0, pt2, offset, PT.length);
        if (isGCM) {
            c.init(Cipher.ENCRYPT_MODE, KEY);
            c.init(Cipher.ENCRYPT_MODE, KEY, params);
        }
        c.doFinal(pt2, offset, PT.length, pt2, 0);
        if (!isTwoArraysEqual(pt2, 0, answer, 0, answer.length)) {
            throw new Exception("Enc#3 diff check failed");
        } else if (DEBUG) {
            System.out.println("Enc#3 diff check passed");
        }
        c.init(Cipher.DECRYPT_MODE, KEY, params);
        pt2 = Arrays.copyOf(answer, answer.length + offset);
        c.doFinal(pt2, 0, answer.length, pt2, 0);
        if (!isTwoArraysEqual(pt2, 0, PT, 0, PT.length)) {
            throw new Exception("Dec#1 diff check failed!");
        } else if (DEBUG) {
            System.out.println("Dec#1 diff check passed");
        }
        System.arraycopy(answer, 0, pt2, 0, answer.length);
        c.doFinal(pt2, 0, answer.length, pt2, offset);
        if (!isTwoArraysEqual(pt2, offset, PT, 0, PT.length)) {
            throw new Exception("Dec#2 diff check failed");
        } else if (DEBUG) {
            System.out.println("Dec#2 diff check passed");
        }
        System.arraycopy(answer, 0, pt2, offset, answer.length);
        c.doFinal(pt2, offset, answer.length, pt2, 0);
        if (!isTwoArraysEqual(pt2, 0, PT, 0, PT.length)) {
            throw new Exception("Dec#3 diff check failed");
        } else if (DEBUG) {
            System.out.println("Dec#3 diff check passed");
        }
    }

    private static boolean isTwoArraysEqual(byte[] a, int aOff, byte[] b, int bOff, int len) {
        for (int i = 0; i < len; i++) {
            if (a[aOff + i] != b[bOff + i]) {
                return false;
            }
        }
        return true;
    }
}
