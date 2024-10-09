


import java.io.PrintStream;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Provider;

public class Test4517355 {

    private static final String ALGO = "AES";
    private static final int KEYSIZE = 16; 

    private static byte[] plainText = new byte[125];

    public void execute(String mode, String padding) throws Exception {
        String transformation = ALGO + "/" + mode + "/" + padding;

        Cipher ci = Cipher.getInstance(transformation, "SunJCE");
        KeyGenerator kg = KeyGenerator.getInstance(ALGO, "SunJCE");
        kg.init(KEYSIZE*8);
        SecretKey key = kg.generateKey();

        
        ci.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = ci.doFinal(plainText);

        if (mode.equalsIgnoreCase("GCM")) {
            AlgorithmParameters params = ci.getParameters();
            ci.init(Cipher.DECRYPT_MODE, key, params);
        } else {
            byte[] iv = ci.getIV();
            AlgorithmParameterSpec aps = new IvParameterSpec(iv);
            ci.init(Cipher.DECRYPT_MODE, key, aps);
        }
        byte[] recoveredText = new byte[plainText.length];
        try {
            int len = ci.doFinal(cipherText, 0, cipherText.length,
                                 recoveredText);
        } catch (ShortBufferException ex) {
            throw new Exception("output buffer is the right size!");
        }

        
        
        if (!Arrays.equals(plainText, recoveredText)) {
            throw new Exception("encryption/decryption does not work!");
        }
        
        if (Arrays.equals(plainText, cipherText)) {
            throw new Exception("encryption does not work!");
        }
        
        if (padding.equalsIgnoreCase("PKCS5Padding")) {
            if ((cipherText.length/16)*16 != cipherText.length) {
                throw new Exception("padding does not work!");
            }
        }
        System.out.println(transformation + ": Passed");
    }

    public static void main (String[] args) throws Exception {
        Test4517355 test = new Test4517355();
        Random rdm = new Random();
        rdm.nextBytes(test.plainText);

        test.execute("CBC", "PKCS5Padding");
        test.execute("GCM", "NoPadding");
    }
}
