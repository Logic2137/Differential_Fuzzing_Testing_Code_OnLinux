


import java.security.SecureRandom;
import java.security.Security;
import static java.lang.Math.*;

public class EnoughSeedTest {

    private static final String DRBG_CONFIG = "securerandom.drbg.config";
    private static final String DRBG_CONFIG_VALUE
            = Security.getProperty(DRBG_CONFIG);

    public static void main(String[] args) {
        System.setProperty("java.security.egd", "file:/dev/urandom");

        boolean success = true;
        for (String mech : new String[]{
            "SHA1PRNG", "Hash_DRBG", "HMAC_DRBG", "CTR_DRBG"}) {
            System.out.printf("%nTest for SecureRandom algorithm: '%s'", mech);
            try {
                SecureRandom sr = null;
                if (!mech.contains("_DRBG")) {
                    sr = SecureRandom.getInstance(mech);
                } else {
                    Security.setProperty(DRBG_CONFIG, mech);
                    sr = SecureRandom.getInstance("DRBG");
                }

                success &= forEachSeedBytes(sr);
                System.out.printf("%nCompleted test for SecureRandom "
                        + "mechanism: '%s'", mech);
            } catch (Exception e) {
                success &= false;
                e.printStackTrace(System.out);
            } finally {
                Security.setProperty(DRBG_CONFIG, DRBG_CONFIG_VALUE);
            }
        }
        if (!success) {
            throw new RuntimeException("At least one test failed.");
        }
    }

    
    private static boolean forEachSeedBytes(SecureRandom sr) {
        boolean success = true;
        sr.setSeed(1l);
        for (int seedByte : new int[]{Integer.MIN_VALUE, -1, 0, 1, 256, 1024,
            Short.MAX_VALUE, (int) pow(2, 20)}) {
            try {
                byte[] seed = sr.generateSeed(seedByte);
                if (seed.length != seedByte) {
                    throw new RuntimeException("Not able to produce expected "
                            + "seed size.");
                }
            } catch (IllegalArgumentException e) {
                if (seedByte >= 0) {
                    throw new RuntimeException("Unknown Exception occured.", e);
                }
                System.out.printf("%nPASS - Exception expected when required "
                        + "seed size requested is negative: %s", seedByte);
            }
        }
        return success;
    }

}
