import java.security.SecureRandom;
import java.security.Security;

public class SeedGeneratorChoice {

    public static void main(String... arguments) throws Exception {
        for (String mech : new String[] { "SHA1PRNG", "Hash_DRBG", "HMAC_DRBG", "CTR_DRBG" }) {
            SecureRandom prng = null;
            if (!mech.contains("_DRBG")) {
                prng = SecureRandom.getInstance(mech);
            } else {
                Security.setProperty("securerandom.drbg.config", mech);
                prng = SecureRandom.getInstance("DRBG");
            }
            prng.generateSeed(1);
        }
    }
}
