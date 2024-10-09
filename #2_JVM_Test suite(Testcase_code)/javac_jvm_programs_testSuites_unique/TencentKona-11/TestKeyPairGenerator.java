










import java.security.*;
import java.security.interfaces.*;

public class TestKeyPairGenerator {

    private static void checkKeyLength(KeyPair kp, int len) throws Exception {
        DSAPublicKey key = (DSAPublicKey)kp.getPublic();
        int n = key.getParams().getP().bitLength();
        System.out.println("Key length: " + n);
        if (len != n) {
            throw new Exception("Wrong key length");
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        KeyPairGenerator kpg;
        KeyPair kp;
        
        
        
        kpg = KeyPairGenerator.getInstance("DSA", "SUN");
        kp = kpg.generateKeyPair();

        kpg = KeyPairGenerator.getInstance("DSA", "SUN");
        kp = kpg.generateKeyPair();

        
        kp = kpg.generateKeyPair();

        kpg.initialize(1024);
        kp = kpg.generateKeyPair();
        checkKeyLength(kp, 1024);

        kpg.initialize(768);
        kp = kpg.generateKeyPair();
        checkKeyLength(kp, 768);

        kpg.initialize(512);
        kp = kpg.generateKeyPair();
        checkKeyLength(kp, 512);

        kpg.initialize(2048);
        kp = kpg.generateKeyPair();
        checkKeyLength(kp, 2048);

        kpg.initialize(3072);
        kp = kpg.generateKeyPair();
        checkKeyLength(kp, 3072);

        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + " ms.");
    }
}
