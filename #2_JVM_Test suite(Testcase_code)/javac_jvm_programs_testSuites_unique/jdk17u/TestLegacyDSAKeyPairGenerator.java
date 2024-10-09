import java.security.*;
import java.security.interfaces.*;

public class TestLegacyDSAKeyPairGenerator {

    private static void checkKeyLength(KeyPair kp, int len) throws Exception {
        DSAPublicKey key = (DSAPublicKey) kp.getPublic();
        int n = key.getParams().getP().bitLength();
        System.out.println("Key length: " + n);
        if (len != n) {
            throw new Exception("Wrong key length");
        }
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA", "SUN");
        if (!(kpg instanceof DSAKeyPairGenerator)) {
            throw new Exception("Should be an instance of DSAKeyPairGenerator");
        }
        System.out.println("Returned an instance of DSAKeyPairGenerator");
        KeyPair kp1 = kpg.generateKeyPair();
        checkKeyLength(kp1, 1024);
        KeyPair kp2 = kpg.generateKeyPair();
        checkKeyLength(kp2, 1024);
        System.out.println("Used 1024 default key size");
        DSAParams param1 = ((DSAPublicKey) kp1.getPublic()).getParams();
        DSAParams param2 = ((DSAPublicKey) kp2.getPublic()).getParams();
        if ((param1.getP().compareTo(param2.getP()) != 0) || (param1.getQ().compareTo(param2.getQ()) != 0) || (param1.getG().compareTo(param2.getG()) != 0)) {
            throw new RuntimeException("Key params mismatch");
        }
        System.out.println("Used same default params");
        int sizeNotInCache = (1024 - 64);
        try {
            ((DSAKeyPairGenerator) kpg).initialize(sizeNotInCache, false, null);
            throw new RuntimeException("Expected IPE not thrown");
        } catch (InvalidParameterException ipe) {
            System.out.println("Throwed expected IPE");
        }
        ((DSAKeyPairGenerator) kpg).initialize(sizeNotInCache, true, null);
        KeyPair kp = kpg.generateKeyPair();
        checkKeyLength(kp, sizeNotInCache);
        System.out.println("Generated requested key size");
    }
}
