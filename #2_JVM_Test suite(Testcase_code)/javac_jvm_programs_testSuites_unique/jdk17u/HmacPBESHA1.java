import java.io.PrintStream;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class HmacPBESHA1 {

    private static final String[] MAC_ALGOS = { "HmacPBESHA1", "PBEWithHmacSHA1", "PBEWithHmacSHA224", "PBEWithHmacSHA256", "PBEWithHmacSHA384", "PBEWithHmacSHA512" };

    private static final int[] MAC_LENGTHS = { 20, 20, 28, 32, 48, 64 };

    private static final String KEY_ALGO = "PBE";

    private static final String PROVIDER = "SunJCE";

    private static SecretKey key = null;

    public static void main(String[] argv) throws Exception {
        for (int i = 0; i < MAC_ALGOS.length; i++) {
            runtest(MAC_ALGOS[i], MAC_LENGTHS[i]);
        }
        System.out.println("\nTest Passed");
    }

    private static void runtest(String algo, int length) throws Exception {
        System.out.println("Testing: " + algo);
        if (key == null) {
            char[] password = { 't', 'e', 's', 't' };
            PBEKeySpec keySpec = new PBEKeySpec(password);
            SecretKeyFactory kf = SecretKeyFactory.getInstance(KEY_ALGO, PROVIDER);
            key = kf.generateSecret(keySpec);
        }
        Mac mac = Mac.getInstance(algo, PROVIDER);
        byte[] plainText = new byte[30];
        PBEParameterSpec spec = new PBEParameterSpec("saltValue".getBytes(), 250);
        mac.init(key, spec);
        mac.update(plainText);
        byte[] value1 = mac.doFinal();
        if (value1.length != length) {
            throw new Exception("incorrect MAC output length, expected " + length + ", got " + value1.length);
        }
        mac.update(plainText);
        byte[] value2 = mac.doFinal();
        if (!Arrays.equals(value1, value2)) {
            throw new Exception("generated different MAC outputs");
        }
    }
}
