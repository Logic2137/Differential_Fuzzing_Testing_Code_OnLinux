import java.io.*;
import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import java.util.*;

public class PBKDF2HmacSHA1FactoryTest {

    private static final String ALGO = "PBKDF2WithHmacSHA1";

    static final int[] KEY_SIZES = { 128, 256 };

    private static final TestVector[] TEST_VECTORS = { new TestVector(1, "password", "ATHENA.MIT.EDUraeburn", "cdedb5281bb2f801565a1122b25635150ad1f7a04bb9f3a333ecc0e2e1f70837"), new TestVector(2, "password", "ATHENA.MIT.EDUraeburn", "01dbee7f4a9e243e988b62c73cda935da05378b93244ec8f48a99e61ad799d86"), new TestVector(1200, "password", "ATHENA.MIT.EDUraeburn", "5c08eb61fdf71e4e4ec3cf6ba1f5512ba7e52ddbc5e5142f708a31e2e62b1e13"), new TestVector(5, "password", fromHexString("1234567878563412"), "d1daa78615f287e6a1c8b120d7062a493f98d203e6be49a6adf4fa574b6e64ee"), new TestVector(1200, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "pass phrase equals block size", "139c30c0966bc32ba55fdbf212530ac9c5ec59f1a452f5cc9ad940fea0598ed1"), new TestVector(1200, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "pass phrase exceeds block size", "9ccad6d468770cd51b10e6a68721be611a8b4d282601db3b36be9246915ec82a"), new TestVector(50, fromHexString("f09d849e"), "EXAMPLE.COMpianist", "6b9cf26d45455a43a5b8bb276a403b39e7fe37a0c41e02c281ff3069e1e94f52") };

    private static void test() throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGO, "SunJCE");
        for (int i = 0; i < TEST_VECTORS.length; i++) {
            System.out.println("=>Testing vector#" + (i + 1));
            TestVector tv = TEST_VECTORS[i];
            for (int j = 0; j < KEY_SIZES.length; j++) {
                PBEKeySpec keySpec = tv.keySpecs[j];
                PBEKey key = (PBEKey) skf.generateSecret(keySpec);
                byte[] derivedKey = key.getEncoded();
                if (!(key.getFormat().equalsIgnoreCase("RAW"))) {
                    throw new Exception("Wrong format for derived key");
                }
                if (derivedKey.length != KEY_SIZES[j] / 8) {
                    throw new Exception("Wrong length for derived key");
                }
                if (!tv.expectedVals[j].equals(toHexString(derivedKey))) {
                    System.out.println("got:      " + toHexString(derivedKey));
                    System.out.println("expected: " + tv.expectedVals[j]);
                    throw new Exception("Wrong value for derived key");
                }
                PBEKeySpec keySpec2 = (PBEKeySpec) skf.getKeySpec(key, PBEKeySpec.class);
                if (!isEqual(keySpec, keySpec2)) {
                    throw new Exception("Wrong derived keySpec");
                }
            }
        }
    }

    private static boolean isEqual(PBEKeySpec spec1, PBEKeySpec spec2) {
        if ((spec1 == null) || (spec2 == null))
            return false;
        if (Arrays.equals(spec1.getPassword(), spec2.getPassword()) && Arrays.equals(spec1.getSalt(), spec2.getSalt()) && spec1.getIterationCount() == spec2.getIterationCount() && spec1.getKeyLength() == spec2.getKeyLength()) {
            return true;
        }
        return false;
    }

    private static String toHexString(byte[] bytes) {
        String mapping = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int low = bytes[i] & 0x0f;
            int high = ((bytes[i] >> 4) & 0x0f);
            char[] res = new char[2];
            res[0] = mapping.charAt(high);
            res[1] = mapping.charAt(low);
            sb.append(res);
        }
        return sb.toString();
    }

    private static byte[] fromHexString(String value) {
        byte[] bytes = new byte[value.length() / 2];
        String mapping = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            String high = value.substring(2 * i, 2 * i + 1);
            String low = value.substring(2 * i + 1, 2 * i + 2);
            bytes[i] = (byte) ((mapping.indexOf(high) << 4) + mapping.indexOf(low));
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        test();
        System.out.println("Test Passed!");
    }
}

class TestVector {

    PBEKeySpec[] keySpecs;

    String[] expectedVals;

    TestVector(int iterCount, String password, String saltString, String expectedVal) {
        try {
            init(iterCount, password, saltString.getBytes("UTF-8"), expectedVal);
        } catch (Exception ex) {
            keySpecs = null;
            expectedVals = null;
        }
    }

    TestVector(int iterCount, byte[] passwordUTF8, String saltString, String expectedVal) {
        try {
            init(iterCount, new String(passwordUTF8, "UTF-8"), saltString.getBytes("UTF-8"), expectedVal);
        } catch (Exception ex) {
            keySpecs = null;
            expectedVals = null;
        }
    }

    TestVector(int iterCount, String password, byte[] salt, String expectedVal) {
        init(iterCount, password, salt, expectedVal);
    }

    private void init(int iterCount, String password, byte[] salt, String expectedVal) {
        try {
            int numOfKeySizes = PBKDF2HmacSHA1FactoryTest.KEY_SIZES.length;
            keySpecs = new PBEKeySpec[numOfKeySizes];
            expectedVals = new String[numOfKeySizes];
            for (int i = 0; i < numOfKeySizes; i++) {
                int keySize = PBKDF2HmacSHA1FactoryTest.KEY_SIZES[i];
                keySpecs[i] = new PBEKeySpec(password.toCharArray(), salt, iterCount, keySize);
                expectedVals[i] = expectedVal.substring(0, keySize / 4);
            }
        } catch (Exception ex) {
            keySpecs = null;
            expectedVals = null;
        }
    }
}
