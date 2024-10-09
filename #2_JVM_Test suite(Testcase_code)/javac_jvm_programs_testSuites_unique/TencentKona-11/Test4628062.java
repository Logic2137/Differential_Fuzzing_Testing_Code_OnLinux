


import java.security.*;
import javax.crypto.*;
import java.util.*;

public class Test4628062 {

    private static final int[] AES_SIZES = { 16, 24, 32 }; 
    private static final int[] HMACSHA224_SIZES = { 28 };
    private static final int[] HMACSHA256_SIZES = { 32 };
    private static final int[] HMACSHA384_SIZES = { 48 };
    private static final int[] HMACSHA512_SIZES = { 64 };

    public boolean execute(String algo, int[] keySizes) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(algo, "SunJCE");

        
        Key keyWithDefaultSize = kg.generateKey();
        byte[] encoding = keyWithDefaultSize.getEncoded();
        int defKeyLen = encoding.length ;
        if (defKeyLen == 0) {
            throw new Exception("default key length is 0!");
        } else if (defKeyLen != keySizes[0]) {
            throw new Exception("default key length mismatch!");
        }

        
        if (keySizes.length > 1) {
            
            
            for (int i=0; i<keySizes.length; i++) {
                kg.init(keySizes[i]*8); 
                Key key = kg.generateKey();
                if (key.getEncoded().length != keySizes[i]) {
                    throw new Exception("key is generated with the wrong length!");
                }
            }
            
            
            try {
                kg.init(keySizes[0]*8+1);
            } catch (InvalidParameterException ex) {
            } catch (Exception ex) {
                throw new Exception("wrong exception is thrown for invalid key size!");
            }
        }
        
        return true;
    }

    public static void main (String[] args) throws Exception {
        Test4628062 test = new Test4628062();
        String testName = test.getClass().getName();
        if (test.execute("AES", AES_SIZES)) {
            System.out.println(testName + ": AES Passed!");
        }
        if (test.execute("HmacSHA224", HMACSHA224_SIZES)) {
            System.out.println(testName + ": HmacSHA224 Passed!");
        }
        if (test.execute("HmacSHA256", HMACSHA256_SIZES)) {
            System.out.println(testName + ": HmacSHA256 Passed!");
        }
        if (test.execute("HmacSHA384", HMACSHA384_SIZES)) {
            System.out.println(testName + ": HmacSHA384 Passed!");
        }
        if (test.execute("HmacSHA512", HMACSHA512_SIZES)) {
            System.out.println(testName + ": HmacSHA512 Passed!");
        }
    }
}
