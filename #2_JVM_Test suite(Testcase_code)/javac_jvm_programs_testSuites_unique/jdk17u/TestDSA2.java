import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

public class TestDSA2 {

    private static final String PROV = "SUN";

    private static final String[] SIG_ALGOS = { "NONEwithDSA", "SHA1withDSA", "SHA224withDSA", "SHA256withDSA", "NONEwithDSAinP1363Format", "SHA1withDSAinP1363Format", "SHA224withDSAinP1363Format", "SHA256withDSAinP1363Format" };

    private static final int[] KEYSIZES = { 1024, 2048 };

    public static void main(String[] args) throws Exception {
        boolean[] expectedToPass = { true, true, true, true, true, true, true, true };
        test(1024, expectedToPass);
        boolean[] expectedToPass2 = { true, false, true, true, true, false, true, true };
        test(2048, expectedToPass2);
    }

    private static void test(int keySize, boolean[] testStatus) throws Exception {
        byte[] data = "12345678901234567890".getBytes();
        System.out.println("Test against key size: " + keySize);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", PROV);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        if (testStatus.length != SIG_ALGOS.length) {
            throw new RuntimeException("TestError: incorrect status array!");
        }
        for (int i = 0; i < SIG_ALGOS.length; i++) {
            Signature dsa = Signature.getInstance(SIG_ALGOS[i], PROV);
            try {
                dsa.initSign(pair.getPrivate());
                dsa.update(data);
                byte[] sig = dsa.sign();
                dsa.initVerify(pair.getPublic());
                dsa.update(data);
                boolean verifies = dsa.verify(sig);
                if (verifies == testStatus[i]) {
                    System.out.println(SIG_ALGOS[i] + ": Passed");
                } else {
                    System.out.println(SIG_ALGOS[i] + ": should " + (testStatus[i] ? "pass" : "fail"));
                    throw new RuntimeException(SIG_ALGOS[i] + ": Unexpected Test result!");
                }
            } catch (Exception ex) {
                if (testStatus[i]) {
                    ex.printStackTrace();
                    throw new RuntimeException(SIG_ALGOS[i] + ": Unexpected exception " + ex);
                } else {
                    System.out.println(SIG_ALGOS[i] + ": Passed, expected " + ex);
                }
            }
        }
    }
}
