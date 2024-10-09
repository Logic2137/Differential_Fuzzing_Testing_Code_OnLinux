



import java.util.*;
import java.nio.*;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class EmptyFinalBuffer {

    private static final String[] ALGOS = {
        "AES/ECB/PKCS5Padding", "AES/CBC/PKCS5Padding"
    };

    public static void main(String[] args) throws Exception {

        Provider[] provs = Security.getProviders();

        SecretKey key = new SecretKeySpec(new byte[16], "AES");

        boolean testFailed = false;
        for (Provider p : provs) {
            System.out.println("Testing: " + p.getName());
            for (String algo : ALGOS) {
                System.out.print("Algo: " + algo);
                Cipher c;
                try {
                    c = Cipher.getInstance(algo, p);
                } catch (NoSuchAlgorithmException nsae) {
                    
                    System.out.println("=> No Support");
                    continue;
                }
                c.init(Cipher.ENCRYPT_MODE, key);
                AlgorithmParameters params = c.getParameters();
                c.init(Cipher.DECRYPT_MODE, key, params);
                try {
                    byte[] out = c.doFinal(new byte[0]);
                    System.out.println("=> Accepted w/ " +
                        (out == null? "null" : (out.length + "-byte")) +
                        " output");
                } catch (Exception e) {
                    testFailed = true;
                    System.out.println("=> Rejected w/ Exception");
                    e.printStackTrace();
                }
            }
        }
        if (testFailed) {
            throw new Exception("One or more tests failed");
        } else {
            System.out.println("All tests passed");
        }
    }
}
