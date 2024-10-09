



package compiler.codegen.aes;

import java.io.PrintStream;
import java.security.*;
import java.util.Random;
import java.lang.reflect.Method;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TestCipherBlockChainingEncrypt {
    private static String algorithm = "PBEWithHmacSHA1AndAES_256";
    private static final String PBEPASS = "Hush, it's supposed to be a secret!";

    private static final int INPUT_LENGTH = 800;
    private static final int[] OFFSETS = {0};
    private static final int NUM_PAD_BYTES = 8;
    private static final int PBKDF2_ADD_PAD_BYTES = 8;

    private static SecretKey key;
    private static Cipher ci;

    public static void main(String[] args) throws Exception {
     for(int i=0; i<5_000; i++) {
        if (!(new TestCipherBlockChainingEncrypt().test(args))) {
            throw new RuntimeException("TestCipherBlockChainingEncrypt test failed");
       }
     }
   }

    public boolean test(String[] args) throws Exception {
        boolean result = true;

        Provider p = Security.getProvider("SunJCE");
        ci = Cipher.getInstance(algorithm, p);
        key = SecretKeyFactory.getInstance(algorithm, p).generateSecret(
                        new PBEKeySpec(PBEPASS.toCharArray()));

        
        byte[] inputText = new byte[INPUT_LENGTH + NUM_PAD_BYTES
                + PBKDF2_ADD_PAD_BYTES];
        new Random().nextBytes(inputText);

        try {
            
            execute(Cipher.ENCRYPT_MODE,
                    inputText,
                    0,
                    INPUT_LENGTH);

            
            int padLength = NUM_PAD_BYTES + PBKDF2_ADD_PAD_BYTES;

            
            
            
            execute(Cipher.DECRYPT_MODE,
                    inputText,
                    0,
                    INPUT_LENGTH + padLength);

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            result = false;
        }
        return result;
    }

    private void execute(int edMode, byte[] inputText, int offset, int len) {
        try {
            
            if (Cipher.ENCRYPT_MODE == edMode) {
                ci.init(Cipher.ENCRYPT_MODE, this.key);
            } else {
                ci.init(Cipher.DECRYPT_MODE, this.key, ci.getParameters());
            }

            
            byte[] outputText = ci.doFinal(inputText, offset, len);

            
            int myoff = offset / 2;
            int off = ci.update(inputText, offset, len, inputText, myoff);
            ci.doFinal(inputText, myoff + off);

            
            boolean e = equalsBlock(inputText, myoff, outputText, 0,
                    outputText.length);
        } catch (Exception ex) {
                System.out.println("Got unexpected exception for " + algorithm);
                ex.printStackTrace(System.out);
        }
    }

    private boolean equalsBlock(byte[] b1, int off1,
            byte[] b2, int off2, int len) {
        for (int i = off1, j = off2, k = 0; k < len; i++, j++, k++) {
            if (b1[i] != b2[j]) {
                return false;
            }
        }
        return true;
    }
}
