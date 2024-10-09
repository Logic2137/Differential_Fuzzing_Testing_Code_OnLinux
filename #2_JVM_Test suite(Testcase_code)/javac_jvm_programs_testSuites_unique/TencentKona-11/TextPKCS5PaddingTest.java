


import static java.lang.System.out;

import java.security.AlgorithmParameters;
import java.security.Provider;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class TextPKCS5PaddingTest {
    
    private static final byte[] PLAIN_TEXT = {
        0b10001, 0b10001, 0b10001, 0b10001,
        0b10001, 0b10001, 0b11,    0b11
    };

    public static void main(String[] args) throws Exception {
        Provider provider = Security.getProvider("SunJCE");
        if (provider == null) {
            throw new RuntimeException("SunJCE provider not exist");
        }
        
        Cipher c = Cipher.getInstance("DES/CBC/NoPadding", provider);
        KeyGenerator kgen = KeyGenerator.getInstance("DES", provider);
        SecretKey skey = kgen.generateKey();
        

        c.init(Cipher.ENCRYPT_MODE, skey);
        
        byte[] cipher = c.doFinal(PLAIN_TEXT);
        AlgorithmParameters params = c.getParameters();
        
        c = Cipher.getInstance("DES/CBC/PKCS5Padding", provider);
        c.init(Cipher.DECRYPT_MODE, skey, params);
        try {
            c.doFinal(cipher);
            throw new RuntimeException(
                    "ERROR: Expected BadPaddingException not thrown");
        } catch (BadPaddingException expected) {
            out.println("Expected BadPaddingException thrown");
        }

    }
}
