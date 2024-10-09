import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.List;

public class GetAlgorithm {

    private static final String BASE = System.getProperty("test.src", ".");

    private static final String DRBG_CONFIG = "securerandom.drbg.config";

    private static final String DRBG_CONFIG_VALUE = Security.getProperty(DRBG_CONFIG);

    public static void main(String[] args) throws Exception {
        SecureRandom sr = new SecureRandom();
        if (sr.getAlgorithm().equals("unknown")) {
            throw new Exception("Unknown: " + sr.getAlgorithm());
        }
        for (String mech : new String[] { supportedNativeAlgo(), "SHA1PRNG", "Hash_DRBG", "HMAC_DRBG", "CTR_DRBG" }) {
            if (!mech.contains("_DRBG")) {
                check(mech, SecureRandom.getInstance(mech));
            } else {
                try {
                    Security.setProperty(DRBG_CONFIG, mech);
                    check("DRBG", SecureRandom.getInstance("DRBG"));
                } finally {
                    Security.setProperty(DRBG_CONFIG, DRBG_CONFIG_VALUE);
                }
            }
        }
        check("unknown", new MySecureRandom());
        InputStream in = new FileInputStream(new File(BASE, "sha1prng-old.bin"));
        ObjectInputStream oin = new ObjectInputStream(in);
        sr = (SecureRandom) oin.readObject();
        oin.close();
        check("unknown", sr);
        in = new FileInputStream(new File(BASE, "sha1prng-new.bin"));
        oin = new ObjectInputStream(in);
        sr = (SecureRandom) oin.readObject();
        oin.close();
        check("SHA1PRNG", sr);
        System.out.println("All tests passed");
    }

    private static void check(String s1, SecureRandom sr) throws Exception {
        String s2 = sr.getAlgorithm();
        if (s1.equals(s2) == false) {
            throw new Exception("Expected " + s1 + ", got " + s2);
        }
    }

    private static class MySecureRandom extends SecureRandom {
    }

    private static String supportedNativeAlgo() {
        String nativeSr = "Windows-PRNG";
        try {
            SecureRandom.getInstance(nativeSr);
        } catch (NoSuchAlgorithmException e) {
            nativeSr = "NativePRNG";
        }
        return nativeSr;
    }
}
