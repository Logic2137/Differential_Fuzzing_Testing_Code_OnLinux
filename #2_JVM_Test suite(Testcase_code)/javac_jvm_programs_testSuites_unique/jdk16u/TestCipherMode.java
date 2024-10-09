




import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class TestCipherMode {

    private static final String ALGO = "DES";

    public static void main(String[] argv) throws Exception {
        TestCipherMode test = new TestCipherMode();
        System.out.println("Testing ENCRYPT_MODE...");
        test.checkMode(Cipher.ENCRYPT_MODE, "encryption");
        System.out.println("Testing DECRYPT_MODE...");
        test.checkMode(Cipher.DECRYPT_MODE, "decryption");
        System.out.println("Testing WRAP_MODE...");
        test.checkMode(Cipher.WRAP_MODE, "key wrapping");
        System.out.println("Testing UNWRAP_MODE...");
        test.checkMode(Cipher.UNWRAP_MODE, "key unwrapping");
        System.out.println("All Tests Passed");
   }

    private Cipher c = null;
    private SecretKey key = null;

    private TestCipherMode() throws NoSuchAlgorithmException,
    NoSuchProviderException, NoSuchPaddingException {
        c = Cipher.getInstance(ALGO + "/ECB/PKCS5Padding", "SunJCE");
        String output = c.toString();
        if (!output.equals(
                "Cipher.DES/ECB/PKCS5Padding, mode: not initialized, algorithm from: SunJCE")) {
            throw new RuntimeException(
                    "Unexpected Cipher.toString() output:" + output);
        }
        key = new SecretKeySpec(new byte[8], ALGO);
    }

    private void checkMode(int mode, String opString) throws Exception {
        c.init(mode, key);
        String output = c.toString();
        if (!output.contains("Cipher.DES/ECB/PKCS5Padding")
                && !output.contains(opString)
                && !output.contains("Algorithm from: SunJCE")) {
            throw new Exception("Unexpected toString() output:" + output);
        }

        switch (mode) {
        case Cipher.ENCRYPT_MODE:
        case Cipher.DECRYPT_MODE:
            
            try {
                c.wrap(key);
                throw new Exception("ERROR: should throw ISE for wrap()");
            } catch (IllegalStateException ise) {
                System.out.println("expected ISE is thrown for wrap()");
            }
            try {
                c.unwrap(new byte[16], ALGO, Cipher.SECRET_KEY);
                throw new Exception("ERROR: should throw ISE for unwrap()");
            } catch (IllegalStateException ise) {
                System.out.println("expected ISE is thrown for unwrap()");
            }
            break;
        case Cipher.WRAP_MODE:
        case Cipher.UNWRAP_MODE:
            try {
                c.update(new byte[16]);
                throw new Exception("ERROR: should throw ISE for update()");
            } catch (IllegalStateException ise) {
                System.out.println("expected ISE is thrown for update()");
            }
            try {
                c.doFinal();
                throw new Exception("ERROR: should throw ISE for doFinal()");
            } catch (IllegalStateException ise) {
                System.out.println("expected ISE is thrown for doFinal()");
            }
            break;
        }
    }
}
