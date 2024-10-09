

import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


public class KeyWrapper {

    static final String AES = "AES";
    static final String TRANSFORMATION = "AES/GCM/NoPadding";
    static final String PROVIDER = "SunJCE";
    static final int KEY_LENGTH = 128;

    public static void main(String argv[]) throws Exception {
        doTest(PROVIDER, TRANSFORMATION);
    }

    private static void doTest(String provider, String algo) throws Exception {
        SecretKey key;
        SecretKey keyToWrap;

        
        KeyGenerator kg = KeyGenerator.getInstance(AES, PROVIDER);
        kg.init(KEY_LENGTH);
        key = kg.generateKey();
        keyToWrap = kg.generateKey();

        
        Cipher cipher = Cipher.getInstance(algo, provider);
        cipher.init(Cipher.WRAP_MODE, key);
        AlgorithmParameters params = cipher.getParameters();

        
        byte[] keyWrapper = cipher.wrap(keyToWrap);
        try {
            
            keyWrapper = cipher.wrap(keyToWrap);
            throw new RuntimeException(
                    "FAILED: expected IllegalStateException hasn't "
                            + "been thrown ");
        } catch (IllegalStateException ise) {
            System.out.println(ise.getMessage());
            System.out.println("Expected exception");
        }

        
        cipher.init(Cipher.UNWRAP_MODE, key, params);
        cipher.unwrap(keyWrapper, algo, Cipher.SECRET_KEY);

        
        Key unwrapKey = cipher.unwrap(keyWrapper, algo, Cipher.SECRET_KEY);

        if (!Arrays.equals(keyToWrap.getEncoded(), unwrapKey.getEncoded())) {
            throw new RuntimeException(
                    "FAILED: original and unwrapped keys are not equal");
        }
    }
}
