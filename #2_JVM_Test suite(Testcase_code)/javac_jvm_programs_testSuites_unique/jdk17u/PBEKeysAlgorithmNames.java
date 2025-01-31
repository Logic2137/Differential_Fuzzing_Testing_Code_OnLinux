import javax.crypto.*;
import java.security.spec.*;
import javax.crypto.spec.*;

public class PBEKeysAlgorithmNames {

    static String[] algs = { "PBEWithMD5AndDES", "PBEWithSHA1AndDESede", "PBEWithSHA1AndRC2_40", "PBEWithSHA1AndRC2_128", "PBEWithMD5AndTripleDES", "PBEWithSHA1AndRC4_40", "PBEWithSHA1AndRC4_128", "PBKDF2WithHmacSHA1", "PBKDF2WithHmacSHA224", "PBKDF2WithHmacSHA256", "PBKDF2WithHmacSHA384", "PBKDF2WithHmacSHA512" };

    public static void main(String[] argv) throws Exception {
        byte[] b = new byte[64];
        PBEKeySpec pbeks = new PBEKeySpec("password".toCharArray(), b, 20, 60);
        for (String s : algs) {
            System.out.println("Testing " + s);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(s, "SunJCE");
            System.out.println("    Checking skf.getAlgorithm()");
            if (!skf.getAlgorithm().equalsIgnoreCase(s)) {
                throw new Exception("getAlgorithm() \n\"" + skf.getAlgorithm() + "\" != \"" + s + "\"");
            }
            System.out.println("    Checking skf.generateSecret()");
            SecretKey sk = skf.generateSecret(pbeks);
            if (!sk.getAlgorithm().equalsIgnoreCase(s)) {
                throw new Exception("getAlgorithm() \n\"" + sk.getAlgorithm() + "\" != \"" + s + "\"");
            }
            System.out.println("    Checking skf.translateKey()");
            SecretKey sk1 = skf.translateKey(sk);
            if (!sk1.getAlgorithm().equalsIgnoreCase(s)) {
                throw new Exception("    getAlgorithm() \n\"" + sk.getAlgorithm() + "\" != \"" + s + "\"");
            }
            System.out.println("    Checking skf.getKeySpec()");
            KeySpec ks = skf.getKeySpec(sk, PBEKeySpec.class);
            System.out.println("    passed.\n");
        }
        System.out.println("Test Passed");
    }
}
