import java.util.*;
import java.nio.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class GetMaxAllowed {

    private static void runTest1(boolean isUnlimited) throws Exception {
        System.out.println("Testing " + (isUnlimited ? "un" : "") + "limited policy...");
        String algo = "Blowfish";
        int keyLength = Cipher.getMaxAllowedKeyLength(algo);
        AlgorithmParameterSpec spec = Cipher.getMaxAllowedParameterSpec(algo);
        if (isUnlimited) {
            if ((keyLength != Integer.MAX_VALUE) || (spec != null)) {
                throw new Exception("Check for " + algo + " failed under unlimited policy");
            }
        } else {
            if ((keyLength != 128) || (spec != null)) {
                throw new Exception("Check for " + algo + " failed under default policy");
            }
        }
        algo = "RC5";
        keyLength = Cipher.getMaxAllowedKeyLength(algo);
        RC5ParameterSpec rc5param = (RC5ParameterSpec) Cipher.getMaxAllowedParameterSpec(algo);
        if (isUnlimited) {
            if ((keyLength != Integer.MAX_VALUE) || (rc5param != null)) {
                throw new Exception("Check for " + algo + " failed under unlimited policy");
            }
        } else {
            if ((keyLength != 128) || (rc5param.getRounds() != 12) || (rc5param.getVersion() != Integer.MAX_VALUE) || (rc5param.getWordSize() != Integer.MAX_VALUE)) {
                throw new Exception("Check for " + algo + " failed under default policy");
            }
        }
        System.out.println("All tests passed");
    }

    private static void runTest2() throws Exception {
        System.out.println("Testing against Security.getAlgorithms()");
        Set<String> algorithms = Security.getAlgorithms("Cipher");
        for (String algorithm : algorithms) {
            int keylength = -1;
            keylength = Cipher.getMaxAllowedKeyLength(algorithm);
        }
    }

    public static void main(String[] args) throws Exception {
        boolean isUnlimited = true;
        Cipher c = Cipher.getInstance("AES", "SunJCE");
        try {
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(new byte[24], "AES"));
        } catch (InvalidKeyException ike) {
            isUnlimited = false;
        }
        runTest1(isUnlimited);
        runTest2();
    }
}
