import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.interfaces.*;
import java.util.*;

public class TestLeadingZeros {

    static final String[] PKCS8_ENCODINGS = { "301e020100301206052b0e03020c30090201020201030201040403020101A000", "301f02020000301206052b0e03020c30090201020201030201040403020101A000", "301f020100301306052b0e03020c300a020200020201030201040403020101A000", "3020020100301206052b0e03020c300902010202010302010404050203000001A000" };

    public static void main(String[] argv) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("DSA", "SUN");
        for (String encodings : PKCS8_ENCODINGS) {
            byte[] encodingBytes = hexToBytes(encodings);
            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encodingBytes);
            DSAPrivateKey privKey2 = (DSAPrivateKey) factory.generatePrivate(encodedKeySpec);
            System.out.println("key: " + privKey2);
        }
        System.out.println("Test Passed");
    }

    private static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new RuntimeException("Input should be even length");
        }
        int size = hex.length() / 2;
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            int hi = Character.digit(hex.charAt(2 * i), 16);
            int lo = Character.digit(hex.charAt(2 * i + 1), 16);
            if ((hi == -1) || (lo == -1)) {
                throw new RuntimeException("Input should be hexadecimal");
            }
            result[i] = (byte) (16 * hi + lo);
        }
        return result;
    }
}
