


import java.security.*;
import javax.crypto.*;
import java.util.*;


public class Test4626070 {
    private static final String ALGO = "AES";
    private static final int KEYSIZE = 16; 

    public void execute(String mode, String padding) throws Exception {
        String transformation = ALGO + "/" + mode + "/" + padding;
        Cipher ci = Cipher.getInstance(transformation, "SunJCE");
        KeyGenerator kg = KeyGenerator.getInstance(ALGO, "SunJCE");
        kg.init(KEYSIZE*8);
        SecretKey key = kg.generateKey();

        AlgorithmParameters params = ci.getParameters();

        
        ci.init(Cipher.WRAP_MODE, key, params);
        byte[] wrappedKeyEncoding = ci.wrap(key);
        params = ci.getParameters();
        ci.init(Cipher.UNWRAP_MODE, key, params);
        Key recoveredKey = ci.unwrap(wrappedKeyEncoding, "AES",
                                     Cipher.SECRET_KEY);
        if (!key.equals(recoveredKey)) {
            throw new Exception(
                "key after wrap/unwrap is different from the original!");
        }
        System.out.println(transformation + ": Passed");
    }

    public static void main (String[] args) throws Exception {
        Test4626070 test = new Test4626070();
        test.execute("CBC", "PKCS5Padding");
        test.execute("GCM", "NoPadding");
    }
}
